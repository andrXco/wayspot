package com.example.wayspot.ui.screens.explore.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.data.model.ExploreCategory
import com.example.wayspot.data.model.ExploreCategoryRules

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorerTags(
    categories: List<ExploreCategory>,
    selectedCategory: ExploreCategory,
    onCategoryClick: (ExploreCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
            key = { category -> category.labelRes }
        ) { category ->
            FilterChip(
                selected = selectedCategory == category,
                onClick = { onCategoryClick(category) },
                label = {
                    Text(
                        text = stringResource(category.labelRes),
                        fontSize = 14.sp
                    )
                },
                shape = RoundedCornerShape(20.dp),
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.surface,
                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selectedCategory == category,
                    borderColor = MaterialTheme.colorScheme.outlineVariant,
                    selectedBorderColor = MaterialTheme.colorScheme.primary.copy(alpha = 0f),
                    borderWidth = 1.dp
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExplorerTagsPreview() {
    WayspotTheme {
        ExplorerTags(
            categories = ExploreCategoryRules.categories,
            selectedCategory = ExploreCategoryRules.initialCategory,
            onCategoryClick = {}
        )
    }
}
