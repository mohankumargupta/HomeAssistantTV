package com.mohankumargupta.homeassistanttv.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.mohankumargupta.homeassistanttv.presentation.screens.onboarding.OnboardingViewModel

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    Home(modifier)
}

@Composable
fun Home(
    modifier: Modifier = Modifier
) {

}