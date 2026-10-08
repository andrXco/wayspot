package com.example.wayspot.ui.screens.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.Eco
import androidx.compose.material.icons.rounded.Luggage
import androidx.compose.material.icons.rounded.Restaurant
import androidx.compose.material.icons.rounded.Terrain
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.HomeCategory
import com.example.wayspot.data.model.HomeCategoryId
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

/** Selector horizontal de categorías para filtrar los planes destacados de inicio. */
@Composable
fun HomeCategoryChips(
    categories: List<HomeCategory>,
    selectedCategory: HomeCategoryId?,
    onCategoryClick: (HomeCategoryId) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyRow(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(8.dp)
    ) {
        items(
            items = categories,
            key = { category -> category.id.name }
        ) { category ->
            val selected = category.id == selectedCategory
            Surface(
                modifier = Modifier
                    .clickable { onCategoryClick(category.id) },
                shape = RoundedCornerShape(20.dp),
                color = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                },
                contentColor = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            ) {
                androidx.compose.foundation.layout.Row(
                    verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Icon(
                        imageVector = when (category.id) {
                            HomeCategoryId.NATURE -> Icons.Rounded.Eco
                            HomeCategoryId.GASTRONOMY -> Icons.Rounded.Restaurant
                            HomeCategoryId.GETAWAYS -> Icons.Rounded.Luggage
                            HomeCategoryId.CULTURE -> Icons.Rounded.AccountBalance
                            HomeCategoryId.ADVENTURE -> Icons.Rounded.Terrain
                        },
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    Text(
                        text = stringResource(category.labelRes),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
@WayspotMultiPreview
@Composable
private fun HomeCategoryChipsPreview() {
    WayspotTheme {
        Surface {
            HomeCategoryChips(
                categories = PreviewData.homeCategories,
                selectedCategory = HomeCategoryId.NATURE,
                onCategoryClick = {}
            )
        }
    }
}
