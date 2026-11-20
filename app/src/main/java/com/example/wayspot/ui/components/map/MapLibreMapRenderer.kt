package com.example.wayspot.ui.components.map

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.view.Gravity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlacesMapDefaults
import com.google.gson.JsonObject
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull
import org.maplibre.android.MapLibre
import org.maplibre.android.camera.CameraUpdateFactory
import org.maplibre.android.geometry.LatLng
import org.maplibre.android.geometry.LatLngBounds
import org.maplibre.android.camera.CameraPosition
import org.maplibre.android.maps.MapLibreMap
import org.maplibre.android.maps.MapLibreMapOptions
import org.maplibre.android.maps.MapView
import org.maplibre.android.maps.Style
import org.maplibre.android.style.expressions.Expression
import org.maplibre.android.style.layers.CircleLayer
import org.maplibre.android.style.layers.Property
import org.maplibre.android.style.layers.PropertyFactory.circleColor
import org.maplibre.android.style.layers.PropertyFactory.circleRadius
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeColor
import org.maplibre.android.style.layers.PropertyFactory.circleStrokeWidth
import org.maplibre.android.style.layers.PropertyFactory.iconAllowOverlap
import org.maplibre.android.style.layers.PropertyFactory.iconAnchor
import org.maplibre.android.style.layers.PropertyFactory.iconIgnorePlacement
import org.maplibre.android.style.layers.PropertyFactory.iconImage
import org.maplibre.android.style.layers.SymbolLayer
import org.maplibre.android.style.sources.GeoJsonSource
import org.maplibre.geojson.Feature
import org.maplibre.geojson.FeatureCollection
import org.maplibre.geojson.Point

internal const val LIBERTY_STYLE_URL = "https://tiles.openfreemap.org/styles/liberty"
internal const val DARK_STYLE_URL = "https://tiles.openfreemap.org/styles/dark"

private const val PLACE_SOURCE_ID = "wayspot-places"
private const val PLACE_LAYER_ID = "wayspot-place-markers"
private const val LOCATION_SOURCE_ID = "wayspot-current-location"
private const val LOCATION_LAYER_ID = "wayspot-current-location-marker"
private const val DEFAULT_MARKER_IMAGE_ID = "wayspot-marker"
private const val SELECTED_MARKER_IMAGE_ID = "wayspot-marker-selected"
private const val PLACE_ID_PROPERTY = "place_id"
private const val MARKER_IMAGE_PROPERTY = "marker_image"
private const val MAP_LOAD_TIMEOUT_MILLIS = 15_000L

/** Aloja MapLibre Native en Compose y sincroniza datos, cámara y ciclo de vida. */
@Composable
internal fun MapLibreMapRenderer(
    places: List<PlaceInfo>,
    selectedPlaceId: String?,
    currentLocation: PlaceCoordinates?,
    locationFocusRequestId: Long,
    styleUrl: String,
    isInteractive: Boolean,
    markerColor: androidx.compose.ui.graphics.Color,
    mapSurfaceColor: androidx.compose.ui.graphics.Color,
    onPlaceSelected: (String) -> Unit,
    onMapReady: (MapLibreMap) -> Unit,
    onMapRenderStatusChanged: (MapRenderStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val latestOnPlaceSelected by rememberUpdatedState(onPlaceSelected)
    val latestOnMapReady by rememberUpdatedState(onMapReady)
    val latestOnMapRenderStatusChanged by rememberUpdatedState(onMapRenderStatusChanged)
    val mapView = remember(context) {
        MapLibre.getInstance(context.applicationContext)
        val options = MapLibreMapOptions.createFromAttributes(context, null)
        MapView(context, options).also { view -> view.onCreate(null) }
    }
    var nativeMap by remember(mapView) { mutableStateOf<MapLibreMap?>(null) }
    var loadedMap by remember(mapView) { mutableStateOf<MapLibreMap?>(null) }
    var lastHandledLocationRequestId by remember(mapView) { mutableLongStateOf(0L) }

    DisposableEffect(mapView, lifecycleOwner) {
        val observer = MapViewLifecycleObserver(mapView)
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
            observer.destroy()
        }
    }

    AndroidView(
        modifier = modifier,
        factory = { _ ->
            mapView.getMapAsync { map ->
                map.cameraPosition = initialCameraPosition()
                map.addOnMapClickListener { tappedLocation ->
                    val screenPoint = map.projection.toScreenLocation(tappedLocation)
                    val feature = map.queryRenderedFeatures(screenPoint, PLACE_LAYER_ID)
                        .firstOrNull()
                    val placeId = feature?.getStringProperty(PLACE_ID_PROPERTY)
                    if (placeId != null) {
                        latestOnPlaceSelected(placeId)
                        true
                    } else {
                        false
                    }
                }
                nativeMap = map
            }
            mapView
        },
        update = { view ->
            nativeMap?.let { map ->
                configureMapUi(map, isInteractive, view)
            }
        }
    )

    LaunchedEffect(mapView, lifecycleOwner) {
        var finished = false
        lifecycleOwner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
            if (finished) return@repeatOnLifecycle
            latestOnMapRenderStatusChanged(MapRenderStatus.Loading)
            val result = withTimeoutOrNull(MAP_LOAD_TIMEOUT_MILLIS) {
                val map = snapshotFlow { nativeMap }.filterNotNull().first()
                val style = mapView.awaitMapStyle(map, styleUrl) ?: return@withTimeoutOrNull null
                style.addWaySpotLayers(
                    context = context,
                    markerColor = markerColor.toArgb(),
                    surfaceColor = mapSurfaceColor.toArgb()
                )
                map
            }
            finished = true
            if (result == null) {
                latestOnMapRenderStatusChanged(MapRenderStatus.Failed)
            } else {
                loadedMap = result
                configureMapUi(result, isInteractive, mapView)
                latestOnMapReady(result)
                latestOnMapRenderStatusChanged(MapRenderStatus.Ready)
            }
        }
    }

    LaunchedEffect(loadedMap, places, selectedPlaceId, currentLocation) {
        val style = loadedMap?.style ?: return@LaunchedEffect
        style.getSourceAs<GeoJsonSource>(PLACE_SOURCE_ID)?.setGeoJson(
            placesFeatureCollection(places, selectedPlaceId)
        )
        style.getSourceAs<GeoJsonSource>(LOCATION_SOURCE_ID)?.setGeoJson(
            locationFeatureCollection(currentLocation)
        )
    }

    LaunchedEffect(loadedMap, places, selectedPlaceId, currentLocation, locationFocusRequestId) {
        val map = loadedMap ?: return@LaunchedEffect

        if (locationFocusRequestId != lastHandledLocationRequestId && currentLocation != null) {
            map.animateCamera(
                CameraUpdateFactory.newLatLngZoom(
                    currentLocation.toMapLibreLatLng(),
                    PlacesMapDefaults.selectedPlaceZoom.toDouble()
                )
            )
            lastHandledLocationRequestId = locationFocusRequestId
        } else {
            focusMapOnPlaces(
                map = map,
                places = places,
                selectedPlace = places.firstOrNull { place -> place.id == selectedPlaceId }
            )
        }
    }
}

private suspend fun MapView.awaitMapStyle(map: MapLibreMap, styleUrl: String): Style? {
    val result = CompletableDeferred<Style?>()
    val failedListener = MapView.OnDidFailLoadingMapListener { result.complete(null) }
    addOnDidFailLoadingMapListener(failedListener)
    return try {
        // Las teselas continúan cargando después de que el estilo permite dibujar los marcadores.
        map.setStyle(styleUrl) { style -> result.complete(style) }
        result.await()
    } finally {
        removeOnDidFailLoadingMapListener(failedListener)
    }
}

private fun Style.addWaySpotLayers(context: Context, markerColor: Int, surfaceColor: Int) {
    addImage(
                DEFAULT_MARKER_IMAGE_ID,
                createPlaceMarkerBitmap(
                    context = context,
                    selected = false,
                    primaryColor = markerColor,
                    surfaceColor = surfaceColor
                )
    )
    addImage(
                SELECTED_MARKER_IMAGE_ID,
                createPlaceMarkerBitmap(
                    context = context,
                    selected = true,
                    primaryColor = markerColor,
                    surfaceColor = surfaceColor
                )
    )
    addSource(GeoJsonSource(PLACE_SOURCE_ID, emptyFeatureCollection()))
    addLayer(
                SymbolLayer(PLACE_LAYER_ID, PLACE_SOURCE_ID).withProperties(
                    iconImage(Expression.get(MARKER_IMAGE_PROPERTY)),
                    iconAnchor(Property.ICON_ANCHOR_BOTTOM),
                    iconAllowOverlap(true),
                    iconIgnorePlacement(true)
                )
    )
    addSource(GeoJsonSource(LOCATION_SOURCE_ID, emptyFeatureCollection()))
    addLayer(
                CircleLayer(LOCATION_LAYER_ID, LOCATION_SOURCE_ID).withProperties(
                    circleRadius(7f),
                    circleColor(markerColor),
                    circleStrokeColor(surfaceColor),
                    circleStrokeWidth(2f)
                )
    )
}

private class MapViewLifecycleObserver(
    private val mapView: MapView
) : LifecycleEventObserver {
    private var isStarted = false
    private var isResumed = false
    private var isDestroyed = false

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_START -> start()
            Lifecycle.Event.ON_RESUME -> resume()
            Lifecycle.Event.ON_PAUSE -> pause()
            Lifecycle.Event.ON_STOP -> stop()
            Lifecycle.Event.ON_DESTROY -> destroy()
            else -> Unit
        }
    }

    fun destroy() {
        if (isDestroyed) return
        pause()
        stop()
        mapView.onDestroy()
        isDestroyed = true
    }

    private fun start() {
        if (isDestroyed || isStarted) return
        mapView.onStart()
        isStarted = true
    }

    private fun resume() {
        if (isDestroyed) return
        start()
        if (isResumed) return
        mapView.onResume()
        isResumed = true
    }

    private fun pause() {
        if (isDestroyed || !isResumed) return
        mapView.onPause()
        isResumed = false
    }

    private fun stop() {
        if (isDestroyed || !isStarted) return
        pause()
        mapView.onStop()
        isStarted = false
    }
}

private fun configureMapUi(
    map: MapLibreMap,
    isInteractive: Boolean,
    mapView: MapView
) {
    val uiSettings = map.uiSettings
    uiSettings.setAllGesturesEnabled(isInteractive)
    uiSettings.setCompassEnabled(false)
    uiSettings.setLogoEnabled(true)
    uiSettings.setAttributionEnabled(true)

    val gravity = if (isInteractive) {
        Gravity.BOTTOM or Gravity.START
    } else {
        Gravity.TOP or Gravity.START
    }
    uiSettings.setLogoGravity(gravity)
    uiSettings.setAttributionGravity(gravity)

    val margin = (8.dp.value * mapView.resources.displayMetrics.density).toInt()
    if (isInteractive) {
        uiSettings.setLogoMargins(margin, margin, margin, margin)
        uiSettings.setAttributionMargins(margin, margin, margin, margin)
    } else {
        uiSettings.setLogoMargins(margin, margin, margin, 0)
        uiSettings.setAttributionMargins(margin, margin, margin, 0)
    }
}

private fun initialCameraPosition(): CameraPosition = CameraPosition.Builder()
    .target(PlacesMapDefaults.initialCenter.toMapLibreLatLng())
    .zoom(PlacesMapDefaults.initialZoom.toDouble())
    .build()

internal fun focusMapOnPlaces(
    map: MapLibreMap,
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?
) {
    val selectedCoordinates = selectedPlace?.coordinates?.takeIf(PlaceCoordinates::isValid)
    if (selectedCoordinates != null) {
        map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                selectedCoordinates.toMapLibreLatLng(),
                PlacesMapDefaults.selectedPlaceZoom.toDouble()
            )
        )
        return
    }

    val coordinates = places.mapNotNull { place ->
        place.coordinates?.takeIf(PlaceCoordinates::isValid)
    }.distinct()

    when (coordinates.size) {
        0 -> map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                PlacesMapDefaults.initialCenter.toMapLibreLatLng(),
                PlacesMapDefaults.initialZoom.toDouble()
            )
        )

        1 -> map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                coordinates.first().toMapLibreLatLng(),
                PlacesMapDefaults.selectedPlaceZoom.toDouble()
            )
        )

        else -> {
            val boundsBuilder = LatLngBounds.Builder()
            coordinates.forEach { coordinate ->
                boundsBuilder.include(coordinate.toMapLibreLatLng())
            }
            map.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 120))
        }
    }
}

private fun placesFeatureCollection(
    places: List<PlaceInfo>,
    selectedPlaceId: String?
): FeatureCollection {
    val features = places.mapNotNull { place ->
        val coordinates = place.coordinates?.takeIf(PlaceCoordinates::isValid) ?: return@mapNotNull null
        val isSelected = place.id == selectedPlaceId
        val properties = JsonObject().apply {
            addProperty(PLACE_ID_PROPERTY, place.id)
            addProperty(
                MARKER_IMAGE_PROPERTY,
                if (isSelected) SELECTED_MARKER_IMAGE_ID else DEFAULT_MARKER_IMAGE_ID
            )
        }
        Feature.fromGeometry(coordinates.toMapLibrePoint(), properties)
    }
    return FeatureCollection.fromFeatures(features.toTypedArray())
}

private fun locationFeatureCollection(coordinates: PlaceCoordinates?): FeatureCollection {
    val validCoordinates = coordinates?.takeIf(PlaceCoordinates::isValid)
        ?: return emptyFeatureCollection()
    return FeatureCollection.fromFeatures(
        arrayOf(Feature.fromGeometry(validCoordinates.toMapLibrePoint()))
    )
}

private fun emptyFeatureCollection(): FeatureCollection =
    FeatureCollection.fromFeatures(emptyArray<Feature>())

private fun PlaceCoordinates.toMapLibreLatLng(): LatLng = LatLng(latitude, longitude)

private fun PlaceCoordinates.toMapLibrePoint(): Point = Point.fromLngLat(longitude, latitude)

private fun createPlaceMarkerBitmap(
    context: Context,
    selected: Boolean,
    primaryColor: Int,
    surfaceColor: Int
): Bitmap {
    val density = context.resources.displayMetrics.density
    fun px(dp: Float) = dp * density

    val bitmap = Bitmap.createBitmap(
        px(PlacesMapMarkerDimensions.canvasWidth.value).toInt(),
        px(PlacesMapMarkerDimensions.canvasHeight.value).toInt(),
        Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    val centerX = bitmap.width / 2f
    val markerWidth = px(
        if (selected) PlacesMapMarkerDimensions.selectedWidth.value
        else PlacesMapMarkerDimensions.defaultWidth.value
    )
    val markerHeight = px(
        if (selected) PlacesMapMarkerDimensions.selectedHeight.value
        else PlacesMapMarkerDimensions.defaultHeight.value
    )
    val headRadius = markerWidth / 2f
    val headCenterY = bitmap.height - markerHeight + headRadius
    val left = centerX - headRadius
    val right = centerX + headRadius

    val markerPath = Path().apply {
        moveTo(centerX, bitmap.height.toFloat())
        cubicTo(centerX - markerWidth * 0.18f, headCenterY + headRadius * 0.72f, left, headCenterY + headRadius * 0.38f, left, headCenterY)
        cubicTo(left, headCenterY - headRadius * 1.15f, centerX - headRadius * 0.62f, headCenterY - headRadius * 1.72f, centerX, headCenterY - headRadius * 1.72f)
        cubicTo(centerX + headRadius * 0.62f, headCenterY - headRadius * 1.72f, right, headCenterY - headRadius * 1.15f, right, headCenterY)
        cubicTo(right, headCenterY + headRadius * 0.38f, centerX + markerWidth * 0.18f, headCenterY + headRadius * 0.72f, centerX, bitmap.height.toFloat())
        close()
    }
    canvas.drawPath(
        markerPath,
        Paint(Paint.ANTI_ALIAS_FLAG).apply { color = primaryColor }
    )
    canvas.drawPath(
        markerPath,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = surfaceColor
            style = Paint.Style.STROKE
            strokeWidth = px(PlacesMapMarkerDimensions.strokeWidth.value)
        }
    )
    canvas.drawCircle(
        centerX,
        headCenterY - headRadius * 0.2f,
        headRadius * 0.44f,
        Paint(Paint.ANTI_ALIAS_FLAG).apply {
            xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }
    )
    return bitmap
}
