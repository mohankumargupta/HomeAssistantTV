package com.mohankumargupta.homeassistanttv.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject

val haJson = Json {
    ignoreUnknownKeys = true
    classDiscriminator = "type" // Home Assistant uses "type"
    encodeDefaults = false
    isLenient = true
}

/* ---------------- Inbound messages (from HA) ---------------- */

@Serializable
sealed interface HAIncoming {
    val id: Int?
}

@Serializable
@SerialName("auth_required")
data class AuthRequired(
    @SerialName("ha_version")
    val haVersion: String? = null
) : HAIncoming { override val id: Int? = null }

@Serializable
@SerialName("auth_ok")
data class AuthOk(
    @SerialName("ha_version")
    val haVersion: String? = null
) : HAIncoming { override val id: Int? = null }

@Serializable
@SerialName("auth_invalid")
data class AuthInvalid(
    val message: String? = null
) : HAIncoming { override val id: Int? = null }

@Serializable
@SerialName("result")
data class ResultMsg(
    override val id: Int,
    val success: Boolean,
    val result: JsonElement? = null,
    val error: HAError? = null
) : HAIncoming

@Serializable
data class HAError(
    val code: String,
    val message: String
)

@Serializable
@SerialName("event")
data class EventMsg(
    override val id: Int,
    val event: HAEventPayload
) : HAIncoming

@Serializable
data class HAEventPayload(
    val eventType: String,
    val data: JsonObject? = null,
    val timeFired: String,
    val origin: String? = null,
    val context: HAContext? = null
)

@Serializable
data class HAContext(
    val id: String? = null,
    val parentID: String? = null,
    val userID: String? = null
)

@Serializable
@SerialName("pong")
data class Pong(
    override val id: Int
) : HAIncoming

/* ---------------- Outbound messages (to HA) ---------------- */

@Serializable
sealed interface HAOutgoing {
    val id: Int?
}

@Serializable
@SerialName("auth")
data class Auth(
    @SerialName("access_token")
    val accessToken: String
) : HAOutgoing { override val id: Int? = null }

@Serializable
@SerialName("config/area_registry/list")
data class ListAreas(
    override val id: Int
) : HAOutgoing

@Serializable
@SerialName("ping")
data class Ping(
    override val id: Int
) : HAOutgoing

@Serializable
@SerialName("subscribe_events")
data class SubscribeEvents(
    override val id: Int,
    val eventType: String? = null // null = all events
) : HAOutgoing

@Serializable
@SerialName("unsubscribe_events")
data class UnsubscribeEvents(
    override val id: Int,
    val subscription: Int // the id used when you subscribed
) : HAOutgoing

@Serializable
@SerialName("call_service")
data class CallService(
    override val id: Int,
    val domain: String,
    val service: String,
    @SerialName("service_data") val serviceData: JsonObject? = null,
    val target: JsonObject? = null
) : HAOutgoing
