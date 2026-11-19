package com.example.wayspot.ui.screens.map.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.components.map.MapRenderState
import com.example.wayspot.ui.components.map.MapRenderStatus
import com.example.wayspot.ui.components.map.PlacesMapLoading
import com.example.wayspot.ui.components.map.PlacesMapSurface
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlacesMapDefaults
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.ui.screens.map.MapLocationRequestStatus
import com.example.wayspot.ui.screens.map.MapCatalogStatus
import com.example.wayspot.ui.screens.map.MapNearbyStatus

/** Coordina las secciones visuales del mapa sin poseer estado de negocio ni navegación. */
@Composable
fun PlacesMapLayout(
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?,
    selectedPlaceId: String?,
    onPlaceSelected: (String) -> Unit,
    onPlaceClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    catalogStatus: MapCatalogStatus = MapCatalogStatus.Ready,
    nearbyStatus: MapNearbyStatus = MapNearbyStatus.Idle,
    mapRenderState: MapRenderState = MapRenderState(status = MapRenderStatus.Ready),
    onMapRenderStatusChanged: (MapRenderStatus) -> Unit = {},
    onRetryMapRender: () -> Unit = {},
    selectedReview: ReviewInfo? = null,
    onReviewClick: (String) -> Unit = {},
    selectedPlaceReviews: List<ReviewInfo> = emptyList(),
    onPreviousReview: () -> Unit = {},
    onNextReview: () -> Unit = {},
    isNearbySearchActive: Boolean = false,
    nearbyRadiusKm: Int = PlacesMapDefaults.nearbySearchRadiusMeters / 1000,
    selectedPlaceDistanceMeters: Double? = null,
    onSearchNearby: (PlaceCoordinates) -> Unit = {},
    onClearNearbySearch: () -> Unit = {},
    currentLocation: PlaceCoordinates? = null,
    locationFocusRequestId: Long = 0L,
    locationRequestStatus: MapLocationRequestStatus = MapLocationRequestStatus.Idle,
    onRequestCurrentLocation: () -> Unit = {},
    onLocationFeedbackShown: () -> Unit = {}

) {
    val context = LocalContext.current
    val locationSnackbarHostState = remember { SnackbarHostState() }
    val dataErrorMessage = if (isNearbySearchActive) {
        when (nearbyStatus) {
            MapNearbyStatus.Unavailable -> R.string.places_map_nearby_error
            MapNearbyStatus.InvalidCenter -> R.string.places_map_nearby_invalid_center
            MapNearbyStatus.InvalidResponse -> R.string.places_map_nearby_invalid_response
            MapNearbyStatus.Idle, MapNearbyStatus.Searching, MapNearbyStatus.Ready -> null
        }
    } else {
        when (catalogStatus) {
            MapCatalogStatus.PlacesUnavailable -> R.string.places_map_load_error
            MapCatalogStatus.ReviewsUnavailable -> R.string.places_map_reviews_error
            MapCatalogStatus.Loading, MapCatalogStatus.Ready -> null
        }
    }

    LaunchedEffect(locationRequestStatus) {
        val message = when (locationRequestStatus) {
            MapLocationRequestStatus.PermissionDenied ->
                context.getString(R.string.places_map_location_permission_denied)
            MapLocationRequestStatus.Unavailable ->
                context.getString(R.string.places_map_location_unavailable)
            MapLocationRequestStatus.Idle,
            MapLocationRequestStatus.Requesting -> null
        }
        if (message != null) {
            locationSnackbarHostState.showSnackbar(message)
            onLocationFeedbackShown()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        PlacesMapSurface(
            places = places,
            selectedPlaceId = selectedPlaceId,
            onPlaceSelected = onPlaceSelected,
            mapRenderState = mapRenderState,
            onMapRenderStatusChanged = onMapRenderStatusChanged,
            onRetryMapRender = onRetryMapRender,
            modifier = Modifier.fillMaxSize(),
            isNearbySearchActive = isNearbySearchActive,
            onSearchNearby = onSearchNearby,
            onClearNearbySearch = onClearNearbySearch,
            currentLocation = currentLocation,
            locationFocusRequestId = locationFocusRequestId,
            isRequestingCurrentLocation =
                locationRequestStatus == MapLocationRequestStatus.Requesting,
            onRequestCurrentLocation = onRequestCurrentLocation
        )

        PlacesMapHeader(
            placeCount = places.size,
            isNearbySearchActive = isNearbySearchActive,
            nearbyRadiusKm = nearbyRadiusKm,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(16.dp)
        )

        when {
            mapRenderState.status != MapRenderStatus.Ready -> Unit

            nearbyStatus == MapNearbyStatus.Searching -> PlacesMapLoading(
                messageRes = R.string.places_map_loading_nearby,
                modifier = Modifier.align(Alignment.Center)
            )

            !isNearbySearchActive && catalogStatus == MapCatalogStatus.Loading -> PlacesMapLoading(
                messageRes = R.string.places_map_loading_places,
                modifier = Modifier.align(Alignment.Center)
            )

            dataErrorMessage != null || places.isEmpty() ||
                (selectedPlaceId != null && selectedPlace == null) -> PlacesMapStatusCard(
                canRetry = dataErrorMessage != null &&
                    nearbyStatus != MapNearbyStatus.InvalidCenter,
                messageRes = dataErrorMessage ?: when {
                    selectedPlaceId != null && selectedPlace == null ->
                        R.string.places_map_focused_place_unavailable
                    isNearbySearchActive -> R.string.places_map_nearby_empty
                    else -> R.string.places_map_empty
                },
                onRetry = onRetry,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp)
            )

            selectedPlace != null &&
                    selectedReview != null &&
                    selectedReview.placeId == selectedPlace.id -> MapReviewCard(
                place = selectedPlace,
                review = selectedReview,
                onReviewClick = {
                    onReviewClick(selectedReview.id)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                reviewPosition = selectedPlaceReviews.indexOfFirst {
                    it.id == selectedReview.id
                } + 1,
                reviewCount = selectedPlaceReviews.size,
                onPreviousReview = onPreviousReview,
                onNextReview = onNextReview,
                distanceMeters = selectedPlaceDistanceMeters
            )

            selectedPlace != null -> MapPlaceCard(
                place = selectedPlace,
                onPlaceClick = {
                    onPlaceClick(selectedPlace.id)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                distanceMeters = selectedPlaceDistanceMeters
            )

        }

        SnackbarHost(
            hostState = locationSnackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 144.dp)
        )
    }
}
