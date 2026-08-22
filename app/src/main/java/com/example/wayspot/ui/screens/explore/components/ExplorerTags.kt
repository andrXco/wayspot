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
import com.example.wayspot.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExplorerTags(
    selectedCategoryRes: Int,
    onCategoryClick: (Int) -> Unit,
    m: Modifier = Modifier
) {
    val categories = listOf(
        R.string.category_beaches,
        R.string.category_mountains,
        R.string.category_museums,
        R.string.category_parks,
        R.string.category_cities,
        R.string.category_art
    )

    LazyRow(
        modifier = m,
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            FilterChip(
                selected = selectedCategoryRes == category,
                onClick = { onCategoryClick(category) },
                label = {
                    Text(
                        text = stringResource(category),
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
                    selected = selectedCategoryRes == category,
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
            selectedCategoryRes = R.string.category_beaches,
            onCategoryClick = {}
        )
    }
}
