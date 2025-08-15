package com.mohankumargupta.homeassistanttv.presentation.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.mohankumargupta.homeassistanttv.presentation.screens.home.components.TopBar
import com.mohankumargupta.homeassistanttv.presentation.screens.onboarding.OnboardingViewModel
import com.mohankumargupta.homeassistanttv.presentation.theme.HomeAssistantTVTheme



@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.getAreas()
    }

//    BackHandler(enabled = true) {
//
//    }

    Home(modifier)
}

@Composable
fun Home(
    modifier: Modifier = Modifier
) {
    val topEntries = listOf("Home", "Areas", "Scenes", "Scrips", "QuickDeck")
    var selectedIndex by remember { mutableIntStateOf(0) }
    TopBar(modifier, topEntries, selectedIndex, onSelected = { index ->
        selectedIndex = index
    })
}

@Composable
fun HomeContents(modifier: Modifier = Modifier) {
    Text("Home Screeen")
}

@Preview(device = Devices.TV_720p)
@Composable
fun HomePreview() {
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            Home()
        }
    }
}

/*
private fun BackPressHandledArea(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) =
    Box(
        modifier = Modifier
            .onPreviewKeyEvent {
                if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
                    onBackPressed()
                    true
                } else {
                    false
                }
            }
            .then(modifier),
        content = content
    )


 */