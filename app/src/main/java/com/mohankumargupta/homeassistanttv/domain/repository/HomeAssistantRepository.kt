package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import kotlinx.coroutines.flow.Flow

interface HomeAssistantRepository {
    fun discoverHomeAssistants(): Flow<List<HomeAssistant>>
    fun retrieveTokenAndConnectHomeAssistant(homeAssistant: HomeAssistant): Flow<WebSocketConnectionState>
}
