package com.example.wayspot.ui.screens.explore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.wayspot.R
import com.example.wayspot.ui.theme.WayspotTheme

/** Chips de categorías obtenidas del catálogo real (no de una lista fija). */
@Composable
fun ExplorerTags(
    categories: List<String>,
    selectedCategory: String?,
    onCategoryClick: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item(key = "all_places") {
            ExploreFilterChip(
                label = stringResource(R.string.category_all),
                selected = selectedCategory == null,
                onClick = { onCategoryClick(null) }
            )
        }
        items(categories, key = { it }) { category ->
            ExploreFilterChip(
                label = category,
                selected = selectedCategory == category,
                onClick = { onCategoryClick(category) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExploreFilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = selected,
        onClick = onClick,
        label = { Text(text = label, fontSize = 14.sp) },
        shape = RoundedCornerShape(20.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
            containerColor = MaterialTheme.colorScheme.surface,
            labelColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        border = FilterChipDefaults.filterChipBorder(
            enabled = true,
            selected = selected,
            borderColor = MaterialTheme.colorScheme.outlineVariant,
            selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0f),
            borderWidth = 1.dp
        )
    )
}

@Preview(showBackground = true)
@Composable
fun ExplorerTagsPreview() {
    WayspotTheme {
        ExplorerTags(
            categories = listOf("Playa", "Montaña", "Museo"),
            selectedCategory = "Montaña",
            onCategoryClick = {}
        )
    }
}
