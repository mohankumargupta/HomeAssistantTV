package com.mohankumargupta.homeassistanttv.domain.usecase

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantDiscoveryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeAssistantUseCase @Inject constructor(
    private val homeAssistantDiscoveryRepository: HomeAssistantDiscoveryRepository
) {
    operator fun invoke(): Flow<List<HomeAssistant>> {
        return homeAssistantDiscoveryRepository.discoverHomeAssistants()
    }
}

