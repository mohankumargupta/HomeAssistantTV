package com.mohankumargupta.homeassistanttv.domain.usecase

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListAreasUseCase @Inject constructor(
    private val homeAssistantRepository: HomeAssistantRepository
) {
    operator fun invoke() {
        homeAssistantRepository.getAreas()
    }
}