package com.mohankumargupta.homeassistanttv.data.repository

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import com.mohankumargupta.homeassistanttv.data.model.HAInstance
import com.mohankumargupta.homeassistanttv.domain.repository.HADiscoveryRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class MDNSDiscoveryRepository @Inject constructor(
    private val context: Context,
    private val io: CoroutineDispatcher = Dispatchers.IO,
    private val serviceType: String = SERVICE_TYPE_HOME_ASSISTANT
) : HADiscoveryRepository {

    companion object {
        // trailing dot is expected by NsdManager
        const val SERVICE_TYPE_HOME_ASSISTANT = "_home-assistant._tcp."
    }

    private val appContext = context.applicationContext
    private val nsdManager: NsdManager = appContext.getSystemService(Context.NSD_SERVICE) as NsdManager
    //private val wifiManager = appContext.getSystemService(Context.WIFI_SERVICE)


    override fun discoverEndpoints(): Flow<List<HAInstance>> {
        return callbackFlow {
            val discovered = LinkedHashMap<String, HAInstance>()
            val nameToKey = HashMap<String, String>()

            val requests = kotlinx.coroutines.channels.Channel<NsdServiceInfo>(capacity = kotlinx.coroutines.channels.Channel.UNLIMITED)

            // Single worker processes resolves sequentially
            val worker = launch(Dispatchers.IO) {
                for (info in requests) {
                    val resolved = resolve(info) ?: continue
                    //val ip = resolved.host.hostAddress?.substringBefore('%') ?: resolved.host.hostAddress
                    val ip = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        // Android 12+: non-deprecated path
                        getIpV31(resolved)
                    } else {
                        // Android 11: deprecated but required
                        @Suppress("DEPRECATION")
                        resolved.host?.hostAddress?.substringBefore('%')
                    } ?: continue
                    val ep = HAInstance(ip, resolved.port)
                    val key = "$ip:${resolved.port}"
                    discovered[key] = ep
                    nameToKey[resolved.serviceName] = key
                    trySend(discovered.values.toList())
                }
            }

            val listener = object : NsdManager.DiscoveryListener {
                override fun onServiceFound(info: NsdServiceInfo) {
                    if (!info.serviceType.contains("_home-assistant._tcp", true)) return
                    requests.trySend(info)
                }
                override fun onServiceLost(info: NsdServiceInfo) {
                    nameToKey.remove(info.serviceName)?.let { k ->
                        if (discovered.remove(k) != null) trySend(discovered.values.toList())
                    }
                }
                override fun onStartDiscoveryFailed(t: String, e: Int) { close(IllegalStateException("NSD start failed: $e")) }
                override fun onStopDiscoveryFailed(t: String, e: Int) {}
                override fun onDiscoveryStarted(t: String) {}
                override fun onDiscoveryStopped(t: String) {}
            }

            nsdManager.discoverServices("_home-assistant._tcp.", NsdManager.PROTOCOL_DNS_SD, listener)
            trySend(emptyList())

            awaitClose {
                runCatching { nsdManager.stopServiceDiscovery(listener) }
                requests.close()
                worker.cancel()
            }
        }

    }

    //@RequiresApi(Build.VERSION_CODES.S)
    private fun getIpV31(resolved: NsdServiceInfo): String? {
        @Suppress("DEPRECATION")
        return resolved.host?.hostAddress?.substringBefore('%')

        /*
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            resolved.inetAddresses.firstOrNull()?.hostAddress?.substringBefore('%')
        } else {
            @Suppress("DEPRECATION")
            resolved.host?.hostAddress?.substringBefore('%')
        }
         */
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private suspend fun resolve(info: NsdServiceInfo): NsdServiceInfo? =
        suspendCancellableCoroutine { cont ->
            val listener = object : NsdManager.ResolveListener {
                override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                    cont.resume(null) {}
                }
                override fun onServiceResolved(resolvedInfo: NsdServiceInfo) {
                    cont.resume(resolvedInfo) {}
                }
            }
            runCatching {
                @Suppress("DEPRECATION")
                nsdManager.resolveService(info, listener)
            }
                .onFailure { cont.resume(null) {} }
        }
}

