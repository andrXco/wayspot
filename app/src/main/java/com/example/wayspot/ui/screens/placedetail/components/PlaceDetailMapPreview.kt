package com.example.wayspot.ui.screens.placedetail.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.toPlaceInfo
import com.example.wayspot.ui.components.map.PlacesMapSurface
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

/** Ubicación del detalle con el mapa real cuando el lugar tiene coordenadas. */
@Composable
fun PlaceDetailMapPreview(
    place: PlaceInfo,
    onMapClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    val mapDescription = stringResource(
        R.string.place_map_preview_content_description,
        place.title,
        place.location
    )

    Column(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = stringResource(R.string.place_map_section_label),
                    color = colorScheme.onSurfaceVariant,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = stringResource(R.string.place_map_explore_title),
                    color = colorScheme.onSurface,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            TextButton(
                onClick = onMapClick,
                contentPadding = PaddingValues(horizontal = 4.dp, vertical = 2.dp)
            ) {
                Text(
                    text = stringResource(R.string.place_map_view_action),
                    color = colorScheme.primary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(154.dp)
                .clip(RoundedCornerShape(18.dp))
                .semantics { contentDescription = mapDescription }
        ) {
            if (place.coordinates?.isValid == true) {
                PlacesMapSurface(
                    places = listOf(place),
                    selectedPlaceId = place.id,
                    onPlaceSelected = {},
                    isInteractive = false,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                PlaceDetailMapUnavailable(
                    modifier = Modifier.fillMaxSize()
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(10.dp),
                shape = RoundedCornerShape(10.dp),
                color = colorScheme.surface.copy(alpha = 0.96f),
                shadowElevation = 2.dp,
                border = BorderStroke(
                    width = 1.dp,
                    color = colorScheme.outline.copy(alpha = 0.16f)
                )
            ) {
                Column(modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)) {
                    Text(
                        text = place.title,
                        color = colorScheme.onSurface,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = place.location,
                        color = colorScheme.onSurfaceVariant,
                        fontSize = 9.sp
                    )
                }
            }
        }
    }
}

/** Estado sin coordenadas; evita presentar un marcador en una ubicación inventada. */
@Composable
private fun PlaceDetailMapUnavailable(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Surface(
        modifier = modifier,
        color = colorScheme.surfaceVariant
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.LocationOff,
                contentDescription = null,
                tint = colorScheme.primary,
                modifier = Modifier.size(26.dp)
            )
            Text(
                text = stringResource(R.string.place_map_coordinates_unavailable),
                color = colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun PlaceDetailMapPreviewComponentPreview() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val place = PreviewDataPopular.cerroMonserrate.toPlaceInfo(context::getString)

    WayspotTheme {
        PlaceDetailMapPreview(
            place = place,
            onMapClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
