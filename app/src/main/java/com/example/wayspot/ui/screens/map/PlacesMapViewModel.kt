package com.example.wayspot.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.place.NearbyPlace
import com.example.wayspot.data.model.place.NearbyPlacesResponseException
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlacesMapDefaults
import com.example.wayspot.data.model.review.RecentReviewWindow
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.repository.NearbyPlacesRepository
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.ui.components.map.MapRenderStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/** Carga lugares con coordenadas y reseñas recientes, y permite buscar alrededor del mapa. */
@HiltViewModel
class PlacesMapViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val nearbyPlacesRepository: NearbyPlacesRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesMapState())
    val uiState: StateFlow<PlacesMapState> = _uiState

    private var mapLoadJob: Job? = null
    private var nearbySearchJob: Job? = null
    private var expirationJob: Job? = null
    private var loadedReviews: List<ReviewInfo> = emptyList()
    private var availablePlaces: List<PlaceInfo> = emptyList()

    init {
        loadPlaces()
    }

    fun focusPlace(placeId: String?) {
        if (_uiState.value.focusedPlaceId == placeId) return
        _uiState.update { state ->
            state.copy(focusedPlaceId = placeId, selectedPlaceId = placeId)
        }
        refreshVisibleReviews()
    }

    fun selectPlace(placeId: String?) {
        _uiState.update { currentState ->
            val latestReview = currentState.recentReviews
                .filter { review -> placeId != null && review.placeId == placeId }
                .maxByOrNull { review -> review.createdAtMillis ?: 0L }

            currentState.copy(
                selectedPlaceId = placeId,
                selectedReviewId = latestReview?.id
            )
        }
    }

    fun selectReview(reviewId: String?) {
        _uiState.update { currentState ->
            val selectedReview = currentState.recentReviews
                .firstOrNull { review -> review.id == reviewId }

            currentState.copy(
                selectedReviewId = selectedReview?.id,
                selectedPlaceId = selectedReview?.placeId
            )
        }
    }

    fun nextReview() {
        changeSelectedReview(1)
    }

    fun previousReview() {
        changeSelectedReview(-1)
    }

    private fun changeSelectedReview(direction: Int) {
        _uiState.update { currentState ->
            val reviews = currentState.selectedPlaceReviews
            if (reviews.isEmpty()) {
                currentState
            } else {
                val currentIndex = reviews.indexOfFirst { review ->
                    review.id == currentState.selectedReviewId
                }
                val nextIndex = if (currentIndex == -1) {
                    0
                } else {
                    (currentIndex + direction + reviews.size) % reviews.size
                }

                currentState.copy(selectedReviewId = reviews[nextIndex].id)
            }
        }
    }

    fun retry() {
        val center = _uiState.value.nearbySearchCenter
        if (center == null) {
            loadPlaces()
        } else if (center.isValid) {
            searchNearby(center)
        }
    }

    fun onMapRenderStatusChanged(status: MapRenderStatus) {
        _uiState.update { state ->
            state.copy(mapRenderState = state.mapRenderState.copy(status = status))
        }
    }

    fun retryMapRender() {
        _uiState.update { state ->
            state.copy(mapRenderState = state.mapRenderState.retry())
        }
    }

    fun refreshOnResume() {
        val state = _uiState.value
        if (state.catalogStatus != MapCatalogStatus.Loading &&
            state.nearbyStatus != MapNearbyStatus.Searching &&
            state.locationRequestStatus != MapLocationRequestStatus.Requesting
        ) {
            retry()
        }
    }

    fun searchNearby(center: PlaceCoordinates) {
        nearbySearchJob?.cancel()
        if (!center.isValid) {
            startNearbySearch(center, MapNearbyStatus.InvalidCenter)
            return
        }

        startNearbySearch(center)

        nearbySearchJob = viewModelScope.launch {
            val result = nearbyPlacesRepository.findNearbyPlaces(
                latitude = center.latitude,
                longitude = center.longitude,
                radiusMeters = PlacesMapDefaults.nearbySearchRadiusMeters
            )
            val nearbyPlaces = result.getOrNull()

            if (nearbyPlaces == null) {
                val status = if (result.exceptionOrNull() is NearbyPlacesResponseException) {
                    MapNearbyStatus.InvalidResponse
                } else {
                    MapNearbyStatus.Unavailable
                }
                _uiState.update { currentState ->
                    currentState.copy(
                        nearbyStatus = status
                    )
                }
                return@launch
            }

            val validNearbyPlaces = nearbyPlaces.filter { nearbyPlace ->
                nearbyPlace.place.coordinates?.isValid == true
            }
            val visiblePlaceIds = validNearbyPlaces.mapTo(mutableSetOf()) { nearbyPlace ->
                nearbyPlace.place.id
            }
            val recentReviews = recentReviewsFor(visiblePlaceIds)
            val selectedPlaceId = validNearbyPlaces.firstOrNull()?.place?.id
            val inRadiusReviews = recentReviews.filter { review ->
                (review.placeId != null && review.placeId in visiblePlaceIds) ||
                    review.coordinates?.let { coordinates ->
                        isWithinRadius(coordinates, center, PlacesMapDefaults.nearbySearchRadiusMeters)
                    } == true
            }
            val selectedReviewId = inRadiusReviews
                .filter { review -> selectedPlaceId != null && review.placeId == selectedPlaceId }
                .maxByOrNull { review -> review.createdAtMillis ?: 0L }
                ?.id

            _uiState.update { currentState ->
                currentState.copy(
                    places = validNearbyPlaces.map(NearbyPlace::place),
                    nearbyPlaces = validNearbyPlaces,
                    recentReviews = inRadiusReviews,
                    selectedPlaceId = selectedPlaceId,
                    selectedReviewId = selectedReviewId,
                    nearbyStatus = MapNearbyStatus.Ready
                )
            }
        }
    }

    private fun startNearbySearch(
        center: PlaceCoordinates,
        status: MapNearbyStatus = MapNearbyStatus.Searching
    ) {
        _uiState.update { state ->
            state.copy(
                places = emptyList(),
                recentReviews = emptyList(),
                nearbyPlaces = null,
                nearbySearchCenter = center,
                selectedPlaceId = null,
                selectedReviewId = null,
                nearbyStatus = status
            )
        }
    }

    fun clearNearbySearch() {
        nearbySearchJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                nearbyPlaces = null,
                nearbySearchCenter = null,
                nearbyStatus = MapNearbyStatus.Idle
            )
        }
        refreshVisibleReviews()
    }

    fun startCurrentLocationRequest() {
        _uiState.update { currentState ->
            currentState.copy(locationRequestStatus = MapLocationRequestStatus.Requesting)
        }
    }

    fun onCurrentLocationPermissionDenied() {
        _uiState.update { currentState ->
            currentState.copy(locationRequestStatus = MapLocationRequestStatus.PermissionDenied)
        }
    }

    fun onCurrentLocationUnavailable() {
        _uiState.update { currentState ->
            currentState.copy(locationRequestStatus = MapLocationRequestStatus.Unavailable)
        }
    }

    fun onCurrentLocationFound(coordinates: PlaceCoordinates) {
        if (!coordinates.isValid) {
            onCurrentLocationUnavailable()
            return
        }

        _uiState.update { currentState ->
            currentState.copy(
                currentLocation = coordinates,
                locationFocusRequestId = currentState.locationFocusRequestId + 1,
                locationRequestStatus = MapLocationRequestStatus.Idle
            )
        }
    }

    fun clearLocationRequestStatus() {
        _uiState.update { currentState ->
            if (currentState.locationRequestStatus == MapLocationRequestStatus.PermissionDenied ||
                currentState.locationRequestStatus == MapLocationRequestStatus.Unavailable
            ) {
                currentState.copy(locationRequestStatus = MapLocationRequestStatus.Idle)
            } else {
                currentState
            }
        }
    }

    private fun loadPlaces() {
        mapLoadJob?.cancel()
        nearbySearchJob?.cancel()
        expirationJob?.cancel()

        loadedReviews = emptyList()
        availablePlaces = emptyList()

        _uiState.update { currentState ->
            currentState.copy(
                places = emptyList(),
                recentReviews = emptyList(),
                nearbyPlaces = null,
                nearbySearchCenter = null,
                catalogStatus = MapCatalogStatus.Loading,
                nearbyStatus = MapNearbyStatus.Idle
            )
        }

        mapLoadJob = viewModelScope.launch {
            val result = placeRepository.getPlaces()
            val places = result.getOrNull().orEmpty()
            availablePlaces = places.filter { place ->
                place.coordinates?.isValid == true
            }
            _uiState.update { currentState ->
                currentState.copy(
                    catalogStatus = if (result.isSuccess) {
                        MapCatalogStatus.Loading
                    } else {
                        MapCatalogStatus.PlacesUnavailable
                    }
                )
            }

            flow { emitAll(reviewRepository.getRecentReviews()) }
                .catch {
                    _uiState.update { currentState ->
                        currentState.copy(catalogStatus = MapCatalogStatus.ReviewsUnavailable)
                    }
                }
                .collect { reviews ->
                    loadedReviews = reviews
                    refreshVisibleReviews(catalogUpdated = result.isSuccess)
                    scheduleReviewExpiration()
                }
        }
    }

    private fun refreshVisibleReviews(catalogUpdated: Boolean = false) {
        val validPlaceIds = availablePlaces.mapTo(mutableSetOf()) { place -> place.id }
        val recentReviews = recentReviewsFor(validPlaceIds)

        _uiState.update { state ->
            val currentState = if (catalogUpdated) {
                state.copy(catalogStatus = MapCatalogStatus.Ready)
            } else {
                state
            }
            if (currentState.isNearbySearchActive && currentState.nearbyPlaces == null) {
                return@update currentState
            }
            val nearbyPlaceIds = currentState.nearbyPlaces?.mapTo(mutableSetOf()) { nearbyPlace ->
                nearbyPlace.place.id
            }
            val visibleRecentReviews = if (nearbyPlaceIds == null) {
                recentReviews
            } else {
                recentReviews.filter { review ->
                    (review.placeId != null && review.placeId in nearbyPlaceIds) ||
                        (currentState.nearbySearchCenter != null &&
                            review.coordinates?.let { coordinates ->
                                isWithinRadius(
                                    coordinates,
                                    currentState.nearbySearchCenter,
                                    PlacesMapDefaults.nearbySearchRadiusMeters
                                )
                            } == true)
                }
            }
            val visiblePlaces = currentState.nearbyPlaces?.map(NearbyPlace::place)
                ?: availablePlaces.filter { place ->
                    place.id == currentState.focusedPlaceId ||
                        recentReviews.any { review -> review.placeId == place.id }
                }
            val visiblePlaceIds = visiblePlaces.mapTo(mutableSetOf()) { place -> place.id }
            val selectedPlaceId = currentState.selectedPlaceId
                ?.takeIf { placeId -> placeId in visiblePlaceIds }
                ?: currentState.nearbyPlaces?.firstOrNull()?.place?.id
                ?: currentState.focusedPlaceId.takeUnless { currentState.isNearbySearchActive }
            val selectedReviewId = currentState.selectedReviewId
                ?.takeIf { reviewId ->
                    visibleRecentReviews.any { review ->
                        review.id == reviewId &&
                            (selectedPlaceId == null || review.placeId == selectedPlaceId)
                    }
                } ?: visibleRecentReviews
                .filter { review -> selectedPlaceId != null && review.placeId == selectedPlaceId }
                .maxByOrNull { review -> review.createdAtMillis ?: 0L }
                ?.id

            currentState.copy(
                places = visiblePlaces,
                recentReviews = visibleRecentReviews,
                selectedPlaceId = selectedPlaceId,
                selectedReviewId = selectedReviewId
            )
        }
    }

    private fun recentReviewsFor(placeIds: Set<String>): List<ReviewInfo> {
        val now = System.currentTimeMillis()
        return loadedReviews.filter { review ->
            RecentReviewWindow.contains(review.createdAtMillis, now) &&
                (review.placeId != null && review.placeId in placeIds ||
                    review.coordinates?.isValid == true)
        }
    }

    private fun isWithinRadius(
        coordinates: PlaceCoordinates,
        center: PlaceCoordinates,
        radiusMeters: Int
    ): Boolean {
        val latitudeDifference = Math.toRadians(center.latitude - coordinates.latitude)
        val longitudeDifference = Math.toRadians(center.longitude - coordinates.longitude)
        val haversine = sin(latitudeDifference / 2).pow(2) +
            cos(Math.toRadians(coordinates.latitude)) *
            cos(Math.toRadians(center.latitude)) *
            sin(longitudeDifference / 2).pow(2)
        val distanceMeters = 6_371_000.0 * 2 * atan2(sqrt(haversine), sqrt(1 - haversine))
        return distanceMeters <= radiusMeters
    }

    private fun scheduleReviewExpiration() {
        expirationJob?.cancel()

        expirationJob = viewModelScope.launch {
            while (true) {
                val nextExpiration = _uiState.value.recentReviews
                    .mapNotNull { review ->
                        review.createdAtMillis?.let(RecentReviewWindow::expiresAtMillis)
                    }
                    .minOrNull() ?: break

                val waitingTime = (
                    nextExpiration - System.currentTimeMillis()
                    ).coerceAtLeast(1L)

                delay(waitingTime)
                refreshVisibleReviews()
            }
        }
    }
}
