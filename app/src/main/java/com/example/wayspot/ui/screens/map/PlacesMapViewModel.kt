package com.example.wayspot.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.place.NearbyPlace
import com.example.wayspot.data.model.place.PlaceCoordinates
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.place.PlacesMapDefaults
import com.example.wayspot.data.model.review.RecentReviewWindow
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.repository.NearbyPlacesRepository
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    fun selectPlace(placeId: String?) {
        _uiState.update { currentState ->
            val latestReview = currentState.recentReviews
                .filter { review -> review.placeId == placeId }
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
        } else {
            searchNearby(center)
        }
    }

    fun refreshOnResume() {
        if (!_uiState.value.isLoading && !_uiState.value.isSearchingNearby) {
            retry()
        }
    }

    fun searchNearby(center: PlaceCoordinates) {
        if (!center.isValid) {
            _uiState.update { currentState ->
                currentState.copy(
                    nearbySearchCenter = center,
                    hasError = true,
                    isSearchingNearby = false
                )
            }
            return
        }

        nearbySearchJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                places = emptyList(),
                recentReviews = emptyList(),
                nearbyPlaces = emptyList(),
                nearbySearchCenter = center,
                selectedPlaceId = null,
                selectedReviewId = null,
                isLoading = false,
                isSearchingNearby = true,
                hasError = false
            )
        }

        nearbySearchJob = viewModelScope.launch {
            val result = nearbyPlacesRepository.findNearbyPlaces(
                latitude = center.latitude,
                longitude = center.longitude,
                radiusMeters = PlacesMapDefaults.nearbySearchRadiusMeters
            )
            val nearbyPlaces = result.getOrNull()

            if (nearbyPlaces == null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        nearbyPlaces = null,
                        isSearchingNearby = false,
                        hasError = true
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
            val selectedReviewId = recentReviews
                .filter { review -> review.placeId == selectedPlaceId }
                .maxByOrNull { review -> review.createdAtMillis ?: 0L }
                ?.id

            _uiState.update { currentState ->
                currentState.copy(
                    places = validNearbyPlaces.map(NearbyPlace::place),
                    nearbyPlaces = validNearbyPlaces,
                    recentReviews = recentReviews,
                    selectedPlaceId = selectedPlaceId,
                    selectedReviewId = selectedReviewId,
                    isSearchingNearby = false,
                    hasError = false
                )
            }
        }
    }

    fun clearNearbySearch() {
        nearbySearchJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                nearbyPlaces = null,
                nearbySearchCenter = null,
                isSearchingNearby = false,
                hasError = false
            )
        }
        refreshVisibleReviews()
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
                selectedPlaceId = null,
                selectedReviewId = null,
                nearbyPlaces = null,
                nearbySearchCenter = null,
                isSearchingNearby = false,
                isLoading = true,
                hasError = false
            )
        }

        mapLoadJob = viewModelScope.launch {
            val result = placeRepository.getPlaces()
            val places = result.getOrNull()

            if (places == null) {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, hasError = true)
                }
                return@launch
            }

            availablePlaces = places.filter { place ->
                place.coordinates?.isValid == true
            }

            reviewRepository.getRecentReviews()
                .catch {
                    _uiState.update { currentState ->
                        currentState.copy(isLoading = false, hasError = true)
                    }
                }
                .collect { reviews ->
                    loadedReviews = reviews
                    refreshVisibleReviews()
                    scheduleReviewExpiration()
                }
        }
    }

    private fun refreshVisibleReviews() {
        val validPlaceIds = availablePlaces.mapTo(mutableSetOf()) { place -> place.id }
        val recentReviews = recentReviewsFor(validPlaceIds)

        _uiState.update { currentState ->
            val nearbyPlaceIds = currentState.nearbyPlaces?.mapTo(mutableSetOf()) { nearbyPlace ->
                nearbyPlace.place.id
            }
            val visibleRecentReviews = if (nearbyPlaceIds == null) {
                recentReviews
            } else {
                recentReviews.filter { review -> review.placeId in nearbyPlaceIds }
            }
            val visiblePlaces = currentState.nearbyPlaces?.map(NearbyPlace::place)
                ?: availablePlaces.filter { place ->
                    recentReviews.any { review -> review.placeId == place.id }
                }
            val visiblePlaceIds = visiblePlaces.mapTo(mutableSetOf()) { place -> place.id }
            val selectedPlaceId = currentState.selectedPlaceId
                ?.takeIf { placeId -> placeId in visiblePlaceIds }
                ?: currentState.nearbyPlaces?.firstOrNull()?.place?.id
            val selectedReviewId = currentState.selectedReviewId
                ?.takeIf { reviewId ->
                    visibleRecentReviews.any { review ->
                        review.id == reviewId && review.placeId == selectedPlaceId
                    }
                } ?: visibleRecentReviews
                .filter { review -> review.placeId == selectedPlaceId }
                .maxByOrNull { review -> review.createdAtMillis ?: 0L }
                ?.id

            currentState.copy(
                places = visiblePlaces,
                recentReviews = visibleRecentReviews,
                selectedPlaceId = selectedPlaceId,
                selectedReviewId = selectedReviewId,
                isLoading = false,
                hasError = currentState.isNearbySearchActive && currentState.hasError
            )
        }
    }

    private fun recentReviewsFor(placeIds: Set<String>): List<ReviewInfo> {
        val now = System.currentTimeMillis()
        return loadedReviews.filter { review ->
            RecentReviewWindow.contains(review.createdAtMillis, now) &&
                review.placeId in placeIds
        }
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
