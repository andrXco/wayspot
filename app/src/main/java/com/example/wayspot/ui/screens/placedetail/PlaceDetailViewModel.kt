package com.example.wayspot.ui.screens.placedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.repository.PlaceRepository
import com.example.wayspot.data.repository.ReviewRepository
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

        val fallbackPlace = PreviewDataPopular.listPlaces.find { place ->
            place.id == placeId
        }

        _uiState.update { currentState ->
            currentState.copy(
                place = null,
                fallbackPlace = fallbackPlace,
                reviews = emptyList()
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
}
