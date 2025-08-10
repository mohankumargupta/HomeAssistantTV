package com.mohankumargupta.homeassistanttv.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val homeAssistantRepository: HomeAssistantRepository
) : ViewModel() {

    val homeAssistants = MutableStateFlow<List<HomeAssistant>>(emptyList<HomeAssistant>())

    fun discoverHomeAssistants() {
        viewModelScope.launch {
            homeAssistantRepository
                .discoverHomeAssistants()
                .collect { ha ->
                    homeAssistants.value = ha
                }
        }

    }

}