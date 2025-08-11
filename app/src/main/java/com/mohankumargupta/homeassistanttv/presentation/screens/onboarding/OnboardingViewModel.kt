package com.mohankumargupta.homeassistanttv.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohankumargupta.homeassistanttv.domain.usecase.HomeAssistantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val homeAssistantUseCase: HomeAssistantUseCase
) : ViewModel() {
    val homeAssistants = homeAssistantUseCase().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )
}