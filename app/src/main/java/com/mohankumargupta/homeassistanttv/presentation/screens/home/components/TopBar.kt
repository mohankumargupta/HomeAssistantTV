package com.mohankumargupta.homeassistanttv.presentation.screens.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.mohankumargupta.homeassistanttv.presentation.theme.HomeAssistantTVTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    topEntries: List<String>,
    selectedIndex: Int = 0,
    onSelected: (index: Int) -> Unit = {}
) {
    Box(modifier) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .background(MaterialTheme.colorScheme.background)
            //.background(MaterialTheme.colorScheme.surface)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            topEntries.forEachIndexed { index, entry ->
                TabRow(
                    selectedTabIndex = selectedIndex,
                    modifier = modifier
                        .padding(horizontal = 16.dp)
                        .background(MaterialTheme.colorScheme.background),

                    ) {
                    Tab(
                        selected = selectedIndex == index,
                        onFocus = {
                            onSelected(index)
                        },
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.background),
                        onClick = {},
                    ) {
                        Text(entry)
                    }
                }
            }
        }
    }
}

@Preview(device = Devices.TV_720p)
@Composable
fun TopBarPreview() {
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            TopBar(topEntries = listOf("Home", "Areas", "Scenes", "Scrips", "QuickDeck") )
        }
    }
}
