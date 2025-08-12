package com.mohankumargupta.homeassistanttv.data.remote

import kotlinx.coroutines.flow.Flow
import okhttp3.Response

sealed class WebSocketEvent {
    data class Open(val response: Response) : WebSocketEvent()
    data class TextMessage(val text: String) : WebSocketEvent()
    data class Closing(val code: Int, val reason: String?) : WebSocketEvent()
    data class Closed(val code: Int, val reason: String?) : WebSocketEvent()
    data class Failure(val throwable: Throwable) : WebSocketEvent()
}

interface WebSocketDataSource {
    fun connect(url: String, headers: Map<String, String> = emptyMap()): Flow<WebSocketEvent>
    fun send(text: String): Boolean
    fun close(code: Int = 1000, reason: String? = null)
}
