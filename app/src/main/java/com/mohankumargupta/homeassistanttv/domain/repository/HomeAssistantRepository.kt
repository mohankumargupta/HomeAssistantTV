package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.domain.model.Area
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.model.WebSocketConnectionState
import kotlinx.coroutines.flow.Flow

interface HomeAssistantRepository {
    fun discoverHomeAssistants(): Flow<List<HomeAssistant>>
    fun retrieveTokenAndConnectHomeAssistant(homeAssistant: HomeAssistant): Flow<WebSocketConnectionState>

    fun getAreas()

    fun getLabels()
}
