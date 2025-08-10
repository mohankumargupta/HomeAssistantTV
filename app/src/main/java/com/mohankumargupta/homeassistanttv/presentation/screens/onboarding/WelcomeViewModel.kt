package com.mohankumargupta.homeassistanttv.presentation.screens.onboarding

import androidx.lifecycle.ViewModel
import com.mohankumargupta.homeassistanttv.domain.repository.HomeAssistantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val homeAssistantRepository: HomeAssistantRepository
) : ViewModel() {

}