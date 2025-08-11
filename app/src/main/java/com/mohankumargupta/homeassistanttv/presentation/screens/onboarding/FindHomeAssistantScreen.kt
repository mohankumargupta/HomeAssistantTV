package com.mohankumargupta.homeassistanttv.presentation.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.ListItem
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.mohankumargupta.homeassistanttv.domain.model.HomeAssistant
import com.mohankumargupta.homeassistanttv.presentation.theme.HomeAssistantTVTheme

@Composable
fun FindHomeAssistantScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNextScreen: () -> Unit = {},
) {
    val assistants by viewModel.homeAssistants.collectAsStateWithLifecycle()
    FindHomeAssistants(modifier, assistants ) { homeAssistant ->
        viewModel.onClickHomeAssistant(homeAssistant)
        onNextScreen()
    }

}

@Composable
fun FindHomeAssistants(
    modifier: Modifier = Modifier,
    assistants: List<HomeAssistant>,
    onClick: (HomeAssistant) -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 58.dp, vertical = 36.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Text(
                text = "Looking for Home Assistant",
                style = MaterialTheme.typography.displaySmall
            )

            // Conditionally display content based on the discovery state
            if (assistants.isEmpty()) {
                // Show a loading/searching indicator
                Text(
                    text = "Searching on your local network...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )
                //CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp))
            } else {
                // Once instances are found, display them in a TV-optimized list
                HomeAssistantList(
                    assistants = assistants,
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun HomeAssistantList(
    assistants: List<HomeAssistant>,
    onClick: (HomeAssistant) -> Unit,
    modifier: Modifier = Modifier
) {
    val focusRequester = remember { FocusRequester() }

    LazyColumn(
        modifier = modifier
            .fillMaxWidth(0.6f) // Don't take up the full screen width
            .focusRequester(focusRequester),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(assistants) { ha ->
            HomeAssistantListItem(
                homeAssistant = ha,
                onClick = { onClick(ha) }
            )
        }
    }

    // Request focus for the list as soon as it's composed.
    // This makes the D-pad work immediately without the user needing to click first.
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeAssistantListItem(
    homeAssistant: HomeAssistant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // `ListItem` is a focusable, TV-optimized component that shows a halo
    // and scales on focus, following Material Design for TV guidelines.
    ListItem(
        modifier = modifier,
        selected = false, // You can use this to show a "selected" state if needed
        onClick = onClick,
        leadingContent = {
            Icon(
                imageVector = Icons.Default.Home,
                contentDescription = null,
                modifier = Modifier.padding(end = 16.dp)
            )
        },
        headlineContent = { Text(text = homeAssistant.ip) },
        supportingContent = {
            Text(
                text = "Port: ${homeAssistant.port}", style = MaterialTheme.typography.bodyLarge
                //color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
            )
        }
    )
}

// --- Preview for easy development ---

@Preview(device = Devices.TV_720p)
@Composable
fun WelcomeScreenSearchingPreview() {
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            FindHomeAssistants(assistants = emptyList()) {

            }
        }
    }
}

@Preview(device = Devices.TV_720p)
@Composable
fun WelcomeScreenFoundPreview() {
    val sampleData = listOf(
        HomeAssistant("192.168.1.100", 8123),
        HomeAssistant("192.168.20.98", 8123)
    )
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            // We can't inject a ViewModel in a preview, so we'll preview the sub-composable
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                HomeAssistantList(assistants = sampleData, onClick = {})
            }
        }
    }
}