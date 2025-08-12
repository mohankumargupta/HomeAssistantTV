package com.mohankumargupta.homeassistanttv.domain.usecase

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import javax.inject.Inject

class ConnectHomeAssistanceUseCase @Inject constructor(
    private val homeAssistantRepository: HomeAssistantRepository
) {
    suspend operator fun invoke(homeAssistant: HomeAssistant) {
        return homeAssistantRepository.retrieveTokenAndConnectHomeAssistant(homeAssistant)
    }
}
