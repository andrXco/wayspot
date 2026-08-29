package com.example.wayspot.ui.screens.savedplaces.components

import androidx.compose.foundation.background
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.SavedPlaceList

@Composable
fun SavedPlacesCategoryTabs(
    selectedList: SavedPlaceList,
    counts: Map<SavedPlaceList, Int>,
    onListSelected: (SavedPlaceList) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 72.dp)
                .selectableGroup(),
            verticalAlignment = Alignment.Bottom
        ) {
            SavedPlaceList.entries.forEach { list ->
                SavedPlacesCategoryTab(
                    list = list,
                    count = counts[list] ?: 0,
                    selected = selectedList == list,
                    onClick = { onListSelected(list) },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        HorizontalDivider(
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.18f)
        )
    }
}

@Composable
private fun SavedPlacesCategoryTab(
    list: SavedPlaceList,
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val contentColor = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }
    val icon: ImageVector
    val iconColor = when (list) {
        SavedPlaceList.WANT_TO_VISIT -> MaterialTheme.colorScheme.tertiary
        SavedPlaceList.FAVORITES -> MaterialTheme.colorScheme.error
        SavedPlaceList.VISITED -> MaterialTheme.colorScheme.primary
    }
    val labelRes: Int

    when (list) {
        SavedPlaceList.WANT_TO_VISIT -> {
            icon = Icons.Filled.Place
            labelRes = R.string.saved_places_list_want_to_visit
        }
        SavedPlaceList.FAVORITES -> {
            icon = Icons.Filled.Favorite
            labelRes = R.string.saved_places_list_favorites
        }
        SavedPlaceList.VISITED -> {
            icon = Icons.Filled.CheckCircle
            labelRes = R.string.saved_places_list_visited
        }
    }

    Column(
        modifier = modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.Tab
            )
            .padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(1.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = iconColor
        )
        Text(
            text = stringResource(labelRes),
            color = contentColor,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            maxLines = 1,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.saved_places_list_count, count),
            color = contentColor,
            style = MaterialTheme.typography.labelSmall
        )
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(
                    if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surface
                    }
                )
        )
    }
}
