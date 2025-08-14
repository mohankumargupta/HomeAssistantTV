package com.mohankumargupta.homeassistanttv.data.repository

import android.content.Context
import com.mohankumargupta.homeassistanttv.data.local.PreferencesDataStore
import com.mohankumargupta.homeassistanttv.data.model.Endpoint
import com.mohankumargupta.homeassistanttv.data.model.UserPreferences
import com.mohankumargupta.homeassistanttv.data.remote.DiscoveryEvent
import com.mohankumargupta.homeassistanttv.data.remote.HTTPDataSource
import com.mohankumargupta.homeassistanttv.data.remote.MDNSDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject

class MDNSRepositoryImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    private val mdnsDataSource: MDNSDataSource,
    private val okHttpClient: OkHttpClient,
    private val preferencesDataSource: PreferencesDataStore
) : MDNSRepository {
    override fun discoverEndpoints(service: String): Flow<List<Endpoint>> = channelFlow {
        val nameToKey = mutableMapOf<String, String>()
        val devices = linkedMapOf<String, Endpoint>()

        val job = launch {
            mdnsDataSource.discoverServices(context, service).collect { event ->
                when (event) {
                    is DiscoveryEvent.Discovered -> {
                        // Resolve full details (port + addresses)
                        val resolved =
                            runCatching { event.onResolve() }.getOrNull() ?: return@collect

                        val ip = resolved.addresses.firstOrNull { it.isIpv4() }
                            ?: resolved.host.takeIf { it.isNotBlank() }
                            ?: resolved.addresses.firstOrNull()
                            ?: return@collect

                        val port = resolved.port.takeIf { it > 0 } ?: return@collect
                        val key = "$ip:$port"

                        nameToKey[resolved.name] = key

                        val ha = Endpoint(ip = ip, port = port)
                        // Only emit when something changed
                        val changed = devices[key] != ha
                        devices[key] = ha
                        if (changed) {
                            trySend(devices.values.toList())
                        }
                    }

                    is DiscoveryEvent.Removed -> {
                        val key = nameToKey.remove(event.service.name)
                        if (key != null && devices.remove(key) != null) {
                            trySend(devices.values.toList())
                        }
                    }
                }
            }
        }

        awaitClose { job.cancel() }
    }

    override fun getAccessToken(endpoint: Endpoint): Flow<String> =
        flow {
            val retrofit = Retrofit
                .Builder()
                .baseUrl("http://${endpoint.ip}:1880/")
                .client(okHttpClient)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

            val api = retrofit.create(HTTPDataSource::class.java)
            emit(api.getAccessToken())
        }

    override fun getUserPreferences(): Flow<UserPreferences> = preferencesDataSource.getPreferences()

    override suspend fun saveUserPreferences(userPreferences: UserPreferences) {
        preferencesDataSource.savePreferences(userPreferences)
    }

    private fun String.isIpv4(): Boolean = !contains(':')
}

