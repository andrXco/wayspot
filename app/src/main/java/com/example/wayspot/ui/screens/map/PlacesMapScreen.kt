package com.example.wayspot.ui.screens.map

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlacesMapDefaults
import com.example.wayspot.data.model.place.toPlaceInfo
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.components.location.hasLocationPermission
import com.example.wayspot.ui.components.location.requestCurrentLocation
import com.example.wayspot.ui.screens.map.components.PlacesMapLayout
import com.example.wayspot.ui.theme.WayspotTheme

/** Adapta argumentos y eventos del mapa al ViewModel del destino. */

@Composable
fun PlacesMapScreen(
    viewModel: PlacesMapViewModel,
    focusedPlaceId: String?,
    onPlaceClick: (String) -> Unit,
    onReviewClick: (String) -> Unit,
    modifier: Modifier = Modifier,

)
 {
    val state by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionResults ->
        if (permissionResults.values.any { isGranted -> isGranted }) {
            requestCurrentLocation(
                context = context,
                onLocationFound = viewModel::onCurrentLocationFound,
                onUnavailable = viewModel::onCurrentLocationUnavailable
            )
        } else {
            viewModel.onCurrentLocationPermissionDenied()
        }
    }

    DisposableEffect(lifecycleOwner, viewModel) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.refreshOnResume()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(focusedPlaceId) {
        viewModel.focusPlace(focusedPlaceId)
    }

    PlacesMapLayout(
         places = state.places,
         reviews = state.recentReviews,
         selectedPlace = state.selectedPlace,
         selectedPlaceId = state.selectedPlaceId,
         catalogStatus = state.catalogStatus,
         nearbyStatus = state.nearbyStatus,
         mapRenderState = state.mapRenderState,
         onMapRenderStatusChanged = viewModel::onMapRenderStatusChanged,
         onRetryMapRender = viewModel::retryMapRender,
         onPlaceSelected = viewModel::selectPlace,
         onPlaceClick = onPlaceClick,
         onRetry = viewModel::retry,
         modifier = modifier,
         selectedReview = state.selectedReview,
         onReviewSelected = viewModel::selectReview,
         onReviewClick = onReviewClick,
         selectedPlaceReviews = state.selectedPlaceReviews,
         onPreviousReview = viewModel::previousReview,
         onNextReview = viewModel::nextReview,
         isNearbySearchActive = state.isNearbySearchActive,
         nearbyRadiusKm = PlacesMapDefaults.nearbySearchRadiusMeters / 1000,
         selectedPlaceDistanceMeters = state.selectedPlaceDistanceMeters,
         onSearchNearby = viewModel::searchNearby,
         onClearNearbySearch = viewModel::clearNearbySearch,
         currentLocation = state.currentLocation,
         locationFocusRequestId = state.locationFocusRequestId,
         locationRequestStatus = state.locationRequestStatus,
         onRequestCurrentLocation = {
             viewModel.startCurrentLocationRequest()
             if (hasLocationPermission(context)) {
                 requestCurrentLocation(
                     context = context,
                     onLocationFound = viewModel::onCurrentLocationFound,
                     onUnavailable = viewModel::onCurrentLocationUnavailable
                 )
             } else {
                 locationPermissionLauncher.launch(
                     arrayOf(
                         Manifest.permission.ACCESS_COARSE_LOCATION,
                         Manifest.permission.ACCESS_FINE_LOCATION
                     )
                 )
             }
         },
         onLocationFeedbackShown = viewModel::clearLocationRequestStatus

     )
 }

/** Orquesta las secciones a partir de estado inmutable y callbacks. */
@Composable
fun PlacesMapContent(
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?,
    selectedPlaceId: String?,
    onPlaceSelected: (String) -> Unit,
    onPlaceClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    PlacesMapLayout(
        places = places,
        selectedPlace = selectedPlace,
        selectedPlaceId = selectedPlaceId,
        onPlaceSelected = onPlaceSelected,
        onPlaceClick = onPlaceClick,
        onRetry = onRetry,
        modifier = modifier
    )
}

@WayspotMultiPreview
@Composable
private fun PlacesMapScreenPreview() {
    val context = LocalContext.current
    val places = PreviewDataPopular.listPlaces.map { place ->
        place.toPlaceInfo(context::getString)
    }

    WayspotTheme {
        PlacesMapContent(
            places = places,
            selectedPlace = places.firstOrNull(),
            selectedPlaceId = places.firstOrNull()?.id,
            onPlaceSelected = {},
            onPlaceClick = {},
            onRetry = {}
        )
    }
}
