package com.example.wayspot.ui.screens.map.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/** Marcador compacto de WaySpot, resaltado cuando el lugar está seleccionado. */
@Composable
fun PlacesMapMarker(
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = modifier.size(if (isSelected) 44.dp else 38.dp),
        shape = CircleShape,
        color = if (isSelected) colorScheme.primary else colorScheme.surface,
        contentColor = if (isSelected) colorScheme.onPrimary else colorScheme.primary,
        border = BorderStroke(2.dp, colorScheme.surface),
        shadowElevation = 5.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = null,
                modifier = Modifier.size(if (isSelected) 25.dp else 22.dp)
            )
        }
    }
}
