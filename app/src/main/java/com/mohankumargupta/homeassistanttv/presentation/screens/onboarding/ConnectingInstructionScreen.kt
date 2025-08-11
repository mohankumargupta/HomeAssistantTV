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
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Button
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

@Composable
fun ConnectingInstructionScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel(),
    onNextScreen: () -> Unit = {},
) {
    ConnectionInstruction(modifier) {
        viewModel.onClickConnecting()
        onNextScreen()
    }
}

@Composable
fun ConnectionInstruction(
    modifier: Modifier = Modifier,
    onSelected: () -> Unit = {},
) {
    val blurb = """
        http://bit.ly/3Hw9lhI
    """.trimIndent()

    val heading = """
        Connecting to Home Assistant
    """.trimIndent()


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
                Text(heading, style = MaterialTheme.typography.displayLarge)
                Text(blurb, style = MaterialTheme.typography.bodyLarge)
                Button(
                    onClick = onSelected,
                    modifier = modifier.focusRequester(focusRequester)
                ) {
                    Text("Connect")
                }
            }
        }

    }
}