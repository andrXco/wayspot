package com.example.wayspot.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.model.place.PlaceInfo
import com.example.wayspot.data.model.review.ReviewInfo
import com.example.wayspot.data.model.review.RecentReviewWindow
import com.example.wayspot.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect

/** Carga desde Firestore los lugares que tienen coordenadas utilizables para el mapa. */
@HiltViewModel
class PlacesMapViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(PlacesMapState())
    val uiState: StateFlow<PlacesMapState> = _uiState
    private var mapLoadJob: Job? = null
    private var expirationJob: Job? = null
    private var loadedReviews: List<ReviewInfo> = emptyList()
    private var availablePlaces: List<PlaceInfo> = emptyList()


    init {
        loadPlaces()
    }

    fun selectPlace(placeId: String?) {
        _uiState.update { currentState ->

            val latestReview = currentState.recentReviews
                .filter { review ->
                    review.placeId == placeId
                }
                .maxByOrNull { review ->
                    review.createdAtMillis ?: 0L
                }

            currentState.copy(
                selectedPlaceId = placeId,
                selectedReviewId = latestReview?.id
            )
        }
    }

    fun selectReview(reviewId: String?) {
        _uiState.update { currentState ->

            val selectedReview = currentState.recentReviews
                .firstOrNull { review ->
                    review.id == reviewId
                }

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

                currentState.copy(
                    selectedReviewId = reviews[nextIndex].id
                )
            }
        }
    }

    fun retry() {
        loadPlaces()
    }


    private fun loadPlaces() {
        mapLoadJob?.cancel()
        expirationJob?.cancel()

        loadedReviews = emptyList()
        availablePlaces = emptyList()

        _uiState.update { currentState ->
            currentState.copy(
                places = emptyList(),
                recentReviews = emptyList(),
                selectedPlaceId = null,
                selectedReviewId = null,
                isLoading = true,
                hasError = false
            )
        }

        mapLoadJob = viewModelScope.launch {
            val result = placeRepository.getPlaces()
            val places = result.getOrNull()

            if (places == null) {
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        hasError = true
                    )
                }
                return@launch
            }

            val validPlaces = places.filter { place ->
                place.coordinates?.isValid == true
            }

            availablePlaces = validPlaces

            reviewRepository.getRecentReviews()
                .catch {
                    _uiState.update { currentState ->
                        currentState.copy(
                            isLoading = false,
                            hasError = true
                        )
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
        val now = System.currentTimeMillis()
        val validPlaceIds = availablePlaces.map { place ->
            place.id
        }.toSet()

        // El corte de Firestore es fijo; este filtro mantiene visibles solo las
        // publicaciones de las últimas 24 horas mientras el mapa permanece abierto.
        val recentReviews = loadedReviews.filter { review ->
            RecentReviewWindow.contains(review.createdAtMillis, now) &&
                review.placeId in validPlaceIds
        }

        val reviewedPlaceIds = recentReviews.map { review ->
            review.placeId
        }.toSet()

        _uiState.update { currentState ->

            val selectedPlaceId = currentState.selectedPlaceId
                ?.takeIf { it in reviewedPlaceIds }

            val selectedReviewId = currentState.selectedReviewId
                ?.takeIf { reviewId ->
                    recentReviews.any { review ->
                        review.id == reviewId &&
                                review.placeId == selectedPlaceId
                    }
                } ?: recentReviews
                .filter { it.placeId == selectedPlaceId }
                .maxByOrNull { it.createdAtMillis ?: 0L }
                ?.id

            currentState.copy(
                places = availablePlaces.filter { place ->
                    place.id in reviewedPlaceIds
                },
                recentReviews = recentReviews,
                selectedPlaceId = selectedPlaceId,
                selectedReviewId = selectedReviewId,
                isLoading = false,
                hasError = false
            )
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
