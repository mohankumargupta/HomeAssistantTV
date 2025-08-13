package com.mohankumargupta.homeassistanttv.domain.usecase

import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.model.WebSocketConnectionState
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ConnectHomeAssistanceUseCase @Inject constructor(
    private val homeAssistantRepository: HomeAssistantRepository
) {
    operator fun invoke(homeAssistant: HomeAssistant): Flow<WebSocketConnectionState> {
        return homeAssistantRepository.retrieveTokenAndConnectHomeAssistant(homeAssistant)
    }
}
