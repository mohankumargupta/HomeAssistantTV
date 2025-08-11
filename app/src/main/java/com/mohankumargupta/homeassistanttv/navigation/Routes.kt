package com.mohankumargupta.homeassistanttv.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

@Serializable
sealed interface AppNavKey: NavKey {
    @Serializable
    object Welcome: AppNavKey
    @Serializable
    object HomeAssistantSearch: AppNavKey
    @Serializable
    object Home: AppNavKey
}