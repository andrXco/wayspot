package com.example.wayspot.ui.components.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CenterFocusStrong
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlacesMapDefaults
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.maps.MapLibreMap

/** Dibuja el mapa real y conserva una superficie estable para el render de Compose Preview. */
@Composable
fun PlacesMapSurface(
    places: List<PlaceInfo>,
    selectedPlaceId: String?,
    onPlaceSelected: (String) -> Unit,
    mapRenderState: MapRenderState,
    onMapRenderStatusChanged: (MapRenderStatus) -> Unit,
    onRetryMapRender: () -> Unit,
    modifier: Modifier = Modifier,
    isInteractive: Boolean = true,
    showRecenterButton: Boolean = true,
    isNearbySearchActive: Boolean = false,
    onSearchNearby: (PlaceCoordinates) -> Unit = {},
    onClearNearbySearch: () -> Unit = {},
    currentLocation: PlaceCoordinates? = null,
    locationFocusRequestId: Long = 0L,
    isRequestingCurrentLocation: Boolean = false,
    onRequestCurrentLocation: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    val styleUrl = if (isSystemInDarkTheme()) DARK_STYLE_URL else LIBERTY_STYLE_URL
    val isInspectionMode = LocalInspectionMode.current
    val mapDescription = stringResource(R.string.places_map_title)
    val selectedPlace = places.firstOrNull { place -> place.id == selectedPlaceId }
    val validPlaces = places.filter { place -> place.coordinates?.isValid == true }
    var mapLibreMap by remember(styleUrl, mapRenderState.attempt) {
        mutableStateOf<MapLibreMap?>(null)
    }
    val showControls = isInspectionMode ||
        (mapRenderState.status == MapRenderStatus.Ready && mapLibreMap != null)

    Box(
        modifier = modifier
            .fillMaxSize()
            .semantics {
                contentDescription = mapDescription
            }
    ) {
        if (isInspectionMode) {
            PlacesMapPreviewSurface(modifier = Modifier.fillMaxSize())
        } else {
            key(styleUrl, mapRenderState.attempt) {
                MapLibreMapRenderer(
                    places = validPlaces,
                    selectedPlaceId = selectedPlaceId,
                    currentLocation = currentLocation,
                    locationFocusRequestId = locationFocusRequestId,
                    styleUrl = styleUrl,
                    isInteractive = isInteractive,
                    markerColor = colorScheme.primary,
                    mapSurfaceColor = colorScheme.surface,
                    onPlaceSelected = onPlaceSelected,
                    onMapReady = { map -> mapLibreMap = map },
                    onMapRenderStatusChanged = onMapRenderStatusChanged,
                    modifier = Modifier.fillMaxSize()
                )
            }

            when (mapRenderState.status) {
                MapRenderStatus.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                    strokeWidth = 3.dp
                )
                MapRenderStatus.Failed -> PlacesMapRenderError(
                    onRetry = onRetryMapRender,
                    modifier = Modifier
                        .align(if (isInteractive) Alignment.Center else Alignment.TopCenter)
                        .padding(8.dp)
                )
                MapRenderStatus.Ready -> Unit
            }
        }

        if (showControls && showRecenterButton) {
            Column(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier.shadow(elevation = 4.dp, shape = CircleShape),
                    shape = CircleShape,
                    color = colorScheme.surface,
                    contentColor = colorScheme.primary
                ) {
                    IconButton(
                        onClick = {
                            if (isNearbySearchActive) {
                                onClearNearbySearch()
                            } else {
                                mapLibreMap?.let { map ->
                                    focusMapOnPlaces(map, validPlaces, selectedPlace)
                                }
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CenterFocusStrong,
                            contentDescription = stringResource(
                                if (isNearbySearchActive) {
                                    R.string.places_map_clear_nearby_content_description
                                } else {
                                    R.string.places_map_show_all_content_description
                                }
                            )
                        )
                    }
                }

                if (isInteractive) {
                    Surface(
                        modifier = Modifier.shadow(elevation = 4.dp, shape = CircleShape),
                        shape = CircleShape,
                        color = colorScheme.surface,
                        contentColor = colorScheme.primary
                    ) {
                        IconButton(
                            enabled = !isRequestingCurrentLocation,
                            onClick = onRequestCurrentLocation
                        ) {
                            if (isRequestingCurrentLocation) {
                                CircularProgressIndicator(
                                    modifier = Modifier.padding(8.dp),
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.MyLocation,
                                    contentDescription = stringResource(
                                        R.string.places_map_current_location_content_description
                                    )
                                )
                            }
                        }
                    }

                    Surface(
                        modifier = Modifier.shadow(elevation = 4.dp, shape = CircleShape),
                        shape = CircleShape,
                        color = colorScheme.surface,
                        contentColor = colorScheme.primary
                    ) {
                        IconButton(
                            onClick = {
                                val center = mapLibreMap?.cameraPosition?.target ?: return@IconButton
                                onSearchNearby(
                                    PlaceCoordinates(
                                        latitude = center.latitude,
                                        longitude = center.longitude
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = stringResource(
                                    R.string.places_map_search_nearby_content_description
                                )
                            )
                        }
                    }
                }
            }
        }

        if (showControls && isInteractive) {
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
                            mapLibreMap?.animateCamera(CameraUpdateFactory.zoomIn())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = stringResource(
                                R.string.places_map_zoom_in_content_description
                            )
                        )
                    }
                    IconButton(
                        onClick = {
                            mapLibreMap?.animateCamera(CameraUpdateFactory.zoomOut())
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = stringResource(
                                R.string.places_map_zoom_out_content_description
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun PlacesMapRenderError(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        shadowElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).padding(top = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.places_map_render_error),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            TextButton(onClick = onRetry) {
                Text(text = stringResource(R.string.places_map_retry))
            }
        }
    }
}

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
