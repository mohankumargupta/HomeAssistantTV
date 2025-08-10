package com.mohankumargupta.homeassistanttv.domain.usecase

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeAssistantUseCase @Inject constructor(
    private val homeAssistantRepository: HomeAssistantRepository
) {
    operator fun invoke(): Flow<List<HomeAssistant>> {
        return homeAssistantRepository.discoverHomeAssistants()
    }
}

