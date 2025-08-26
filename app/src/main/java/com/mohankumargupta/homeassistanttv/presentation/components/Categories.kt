package com.mohankumargupta.homeassistanttv.presentation.components

import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Surface

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
//import androidx.tv.material3.Border
//import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
//import androidx.tv.material3.StandardCardContainer
//import androidx.tv.material3.Surface
import androidx.tv.material3.Text
//import com.google.jetstream.presentation.theme.JetStreamBorderWidth
//import com.google.jetstream.presentation.theme.JetStreamCardShape
//import com.google.jetstream.presentation.utils.Amber300
//import com.google.jetstream.presentation.utils.Blue300
//import com.google.jetstream.presentation.utils.BlueGray300
//import com.google.jetstream.presentation.utils.Brown300
//import com.google.jetstream.presentation.utils.Coral
//import com.google.jetstream.presentation.utils.Cyan300
//import com.google.jetstream.presentation.utils.DeepOrange300
//import com.google.jetstream.presentation.utils.DeepPurple300
//import com.google.jetstream.presentation.utils.Gray300
//import com.google.jetstream.presentation.utils.Green300
//import com.google.jetstream.presentation.utils.Indigo300
//import com.google.jetstream.presentation.utils.LightBlue300
//import com.google.jetstream.presentation.utils.LightGreen300
//import com.google.jetstream.presentation.utils.LightYellow
//import com.google.jetstream.presentation.utils.Lime300
//import com.google.jetstream.presentation.utils.Orange300
//import com.google.jetstream.presentation.utils.Pink300
//import com.google.jetstream.presentation.utils.Purple300
//import com.google.jetstream.presentation.utils.Red300
//import com.google.jetstream.presentation.utils.Teal300
//import com.google.jetstream.presentation.utils.Yellow300
//import com.google.jetstream.presentation.utils.Padding

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp

data class MovieCategory(
    val id: String,
    val name: String,
)

@Immutable
data class Padding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp,
)

val Coral = Color(0xFFF3A397)
val LightYellow = Color(0xFFF8EE94)

val Red300 = Color(0xFFE57373)
val Pink300 = Color(0xFFF06292)
val Purple300 = Color(0xFFBA68C8)
val DeepPurple300 = Color(0xFF9575CD)
val Indigo300 = Color(0xFF7986CB)
val Blue300 = Color(0xFF64B5F6)
val LightBlue300 = Color(0xFF4FC3F7)
val Cyan300 = Color(0xFF4DD0E1)
val Teal300 = Color(0xFF4DB6AC)
val Green300 = Color(0xFF81C784)
val LightGreen300 = Color(0xFFAED581)
val Lime300 = Color(0xFFDCE775)
val Yellow300 = Color(0xFFFFF176)
val Amber300 = Color(0xFFFFD54F)
val Orange300 = Color(0xFFFFB74D)
val DeepOrange300 = Color(0xFFFF8A65)
val Brown300 = Color(0xFFA1887F)
val Gray300 = Color(0xFFE0E0E0)
val BlueGray300 = Color(0xFF90A4AE)

val ourColors = listOf(
    Coral,
    LightYellow,
    Red300,
    Pink300,
    Purple300,
    DeepPurple300,
    Indigo300,
    Blue300,
    LightBlue300,
    Cyan300,
    Teal300,
    Green300,
    LightGreen300,
    Lime300,
    Yellow300,
    Amber300,
    Orange300,
    DeepOrange300,
    Brown300,
    Gray300,
    BlueGray300,
)

typealias MovieCategoryList = List<MovieCategory>

val ParentPadding = PaddingValues(vertical = 16.dp, horizontal = 58.dp)

@Composable
fun rememberChildPadding(direction: LayoutDirection = LocalLayoutDirection.current): Padding {
    return remember {
        Padding(
            start = ParentPadding.calculateStartPadding(direction) + 8.dp,
            top = ParentPadding.calculateTopPadding(),
            end = ParentPadding.calculateEndPadding(direction) + 8.dp,
            bottom = ParentPadding.calculateBottomPadding()
        )
    }
}

val pairs = listOf(
    Coral to LightYellow,
    Red300 to BlueGray300,
    Pink300 to Gray300,
    Purple300 to Brown300,
    DeepPurple300 to DeepOrange300,
    Indigo300 to Orange300,
    Blue300 to Amber300,
    LightBlue300 to Yellow300,
    Cyan300 to Lime300,
    Teal300 to LightGreen300,
    Green300 to Coral,
)

@Composable
fun GradientBg() {
    Box(
        modifier = Modifier
            .background(Brush.radialGradient(pairs.random().toList()))
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun MovieCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    image: @Composable BoxScope.() -> Unit,
) {
    StandardCardContainer(
        modifier = modifier,
        title = title,
        imageCard = {
            Surface(
                onClick = onClick,
                shape = ClickableSurfaceDefaults.shape(JetStreamCardShape),
                border = ClickableSurfaceDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = JetStreamBorderWidth,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                        shape = JetStreamCardShape
                    )
                ),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
                content = image
            )
        },
    )
}


@Composable
fun StandaloneCategoriesScreen(
    gridColumns: Int = 4,
    onCategoryClick: (categoryId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
) {
    val categories = remember {
        listOf(
            MovieCategory("1", "Action"),
            MovieCategory("2", "Adventure"),
            MovieCategory("3", "Comedy"),
            MovieCategory("4", "Drama"),
            MovieCategory("5", "Fantasy"),
            MovieCategory("6", "Horror"),
            MovieCategory("7", "Mystery"),
            MovieCategory("8", "Romance"),
            MovieCategory("9", "Sci-Fi"),
            MovieCategory("10", "Thriller"),
        )
    }

    Catalog(
        gridColumns = gridColumns,
        movieCategories = categories,
        onCategoryClick = onCategoryClick,
        onScroll = onScroll,
        modifier = Modifier.fillMaxSize()
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun Catalog(
    movieCategories: MovieCategoryList,
    modifier: Modifier = Modifier,
    gridColumns: Int = 4,
    onCategoryClick: (categoryId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
) {
    val childPadding = rememberChildPadding()
    val lazyGridState = rememberLazyGridState()
    val shouldShowTopBar by remember {
        derivedStateOf {
            lazyGridState.firstVisibleItemIndex == 0 &&
                    lazyGridState.firstVisibleItemScrollOffset < 100
        }
    }
    LaunchedEffect(shouldShowTopBar) {
        onScroll(shouldShowTopBar)
    }

    AnimatedContent(
        targetState = movieCategories,
        modifier = Modifier
            .padding(horizontal = childPadding.start)
            .padding(top = childPadding.top),
        label = "",
    ) { it ->
        LazyVerticalGrid(
            state = lazyGridState,
            modifier = modifier,
            columns = GridCells.Fixed(gridColumns),
        ) {
            itemsIndexed(it) { index, movieCategory ->
                var isFocused by remember { mutableStateOf(false) }
                MovieCard(
                    onClick = {
                        onCategoryClick(movieCategory.id)
                    },
                    modifier = Modifier
                        .padding(8.dp)
                        .aspectRatio(16 / 9f)
                        .onFocusChanged {
                            isFocused = it.isFocused || it.hasFocus
                        }
                        .focusProperties {
                            if (index % gridColumns == 0) {
                                left = FocusRequester.Cancel
                            }
                        }
                ) {
                    val itemAlpha by animateFloatAsState(
                        targetValue = if (isFocused) .6f else 0.2f,
                        label = ""
                    )
                    val textColor = if (isFocused) Color.White else Color.White

                    Box(contentAlignment = Alignment.Center) {
                        Box(modifier = Modifier.alpha(itemAlpha)) {
                            GradientBg()
                        }
                        Text(
                            text = movieCategory.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                color = textColor,
                            )
                        )
                    }
                }
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun StandaloneCategoriesScreenPreview() {
    StandaloneCategoriesScreen(
        onCategoryClick = {},
        onScroll = {}
    )
}
