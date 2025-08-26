package com.mohankumargupta.homeassistanttv.presentation.screens.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.tv.material3.Surface
import com.mohankumargupta.homeassistanttv.presentation.components.TopBar
import com.mohankumargupta.homeassistanttv.presentation.screens.categories.Category
import com.mohankumargupta.homeassistanttv.presentation.screens.categories.JetstreamCategories
import com.mohankumargupta.homeassistanttv.presentation.screens.onboarding.OnboardingViewModel
import com.mohankumargupta.homeassistanttv.presentation.theme.HomeAssistantTVTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingViewModel = hiltViewModel()
) {

    val topEntries = listOf("Home", "Areas", "QuickDeck")
    var selectedIndex by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getAreas()
        viewModel.getLabels()
    }

//    BackHandler(enabled = true) {
//
//    }

    Home(
        modifier
            .padding(horizontal = 32.dp)
            .padding(top = 16.dp),
        selectedIndex,
        topEntries,
        onTabSelected = { index ->
            selectedIndex = index
        }
    )


}

@Composable
fun Home(
    modifier: Modifier = Modifier,
    selectedIndex: Int,
    topEntries: List<String>,
    onTabSelected: (index: Int) -> Unit
) {
    Column(modifier = modifier) {
        TopBar(
            modifier,
            entries = topEntries,
            selectedTabIndex = selectedIndex,
            onTabSelection = onTabSelected,
        )
        if (selectedIndex == 1) {
            JetstreamCategories(
                movieCategories = listOf(
                    Category(id = "1", name = "one"),
                    Category(id = "2", name = "two"),
                    Category(id = "3", name = "three")
                ),
                //modifier = TODO(),
                //gridColumns = TODO(),
                onCategoryClick = {}
            )
        }
    }
}

//@Composable
//fun HomeContents(modifier: Modifier = Modifier) {
//    Text("Home Screen")
//}

@Preview(name = "home", device = Devices.TV_720p)
@Composable
fun HomePreview() {
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            Home(
                selectedIndex = 0,
                topEntries = listOf("Home", "Areas"),
                onTabSelected = {}
            )
        }
    }
}

@Preview(name = "areas", device = Devices.TV_720p)
@Composable
fun HomeCategoriesPreview() {
    HomeAssistantTVTheme(isInDarkTheme = true) {
        Surface {
            Home(
                selectedIndex = 1,
                topEntries = listOf("Home", "Areas"),
                onTabSelected = {}
            )
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