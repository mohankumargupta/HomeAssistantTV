package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import kotlinx.coroutines.flow.Flow

interface HomeAssistantDiscoveryRepository {
    fun discoverHomeAssistants(): Flow<List<HomeAssistant>>
}