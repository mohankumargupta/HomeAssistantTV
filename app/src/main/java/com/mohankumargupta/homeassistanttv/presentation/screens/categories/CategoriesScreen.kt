package com.mohankumargupta.homeassistanttv.presentation.screens.categories

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
//import androidx.hilt.navigation.compose.hiltViewModel
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text
//import com.google.jetstream.data.entities.MovieCategoryList
//import com.google.jetstream.presentation.common.Loading

//import com.google.jetstream.presentation.common.MovieCard
//import com.google.jetstream.presentation.utils.GradientBg


import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Devices.TV_720p
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import com.mohankumargupta.homeassistanttv.presentation.components.HomeCard

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

@Immutable
data class Padding(
    val start: Dp,
    val top: Dp,
    val end: Dp,
    val bottom: Dp,
)

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



/*
@Composable
fun CategoriesScreen(
    gridColumns: Int = 4,
    onCategoryClick: (categoryId: String) -> Unit,
    onScroll: (isTopBarVisible: Boolean) -> Unit,
    //categoriesScreenViewModel: CategoriesScreenViewModel = hiltViewModel()
) {

    val uiState by categoriesScreenViewModel.uiState.collectAsStateWithLifecycle()

    when (val s = uiState) {
        CategoriesScreenUiState.Loading -> {
            Loading(modifier = Modifier.fillMaxSize())
        }

        is CategoriesScreenUiState.Ready -> {
            Catalog(
                gridColumns = gridColumns,
                movieCategories = s.categoryList,
                onCategoryClick = onCategoryClick,
                onScroll = onScroll,
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
 */

data class MovieCategory(
    val id: String,
    val name: String,
)

//fun MovieCategoriesResponseItem.toMovieCategory(): MovieCategory =
//    MovieCategory(id, name)

typealias MovieCategoryList = List<MovieCategory>

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
                HomeCard(
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = TV_720p)
@Composable
fun CatalogPreview() {
    // Sample data for the preview
    val sampleMovieCategories = listOf(
        MovieCategory("1", "Action"),
        MovieCategory("2", "Comedy"),
        MovieCategory("3", "Drama"),
        MovieCategory("4", "Horror"),
        MovieCategory("5", "Science Fiction"),
        MovieCategory("6", "Thriller"),
        MovieCategory("7", "Romance"),
        MovieCategory("8", "Documentary"),
        MovieCategory("9", "Animation"),
        MovieCategory("10", "Fantasy"),
        MovieCategory("11", "Mystery"),
        MovieCategory("12", "Adventure")
    )

    MaterialTheme {
        Catalog(
            movieCategories = sampleMovieCategories,
            onCategoryClick = { /* No-op for preview */ },
            onScroll = { /* No-op for preview */ }
        )
    }
}