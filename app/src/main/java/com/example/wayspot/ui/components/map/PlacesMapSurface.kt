package com.example.wayspot.ui.components.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlacesMapDefaults
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.MarkerComposable
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberUpdatedMarkerState
import kotlinx.coroutines.launch

/** Dibuja el mapa real y conserva una superficie estable para el render de Compose Preview. */
@Composable
fun PlacesMapSurface(
    places: List<PlaceInfo>,
    selectedPlaceId: String?,
    onPlaceSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true,
    showRecenterButton: Boolean = true
) {
    val colorScheme = MaterialTheme.colorScheme
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val cameraPositionState = rememberCameraPositionState()
    val coroutineScope = rememberCoroutineScope()
    val isInspectionMode = LocalInspectionMode.current
    val selectedPlace = places.firstOrNull { place -> place.id == selectedPlaceId }
    val validPlaces = places.filter { place -> place.coordinates?.isValid == true }
    var isMapLoaded by remember { mutableStateOf(false) }

    if (!isInspectionMode) {
        LaunchedEffect(validPlaces, selectedPlace?.id, isMapLoaded) {
            if (isMapLoaded) {
                cameraPositionState.focusPlaces(
                    places = validPlaces,
                    selectedPlace = selectedPlace
                )
            }
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (LocalInspectionMode.current) {
            PlacesMapPreviewSurface(modifier = Modifier.fillMaxSize())
        } else {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                contentDescription = stringResource(R.string.places_map_title),
                onMapLoaded = { isMapLoaded = true },
                properties = MapProperties(
                    mapStyleOptions = if (isDarkTheme) {
                        MapStyleOptions.loadRawResourceStyle(context, R.raw.map_style_dark)
                    } else {
                        null
                    }
                ),
                uiSettings = MapUiSettings(
                    scrollGesturesEnabled = isInteractive,
                    zoomGesturesEnabled = isInteractive,
                    tiltGesturesEnabled = isInteractive,
                    rotationGesturesEnabled = isInteractive,
                    compassEnabled = false,
                    mapToolbarEnabled = false,
                    myLocationButtonEnabled = false,
                    zoomControlsEnabled = false
                )
            ) {
                validPlaces.forEach { place ->
                    val coordinates = place.coordinates ?: return@forEach
                    val isSelected = place.id == selectedPlaceId

                    MarkerComposable(
                        keys = arrayOf<Any>(place.id, isSelected),
                        state = rememberUpdatedMarkerState(
                            position = coordinates.toLatLng()
                        ),
                        contentDescription = stringResource(
                            R.string.places_map_marker_content_description,
                            place.title
                        ),
                        title = place.title,
                        snippet = place.location,
                        zIndex = if (isSelected) 1f else 0f,
                        onClick = {
                            onPlaceSelected(place.id)
                            true
                        }
                    ) {
                        PlacesMapMarker(isSelected = isSelected)
                    }
                }
            }
        }

        if (showRecenterButton) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .shadow(elevation = 4.dp, shape = CircleShape),
                shape = CircleShape,
                color = colorScheme.surface,
                contentColor = colorScheme.primary
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            cameraPositionState.focusPlaces(
                                places = validPlaces,
                                selectedPlace = selectedPlace
                            )
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.CenterFocusStrong,
                        contentDescription = stringResource(
                            R.string.places_map_show_all_content_description
                        )
                    )
                }
            }
        }

        if (isInteractive) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(12.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(24.dp)),
                shape = RoundedCornerShape(24.dp),
                color = colorScheme.surface,
                contentColor = colorScheme.primary
            ) {
                Column {
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.zoomIn())
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(R.string.places_map_zoom_in_content_description)
                        )
                    }
                    IconButton(
                        onClick = {
                            coroutineScope.launch {
                                cameraPositionState.animate(CameraUpdateFactory.zoomOut())
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = stringResource(R.string.places_map_zoom_out_content_description)
                        )
                    }
                }
            }
        }
    }
}

private suspend fun CameraPositionState.focusPlaces(
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?
) {
    val selectedCoordinates = selectedPlace?.coordinates?.takeIf { coordinates ->
        coordinates.isValid
    }

    if (selectedCoordinates != null) {
        animate(
            CameraUpdateFactory.newLatLngZoom(
                selectedCoordinates.toLatLng(),
                PlacesMapDefaults.selectedPlaceZoom
            )
        )
        return
    }

    val coordinates = places.mapNotNull { place ->
        place.coordinates?.takeIf(PlaceCoordinates::isValid)
    }.distinct()

    when (coordinates.size) {
        0 -> animate(
            CameraUpdateFactory.newLatLngZoom(
                PlacesMapDefaults.initialCenter.toLatLng(),
                PlacesMapDefaults.initialZoom
            )
        )

        1 -> animate(
            CameraUpdateFactory.newLatLngZoom(
                coordinates.first().toLatLng(),
                PlacesMapDefaults.selectedPlaceZoom
            )
        )

        else -> {
            val boundsBuilder = LatLngBounds.Builder()
            coordinates.forEach { coordinates ->
                boundsBuilder.include(coordinates.toLatLng())
            }
            animate(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 120))
        }
    }
}

private fun PlaceCoordinates.toLatLng(): LatLng = LatLng(latitude, longitude)

@Composable
private fun PlacesMapPreviewSurface(
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = modifier.background(colorScheme.surfaceVariant)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val roads = listOf(
                Path().apply {
                    moveTo(-width * 0.12f, height * 0.22f)
                    cubicTo(width * 0.18f, height * 0.08f, width * 0.32f, height * 0.42f, width * 1.1f, height * 0.18f)
                },
                Path().apply {
                    moveTo(-width * 0.1f, height * 0.76f)
                    cubicTo(width * 0.22f, height * 0.5f, width * 0.48f, height * 0.94f, width * 1.12f, height * 0.62f)
                },
                Path().apply {
                    moveTo(width * 0.16f, -height * 0.08f)
                    cubicTo(width * 0.34f, height * 0.22f, width * 0.52f, height * 0.56f, width * 0.78f, height * 1.08f)
                }
            )

            roads.forEach { road ->
                drawPath(
                    path = road,
                    color = colorScheme.surface,
                    style = Stroke(width = 18.dp.toPx(), cap = StrokeCap.Round)
                )
                drawPath(
                    path = road,
                    color = colorScheme.outlineVariant,
                    style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                )
            }
        }

        PlacesMapMarker(
            isSelected = true,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}
