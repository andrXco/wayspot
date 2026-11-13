package com.example.wayspot.ui.screens.map.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.components.map.PlacesMapSurface
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlacesMapDefaults
import com.example.wayspot.data.model.review.ReviewInfo

/** Coordina las secciones visuales del mapa sin poseer estado de negocio ni navegación. */
@Composable
fun PlacesMapLayout(
    places: List<PlaceInfo>,
    selectedPlace: PlaceInfo?,
    selectedPlaceId: String?,
    isLoading: Boolean,
    hasError: Boolean,
    onPlaceSelected: (String) -> Unit,
    onPlaceClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    selectedReview: ReviewInfo? = null,
    onReviewClick: (String) -> Unit = {},
    selectedPlaceReviews: List<ReviewInfo> = emptyList(),
    onPreviousReview: () -> Unit = {},
    onNextReview: () -> Unit = {},
    isNearbySearchActive: Boolean = false,
    isSearchingNearby: Boolean = false,
    nearbyRadiusKm: Int = PlacesMapDefaults.nearbySearchRadiusMeters / 1000,
    selectedPlaceDistanceMeters: Double? = null,
    onSearchNearby: (PlaceCoordinates) -> Unit = {},
    onClearNearbySearch: () -> Unit = {}

) {
    Box(modifier = modifier.fillMaxSize()) {
        PlacesMapSurface(
            places = places,
            selectedPlaceId = selectedPlaceId,
            onPlaceSelected = onPlaceSelected,
            modifier = Modifier.fillMaxSize(),
            isNearbySearchActive = isNearbySearchActive,
            onSearchNearby = onSearchNearby,
            onClearNearbySearch = onClearNearbySearch
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
            isLoading || isSearchingNearby -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )

            hasError || places.isEmpty() || (selectedPlaceId != null && selectedPlace == null) -> PlacesMapStatusCard(
                hasError = hasError,
                messageRes = when {
                    hasError && isNearbySearchActive -> R.string.places_map_nearby_error
                    hasError -> R.string.places_map_load_error
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
    }
}
