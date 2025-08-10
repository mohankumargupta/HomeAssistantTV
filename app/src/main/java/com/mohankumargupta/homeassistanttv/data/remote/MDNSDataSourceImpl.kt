package com.mohankumargupta.homeassistanttv.data.remote

import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withTimeoutOrNull
import java.net.InetAddress
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


const val RESOLVE_TIMEOUT_MS = 5000L

class NsdException(message: String, val errorCode: Int) : Exception(message)

data class DiscoveredService(
    val name: String,
    val type: String,
    val port: Int,
    val host: String,
    val addresses: List<String>
)

sealed class DiscoveryEvent {
    data class Discovered(
        val service: DiscoveredService,
        val onResolve: suspend () -> DiscoveredService?
    ) : DiscoveryEvent()
    data class Removed(val service: DiscoveredService) : DiscoveryEvent()
}

class MDNSDataSourceImpl() : MDNSDataSource {
    override fun discoverServices(context: Context, service: String): Flow<DiscoveryEvent> =
        callbackFlow {
            val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
            val resolveChannel = Channel<ResolutionRequest>(Channel.BUFFERED)

            val resolutionJob = launch(Dispatchers.IO) {
                for (request in resolveChannel) {
                    val (serviceInfo, deferred) = request
                    try {
                        val result = withTimeoutOrNull(RESOLVE_TIMEOUT_MS) {
                            resolveServiceInternal(nsdManager, serviceInfo)
                        }
                        deferred.complete(result)
                    } catch (e: Exception) {
                        deferred.completeExceptionally(e)
                    }
                }
            }

            val listener = object : NsdManager.DiscoveryListener {
                override fun onDiscoveryStarted(serviceType: String) {  }
                override fun onDiscoveryStopped(serviceType: String) {  }
                override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                    trySend(
                        DiscoveryEvent.Discovered(
                            service = serviceInfo.toCommon(),
                            onResolve = {
                                val deferred = CompletableDeferred<DiscoveredService?>()
                                val request = ResolutionRequest(serviceInfo, deferred)
                                resolveChannel.send(request)
                                deferred.await()
                            }
                        )
                    )
                }

                override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                    trySend(DiscoveryEvent.Removed(service = serviceInfo.toCommon()))
                }

                override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                    val error = NsdException("Discovery start failed for $serviceType", errorCode)
                    close(error)
                }

                override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) { /* No-op */ }
            }

            try {
                nsdManager.discoverServices(service, NsdManager.PROTOCOL_DNS_SD, listener)
            } catch (e: Exception) {
                close(e)
            }

            awaitClose {
                try {
                    nsdManager.stopServiceDiscovery(listener)
                } catch (e: Exception) {  }

                resolveChannel.close()
                // Wait for the consumer to finish its final task, if any.
                //resolutionJob.join()
            }
        }

    private data class ResolutionRequest(
        val serviceInfo: NsdServiceInfo,
        val resultDeferred: CompletableDeferred<DiscoveredService?>
    )

    private suspend fun resolveServiceInternal(
        nsdManager: NsdManager,
        serviceInfo: NsdServiceInfo
    ): DiscoveredService = suspendCancellableCoroutine { continuation ->
        val listener = object : NsdManager.ResolveListener {
            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                val error = NsdException("Failed to resolve service: ${serviceInfo.serviceName}", errorCode)
                continuation.resumeWithException(error)
            }

            override fun onServiceResolved(resolvedInfo: NsdServiceInfo) {
                continuation.resume(resolvedInfo.toCommon())
            }
        }
        nsdManager.resolveService(serviceInfo, listener)
    }

    internal fun NsdServiceInfo.toCommon(): DiscoveredService {
        val hostAddresses: List<InetAddress> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            this.hostAddresses
        } else {
            @Suppress("DEPRECATION")
            this.host?.let { listOf(it) }.orEmpty()
        }

        return DiscoveredService(
            name = serviceName,
            type = serviceType,
            port = port,
            host = hostAddresses.firstOrNull()?.canonicalHostName ?: hostAddresses.firstOrNull()?.hostAddress.orEmpty(),
            addresses = hostAddresses.mapNotNull { it.hostAddress }
        )
    }
}
