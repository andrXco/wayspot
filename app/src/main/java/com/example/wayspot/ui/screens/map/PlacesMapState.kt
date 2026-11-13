package com.example.wayspot.ui.screens.map

import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.NearbyPlace
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.review.ReviewInfo

/** Estado renderizable de la pantalla del mapa. */

data class PlacesMapState(
    val places: List<PlaceInfo> = emptyList(),
    val recentReviews: List<ReviewInfo> = emptyList(),
    val selectedPlaceId: String? = null,
    val isLoading: Boolean = true,
    val hasError: Boolean = false,
    val selectedReviewId: String? = null,
    val nearbyPlaces: List<NearbyPlace>? = null,
    val nearbySearchCenter: PlaceCoordinates? = null,
    val isSearchingNearby: Boolean = false
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

