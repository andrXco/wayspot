package com.example.wayspot.ui.screens.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.ui.components.map.MapRenderStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


@HiltViewModel
class PlaceDetailViewModel @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val reviewRepository: ReviewRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailState())

    val uiState: StateFlow<PlaceDetailState> = _uiState

    private var currentPlaceId: String? = null
    private var placeJob: Job? = null
    private var reviewsJob: Job? = null

    fun loadPlace(placeId: String) {
        if (currentPlaceId == placeId) return
        currentPlaceId = placeId
        placeJob?.cancel()
        reviewsJob?.cancel()
        _uiState.value = PlaceDetailState()
        fetchPlace(placeId)
        fetchReviews(placeId)
    }

    fun retryPlace() {
        val placeId = currentPlaceId ?: return
        if (_uiState.value.placeStatus != PlaceDetailLoadStatus.Failed) return
        fetchPlace(placeId)
    }

    fun retryReviews() {
        val placeId = currentPlaceId ?: return
        if (_uiState.value.reviewsStatus != PlaceDetailLoadStatus.Failed) return
        fetchReviews(placeId)
    }

    private fun fetchPlace(placeId: String) {
        _uiState.update { it.copy(placeStatus = PlaceDetailLoadStatus.Loading) }
        placeJob = viewModelScope.launch {
            val place = placeRepository.getPlaceById(placeId).getOrNull()
            val status = if (place == null) PlaceDetailLoadStatus.Failed else PlaceDetailLoadStatus.Ready
            _uiState.update { state ->
                state.copy(place = place, placeStatus = status)
            }
        }
    }

    private fun fetchReviews(placeId: String) {
        _uiState.update { it.copy(reviewsStatus = PlaceDetailLoadStatus.Loading) }
        reviewsJob = viewModelScope.launch {
            val reviews = reviewRepository.getReviewsByPlaceId(placeId).getOrNull()
            val status = if (reviews == null) PlaceDetailLoadStatus.Failed else PlaceDetailLoadStatus.Ready
            _uiState.update { state ->
                state.copy(reviews = reviews.orEmpty(), reviewsStatus = status)
            }
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
}
