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

/**
 * Defines the events emitted during service discovery.
 */
sealed class DiscoveryEvent {
    data class Discovered(
        val service: DiscoveredService,
        /**
         * A suspend function to resolve the full details of this service.
         * Returns the resolved service, or null if resolution fails or times out.
         *
         * Calls to `onResolve` are non-blocking and are safely queued for processing.
         */
        val onResolve: suspend () -> DiscoveredService?
    ) : DiscoveryEvent()

    data class Removed(val service: DiscoveredService) : DiscoveryEvent()
}

class MDNSDataSourceImpl() : MDNSDataSource {
    override fun discoverServices(context: Context, service: String): Flow<DiscoveryEvent> =
        callbackFlow {
            val nsdManager = context.getSystemService(Context.NSD_SERVICE) as NsdManager
            val resolveChannel = Channel<ResolutionRequest>(Channel.BUFFERED)

            // Launch a single, long-running consumer coroutine to process the resolution queue.
            // This coroutine will be automatically cancelled when the flow is closed.
            val resolutionJob = launch(Dispatchers.IO) {
                for (request in resolveChannel) {
                    val (serviceInfo, deferred) = request
                    try {
                        // Process one resolution request at a time.
                        val result = withTimeoutOrNull(RESOLVE_TIMEOUT_MS) {
                            //with context dispatchers.main
                            resolveServiceInternal(nsdManager, serviceInfo)
                        }
                        deferred.complete(result)
                    } catch (e: Exception) {
                        deferred.completeExceptionally(e)
                    }
                }
            }

            val listener = object : NsdManager.DiscoveryListener {
                override fun onDiscoveryStarted(serviceType: String) { /* No-op */ }
                override fun onDiscoveryStopped(serviceType: String) { /* No-op */ }

                override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                    trySend(
                        DiscoveryEvent.Discovered(
                            service = serviceInfo.toCommon(),
                            onResolve = {
                                val deferred = CompletableDeferred<DiscoveredService?>()
                                val request = ResolutionRequest(serviceInfo, deferred)
                                resolveChannel.send(request) // Send request to the queue
                                deferred.await() // Wait for the consumer to process it
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
                // Cleanup sequence is important
                try {
                    nsdManager.stopServiceDiscovery(listener)
                } catch (e: Exception) { /* Swallow */ }

                // Close the channel. This will terminate the for-loop in the consumer coroutine.
                resolveChannel.close()
                // Wait for the consumer to finish its final task, if any.
                //resolutionJob.join()
            }

        }


    /**
     * A private data class to bundle a resolution request with a way to return its result.
     */
    private data class ResolutionRequest(
        val serviceInfo: NsdServiceInfo,
        val resultDeferred: CompletableDeferred<DiscoveredService?>
    )


    /**
     * The core suspendable coroutine for resolving a service.
     */
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

    /**
     * Converts an Android-specific [NsdServiceInfo] to a common [DiscoveredService].
     */
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
