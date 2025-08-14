package com.mohankumargupta.homeassistanttv.domain.model

sealed class WebSocketConnectionState {
    object Connecting : WebSocketConnectionState()
    object AuthRequired : WebSocketConnectionState()
    object Authenticated : WebSocketConnectionState()

    data class ListOfAreas(val areas: List<Area>) : WebSocketConnectionState()
    data class Message(val type: String?, val raw: String) : WebSocketConnectionState()
    data class Closed(val code: Int, val reason: String?) : WebSocketConnectionState()
    data class Error(val throwable: Throwable) : WebSocketConnectionState()
}