package com.mohankumargupta.homeassistanttv.presentation.screens.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text

// A palette of muted colors for the subtle "glow" effect.
private val glowColors = listOf(
    Color(0xFFE57373), // Action (Red)
    Color(0xFF64B5F6), // Documentaries (Blue)
    Color(0xFFDCE775), // Black Voices (Lime)
    Color(0xFFFFB74D), // Comedy (Orange)
    Color(0xFF4DD0E1), // Nature (Cyan)
    Color(0xFF81C784), // Fantasy (Green)
    Color(0xFFE57373), // Foreign (Red)
    Color(0xFF7986CB), // Horror (Indigo)
    Color(0xFFF06292), // LGBTQ (Pink)
    Color(0xFF90A4AE), // War & Military (Blue Grey)
    Color(0xFFFFB74D), // Musicals (Orange)
)
// A slightly lighter background color to reduce contrast and soften the glow
private val cardBackgroundColor = Color(0xFF3A3A3A)

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

data class Category(
    val id: String,
    val name: String,
)

typealias CategoryList = List<Category>

@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
public fun JetstreamCategories(
    movieCategories: List<Category>,
    modifier: Modifier = Modifier,
    gridColumns: Int = 4,
    onCategoryClick: (String) -> Unit,
) {
    val childPadding = rememberChildPadding()

    LazyVerticalGrid(
        modifier = modifier
            .padding(horizontal = childPadding.start)
            .padding(top = childPadding.top),
        columns = GridCells.Fixed(gridColumns),
    ) {
        itemsIndexed(movieCategories) { index, movieCategory ->
            var isFocused by remember { mutableStateOf(false) }

            val cardBrush = remember(index) {
                val glowColor = glowColors[index % glowColors.size]
                // A single, very low alpha value for a consistent and subtle tint
                val alpha = 0.12f
                Brush.radialGradient(
                    colors = listOf(
                        glowColor.copy(alpha = alpha),
                        cardBackgroundColor
                    ),
                    radius = 400f // A large radius for a very soft, diffuse glow
                )
            }

            Card(
                onClick = { onCategoryClick(movieCategory.id) },
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
                    },
                shape = CardDefaults.shape(shape = MaterialTheme.shapes.medium),
                colors = CardDefaults.colors(containerColor = Color.Transparent)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(MaterialTheme.shapes.medium)
                        .background(cardBrush) // The entire card background is one subtle gradient
                ) {
                    Text(
                        text = movieCategory.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(device = "id:tv_1080p")
@Composable
fun CatalogAsImagePreview() {
    val sampleMovieCategories = listOf(
        Category("1", "Action"),
        Category("2", "Documentaries"),
        Category("3", "Black Voices"),
        Category("4", "Comedy"),
        Category("5", "Nature"),
        Category("6", "Fantasy"),
        Category("7", "Foreign"),
        Category("8", "Horror"),
        Category("9", "LGBTQ"),
        Category("10", "War & Military"),
        Category("11", "Musicals"),
    )

    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            colors = SurfaceDefaults.colors(containerColor = Color(0xFF1C1C1C))
        ) {
            JetstreamCategories(
                movieCategories = sampleMovieCategories,
                onCategoryClick = { }
            )
        }
    }
}