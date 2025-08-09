package com.mohankumargupta.homeassistanttv.domain.repository

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import kotlinx.coroutines.flow.Flow

class MDNSDiscovery(): HomeAssistantDiscoveryRepository {
    override fun discoverHomeAssistants(): Flow<List<HomeAssistant>> {
        TODO("Not yet implemented")
    }

}