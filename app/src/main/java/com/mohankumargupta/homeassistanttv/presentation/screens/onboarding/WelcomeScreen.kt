package com.mohankumargupta.homeassistanttv.presentation.screens.onboarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import com.mohankumargupta.homeassistanttv.presentation.theme.HomeAssistantTVTheme

@Composable
fun WelcomeScreen(
    modifier: Modifier = Modifier,
    onNextScreen: () -> Unit = {}
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val focusRequester = remember { FocusRequester() }

        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }

        Box(
            modifier = modifier
                .fillMaxSize()
                .padding(48.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(
                    space = 96.dp,
                    alignment = Alignment.CenterVertically,
                ),
            ) {
                Text("Welcome to Home Assistant TV", style = MaterialTheme.typography.displayLarge)
                Button(
                    onClick = onNextScreen,
                    modifier = modifier.focusRequester(focusRequester)
                ) {
                    Text("Get Started")
                }
            }
        }

    }
}

@Preview(device = TV_720p)
@Composable
fun WelcomePreview() {
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            WelcomeScreen()
        }
    }
}