package com.mohankumargupta.homeassistanttv.presentation.screens.onboarding

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.usecase.ConnectHomeAssistanceUseCase
import com.mohankumargupta.homeassistanttv.domain.usecase.HomeAssistantUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    homeAssistantUseCase: HomeAssistantUseCase,
    private val connectHomeAssistantUseCase: ConnectHomeAssistanceUseCase
) : ViewModel() {
    val homeAssistants = homeAssistantUseCase().stateIn(
        viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    var selectedHomeAssistant: HomeAssistant? by mutableStateOf(null)
        private set

    fun onClickHomeAssistant(homeAssistant: HomeAssistant) {
        selectedHomeAssistant = homeAssistant
    }

    fun onClickConnecting() {
        viewModelScope.launch {
            selectedHomeAssistant?.let { homeAssistant ->
                connectHomeAssistantUseCase(homeAssistant)
            }
        }

    }
}
