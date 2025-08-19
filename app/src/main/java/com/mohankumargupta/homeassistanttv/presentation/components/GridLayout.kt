package com.mohankumargupta.homeassistanttv.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.BringIntoViewSpec
import androidx.compose.foundation.gestures.LocalBringIntoViewSpec
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Card
import androidx.tv.material3.CardDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Text

// Data class to represent a single category
data class Category(val id: String, val name: String)

// Sample data for the categories grid
val sampleCategories = List(12) { index ->
    Category(id = "$index", name = "Genre ${index + 1}")
}

/**
 * Implements a TV-style BringIntoViewSpec that positions the focused item
 * based on a parent and child fraction, mimicking the old pivotOffsets behavior.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PositionFocusedItemInLazyLayout(
    parentFraction: Float = 0.3f,
    childFraction: Float = 0f,
    content: @Composable () -> Unit,
) {
    val bringIntoViewSpec = remember(parentFraction, childFraction) {
        object : BringIntoViewSpec {
            override fun calculateScrollDistance(
                offset: Float,
                size: Float,
                containerSize: Float
            ): Float {
                val childSmallerThanParent = size <= containerSize
                val initialTargetForLeadingEdge = parentFraction * containerSize - (childFraction * size)
                val spaceAvailableToShowItem = containerSize - initialTargetForLeadingEdge
                val targetForLeadingEdge =
                    if (childSmallerThanParent && spaceAvailableToShowItem < size) {
                        containerSize - size
                    } else {
                        initialTargetForLeadingEdge
                    }
                return offset - targetForLeadingEdge
            }
        }
    }
    CompositionLocalProvider(
        LocalBringIntoViewSpec provides bringIntoViewSpec,
        content = content,
    )
}


/**
 * The main screen that displays the top navigation and the category grid.
 * This version uses the standard LazyVerticalGrid and a custom BringIntoViewSpec.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CategoriesScreen(categories: List<Category>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 58.dp, top = 36.dp, end = 58.dp)
    ) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Medium)
        )

        // Apply the custom focus positioning to the grid
        PositionFocusedItemInLazyLayout {
            LazyVerticalGrid(
                columns = GridCells.Fixed(4), // Updated from TvGridCells
                modifier = Modifier.padding(top = 24.dp),
                contentPadding = PaddingValues(vertical = 12.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(categories) { category ->
                    CategoryCard(category = category)
                }
            }
        }
    }
}

/**
 * A composable for displaying a single category card.
 * It shows a border when it is focused.
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun CategoryCard(category: Category, modifier: Modifier = Modifier) {
    var isFocused by remember { mutableStateOf(false) }

    Card(
        onClick = { /* Handle category click */ },
        modifier = modifier
            .aspectRatio(16f / 9f)
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            },
        border = CardDefaults.border(
            focusedBorder = Border(
                border = BorderStroke(width = 3.dp, color = MaterialTheme.colorScheme.onSurface),
                //shape = CardDefaults.shape()
            )
        ),
        shape = CardDefaults.shape(shape = MaterialTheme.shapes.medium),
        colors = CardDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleMedium,
                color = if (isFocused) MaterialTheme.colorScheme.onSurface else Color.White,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

/**
 * A preview for the CategoriesScreen to see the layout in Android Studio.
 */
@Preview(device = "id:tv_1080p")
@Composable
fun CategoriesScreenPreview() {
    MaterialTheme {
        CategoriesScreen(categories = sampleCategories)
    }
}