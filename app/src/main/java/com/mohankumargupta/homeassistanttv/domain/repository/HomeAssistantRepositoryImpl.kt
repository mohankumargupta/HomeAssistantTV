package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.data.model.Auth
import com.mohankumargupta.homeassistanttv.data.model.AuthInvalid
import com.mohankumargupta.homeassistanttv.data.model.AuthOk
import com.mohankumargupta.homeassistanttv.data.model.AuthRequired
import com.mohankumargupta.homeassistanttv.data.model.Endpoint
import com.mohankumargupta.homeassistanttv.data.model.HAIncoming
import com.mohankumargupta.homeassistanttv.data.model.HAOutgoing
import com.mohankumargupta.homeassistanttv.data.model.ListAreas
import com.mohankumargupta.homeassistanttv.data.model.haJson
import com.mohankumargupta.homeassistanttv.data.remote.WebSocketDataSource
import com.mohankumargupta.homeassistanttv.data.remote.WebSocketEvent
import com.mohankumargupta.homeassistanttv.data.repository.MDNSRepository
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.model.WebSocketConnectionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.util.concurrent.atomic.AtomicInteger
import javax.inject.Inject

class HomeAssistantRepositoryImpl @Inject constructor(
    private val mdnsRepository: MDNSRepository,
    private val webSocketDataSource: WebSocketDataSource
) : HomeAssistantRepository {

    private val messageId = AtomicInteger(1)
    private val service = "_home-assistant._tcp."
    override fun discoverHomeAssistants(): Flow<List<HomeAssistant>> =
        mdnsRepository.discoverEndpoints(service).map { endpoints ->
            endpoints.map { endpoint ->
                val port = endpoint.port
                HomeAssistant(ip = endpoint.ip, port = port)
            }
        }

    override fun retrieveTokenAndConnectHomeAssistant(homeAssistant: HomeAssistant): Flow<WebSocketConnectionState> =
        channelFlow {
            trySend(WebSocketConnectionState.Connecting)

            val token = mdnsRepository
                .getAccessToken(homeAssistant.toEndpoint())
                .catch { e -> throw e }
                .first()

            val url = "ws://${homeAssistant.ip}:${homeAssistant.port}/api/websocket"

            webSocketDataSource.connect(url).collect { event ->
                when (event) {
                    is WebSocketEvent.Open -> {
                        // Connected at TCP/WebSocket level; wait for auth_required from HA
                    }

                    is WebSocketEvent.TextMessage -> {
                        val raw = event.text
                        when (val incoming = haJson.decodeFromString<HAIncoming>(raw)) {
                            is AuthRequired -> {
                                trySend(WebSocketConnectionState.AuthRequired)
                                val authMessage = Auth(accessToken = token)
                                val authJson = haJson.encodeToString(HAOutgoing.serializer(), authMessage)
                                webSocketDataSource.send(authJson)
                            }
                            is AuthOk -> {
                                trySend(WebSocketConnectionState.Authenticated)
                            }
                            is AuthInvalid -> {
                                val msg = incoming.message ?: "Authentication failed"
                                trySend(WebSocketConnectionState.Error(IllegalStateException(msg)))
                                webSocketDataSource.close(4001, "auth_invalid")
                            }

                            else -> {

                                // Any other HA message (events, results, etc.)
                                trySend(
                                    WebSocketConnectionState.Message(
                                        type = incoming::class.simpleName,
                                        raw = raw
                                    )
                                )
                            }
                        }
                    }

                    is WebSocketEvent.Closing -> {
                        trySend(WebSocketConnectionState.Closed(event.code, event.reason))
                    }

                    is WebSocketEvent.Closed -> {
                        trySend(WebSocketConnectionState.Closed(event.code, event.reason))
                    }

                    is WebSocketEvent.Failure -> {
                        trySend(WebSocketConnectionState.Error(event.throwable))
                    }
                }
            }
        }

    override fun getAreas() {
        val newMessageId = messageId.getAndIncrement()
        val message = ListAreas(newMessageId)
        val messageJson = haJson.encodeToString(HAOutgoing.serializer(), message)
        webSocketDataSource.send(messageJson)
    }
}

private fun HomeAssistant.toEndpoint(): Endpoint {
    return Endpoint(ip = ip, port = port)
}