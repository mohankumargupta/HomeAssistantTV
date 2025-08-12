package com.mohankumargupta.homeassistanttv.data.remote

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import javax.inject.Inject

class HomeAssistantWebSocketDataSourceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : WebSocketDataSource {

    private var webSocket: WebSocket? = null

    override fun connect(url: String, headers: Map<String, String>): Flow<WebSocketEvent> = callbackFlow {
        val requestBuilder = Request.Builder().url(url)
        headers.forEach { (k, v) -> requestBuilder.addHeader(k, v) }
        val request = requestBuilder.build()

        val listener = object : WebSocketListener() {
            override fun onOpen(ws: WebSocket, response: Response) {
                webSocket = ws
                trySend(WebSocketEvent.Open(response))
            }

            override fun onMessage(ws: WebSocket, text: String) {
                trySend(WebSocketEvent.TextMessage(text))
            }

            override fun onClosing(ws: WebSocket, code: Int, reason: String) {
                trySend(WebSocketEvent.Closing(code, reason))
            }

            override fun onClosed(ws: WebSocket, code: Int, reason: String) {
                trySend(WebSocketEvent.Closed(code, reason))
            }

            override fun onFailure(ws: WebSocket, t: Throwable, response: Response?) {
                trySend(WebSocketEvent.Failure(t))
                close(t)
            }
        }

        val ws = okHttpClient.newWebSocket(request, listener)
        webSocket = ws

        awaitClose {
            try {
                webSocket?.cancel()
            } finally {
                webSocket = null
            }
        }
    }

    override fun send(text: String): Boolean = webSocket?.send(text) ?: false

    override fun close(code: Int, reason: String?) {
        webSocket?.close(code, reason ?: "")
        webSocket = null
    }
}