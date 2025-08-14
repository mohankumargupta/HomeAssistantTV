package com.mohankumargupta.homeassistanttv.data.model

data class UserPreferences(
    val homeAssistantIp: String,
    val homeAssistantPort: Int,
    val accessToken: String
)
