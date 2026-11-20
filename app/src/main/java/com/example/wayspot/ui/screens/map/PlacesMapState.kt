package com.example.wayspot.ui.screens.map

import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.NearbyPlace
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.ui.components.map.MapRenderState

enum class MapCatalogStatus {
    Loading,
    Ready,
    PlacesUnavailable,
    ReviewsUnavailable
}

enum class MapNearbyStatus {
    Idle,
    Searching,
    Ready,
    InvalidCenter,
    InvalidResponse,
    Unavailable
}

enum class MapLocationRequestStatus {
    Idle,
    Requesting,
    PermissionDenied,
    Unavailable
}

/** Estado renderizable de la pantalla del mapa. */

data class PlacesMapState(
    val places: List<PlaceInfo> = emptyList(),
    val recentReviews: List<ReviewInfo> = emptyList(),
    val focusedPlaceId: String? = null,
    val selectedPlaceId: String? = null,
    val catalogStatus: MapCatalogStatus = MapCatalogStatus.Loading,
    val selectedReviewId: String? = null,
    val nearbyPlaces: List<NearbyPlace>? = null,
    val nearbySearchCenter: PlaceCoordinates? = null,
    val nearbyStatus: MapNearbyStatus = MapNearbyStatus.Idle,
    val mapRenderState: MapRenderState = MapRenderState(),
    val currentLocation: PlaceCoordinates? = null,
    val locationFocusRequestId: Long = 0L,
    val locationRequestStatus: MapLocationRequestStatus = MapLocationRequestStatus.Idle
) {
    val isNearbySearchActive: Boolean
        get() = nearbySearchCenter != null

    val selectedPlace: PlaceInfo?
        get() = places.firstOrNull { place ->
            place.id == selectedPlaceId
        }

    val selectedReview: ReviewInfo?
        get() = recentReviews.firstOrNull { review ->
            review.id == selectedReviewId
        }

    val selectedPlaceReviews: List<ReviewInfo>
        get() = recentReviews
            .filter { review ->
                review.placeId == selectedPlaceId
            }
            .sortedByDescending { review ->
                review.createdAtMillis ?: 0L
            }

    val selectedPlaceDistanceMeters: Double?
        get() = nearbyPlaces
            ?.firstOrNull { nearbyPlace -> nearbyPlace.place.id == selectedPlaceId }
            ?.distanceMeters

}

