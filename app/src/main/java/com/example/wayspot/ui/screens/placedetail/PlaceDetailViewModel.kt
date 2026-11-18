package com.example.wayspot.ui.screens.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
import com.example.wayspot.ui.components.map.MapRenderState
import com.example.wayspot.ui.components.map.MapRenderStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
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


    fun loadPlace(placeId: String) {

        _uiState.update { currentState ->
            currentState.copy(
                place = null,
                fallbackPlace = null,
                reviews = emptyList(),
                mapRenderState = MapRenderState()
            )
        }

        viewModelScope.launch {
            val result = placeRepository.getPlaceById(placeId)

            if (result.isSuccess) {
                val place = result.getOrNull()

                if (place != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            place = place
                        )
                    }
                }
            }
        }

        viewModelScope.launch {
            val result = reviewRepository.getReviewsByPlaceId(placeId)

            if (result.isSuccess) {
                val reviews = result.getOrNull()

                if (reviews != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = reviews
                        )
                    }
                }
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
