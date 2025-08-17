package com.mohankumargupta.homeassistanttv.domain.usecase

import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import javax.inject.Inject

class ListLabelsUseCase @Inject constructor(
    private val homeAssistantRepository: HomeAssistantRepository
) {
    operator fun invoke() {
        homeAssistantRepository.getLabels()
    }
}
