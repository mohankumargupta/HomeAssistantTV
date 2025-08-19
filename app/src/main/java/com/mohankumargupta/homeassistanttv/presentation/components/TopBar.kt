package com.mohankumargupta.homeassistanttv.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Icon
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.mohankumargupta.homeassistanttv.R
import com.mohankumargupta.homeassistanttv.presentation.theme.HomeAssistantTVTheme

@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    entries: List<String>,
    selectedTabIndex: Int,
    onTabSelection: (tab: Int) -> Unit
) {
    val focusRequesters = List(size = entries.size + 1) { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Box(modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                //.background(MaterialTheme.colorScheme.surface)
                .focusRestorer(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.home_assistant_icon),
                contentDescription = "avatar icon",
                modifier = modifier.size(32.dp),
                tint = Color.Unspecified,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                var isTabRowFocused by remember { mutableStateOf(false) }
                Spacer(modifier = Modifier.width(20.dp))
                TabRow(
                    modifier = Modifier
                        .onFocusChanged {
                            isTabRowFocused = it.isFocused || it.hasFocus
                        },
                    selectedTabIndex = selectedTabIndex,
                    indicator = { tabPositions, _ ->
                        if (selectedTabIndex >= 0) {
                            TopBarIndicator(
                                currentTabPosition = tabPositions[selectedTabIndex],
                                anyTabFocused = isTabRowFocused,
                                shape = JetStreamCardShape
                            )
                        }
                    },
                    separator = { Spacer(modifier = Modifier) }
                ) {
                    entries.forEachIndexed { index, entry ->
                        key(index) {
                         Tab(
                             selected = index == selectedTabIndex,
                             onFocus = {},
                             modifier = Modifier
                                 .height(32.dp)
                                 .focusRequester(focusRequesters[index + 1]),
                             onClick = {},
                         ) {
                             Text(
                                 modifier = Modifier
                                     .fillMaxSize()
                                     .wrapContentSize()
                                     .padding(horizontal = 16.dp),
                                 text = entry,
                                 style = MaterialTheme.typography.titleSmall.copy(
                                     color = LocalContentColor.current
                                 )
                             )
                         }   
                        }
                    }
                }
            }
        }
    }

}

@Preview(device = TV_720p)
@Composable
fun TopBarPreview(modifier: Modifier = Modifier) {
    val menu = listOf("Home", "Areas", "QuickDeck")
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            TopBar(
                modifier = modifier,
                entries = menu,
                selectedTabIndex = 0,
                onTabSelection = {}
            )
        }
    }
}
