package com.mohankumargupta.homeassistanttv.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.mohankumargupta.homeassistanttv.presentation.screens.onboarding.FindHomeAssistant
import com.mohankumargupta.homeassistanttv.presentation.screens.onboarding.WelcomeScreen

@Composable
fun AppNavigation() {
    val backStack = remember { mutableStateListOf<AppNavKey>(AppNavKey.Welcome) }
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        entryProvider = entryProvider {
            entry<AppNavKey.Welcome> {
                WelcomeScreen(onNextScreen = {
                  backStack.add(AppNavKey.Home)
                })
            }

            entry<AppNavKey.HomeAssistantSearch> {
                FindHomeAssistant()
            }

            entry<AppNavKey.Home> {

            }
        }
    )
}