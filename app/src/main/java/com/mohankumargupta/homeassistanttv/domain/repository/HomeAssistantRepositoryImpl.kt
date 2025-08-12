package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.data.model.Endpoint
import com.mohankumargupta.homeassistanttv.data.repository.MDNSRepository
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeAssistantRepositoryImpl @Inject constructor(
    private val mdnsRepository: MDNSRepository
) : HomeAssistantRepository {
    val service = "_home-assistant._tcp."
    override fun discoverHomeAssistants(): Flow<List<HomeAssistant>> =
        mdnsRepository.discoverEndpoints(service).map { endpoints ->
            endpoints.map { endpoint ->
                val port = endpoint.port
                HomeAssistant(ip = endpoint.ip, port = port)
            }
        }

    override suspend fun retrieveTokenAndConnectHomeAssistant(homeAssistant: HomeAssistant) {
        val accessToken = mdnsRepository
            .getAccessToken(homeAssistant.toEndpoint())
            .catch { exception ->
                val j = 26
            }
            .collect { accessToken ->

            val i = 25
        }
    }

}

private fun HomeAssistant.toEndpoint(): Endpoint {
    return Endpoint(ip = ip, port = port)
}


/*
override fun retrieveTokenAndConnectHomeAssistant(homeAssistant: HomeAssistant): Flow<ConnectionState> = channelFlow {
        trySend(ConnectionState.Connecting)

        // 1) Get the access token (you already have this via Node-RED on :1880)
        val token = mdnsRepository
            .getAccessToken(homeAssistant.toEndpoint())
            .catch { e -> throw e }
            .first()

        // 2) Connect WebSocket to Home Assistant
        val url = "ws://${homeAssistant.ip}:${homeAssistant.port}/api/websocket"

        webSocketDataSource.connect(url).collect { event ->
            when (event) {
                is com.mohankumargupta.homeassistanttv.data.remote.WebSocketEvent.Open -> {
                    // Connected at TCP/WebSocket level; wait for auth_required from HA
                }

                is com.mohankumargupta.homeassistanttv.data.remote.WebSocketEvent.TextMessage -> {
                    val raw = event.text
                    val json = runCatching { JSONObject(raw) }.getOrNull()
                    val type = json?.optString("type", null)

                    when (type) {
                        "auth_required" -> {
                            trySend(ConnectionState.AuthRequired)
                            val auth = JSONObject()
                                .put("type", "auth")
                                .put("access_token", token)
                            webSocketDataSource.send(auth.toString())
                        }
                        "auth_ok" -> {
                            trySend(ConnectionState.Authenticated)
                            // You can now send commands like subscribe_events, get_states, etc.
                            // Example:
                            // val cmd = JSONObject().put("id", 1).put("type", "get_states")
                            // webSocketDataSource.send(cmd.toString())
                        }
                        "auth_invalid" -> {
                            val msg = json?.optString("message", "Authentication failed")
                            trySend(ConnectionState.Error(IllegalStateException(msg)))
                            webSocketDataSource.close(4001, "auth_invalid")
                        }
                        else -> {
                            // Any other HA message (events, results, etc.)
                            trySend(ConnectionState.Message(type = type, raw = raw))
                        }
                    }
                }

                is com.mohankumargupta.homeassistanttv.data.remote.WebSocketEvent.Closing -> {
                    trySend(ConnectionState.Closed(event.code, event.reason))
                }

                is com.mohankumargupta.homeassistanttv.data.remote.WebSocketEvent.Closed -> {
                    trySend(ConnectionState.Closed(event.code, event.reason))
                }

                is com.mohankumargupta.homeassistanttv.data.remote.WebSocketEvent.Failure -> {
                    trySend(ConnectionState.Error(event.throwable))
                }
            }
        }
    }
}

 */