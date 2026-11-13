package com.example.wayspot.ui.screens.map.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R

/** Resumen sin estado del lugar, su calificación y la distancia del filtro cercano. */
@Composable
fun PlacesMapPlaceSummary(
    rating: Double,
    reviewCount: Int,
    distanceMeters: Double?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = stringResource(
                    R.string.places_map_rating_summary,
                    rating,
                    reviewCount
                ),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelMedium
            )
        }

        distanceMeters?.let { distance ->
            Text(
                text = if (distance < 1000) {
                    stringResource(
                        R.string.places_map_distance_meters,
                        distance.toInt()
                    )
                } else {
                    stringResource(
                        R.string.places_map_distance_kilometers,
                        distance / 1000
                    )
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}
