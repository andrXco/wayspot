package com.example.wayspot.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Carga desde Firestore los lugares que tienen coordenadas utilizables para el mapa. */
@HiltViewModel
class PlacesMapViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PlacesMapState())
    val uiState: StateFlow<PlacesMapState> = _uiState

    init {
        loadPlaces()
    }

    fun selectPlace(placeId: String?) {
        _uiState.update { currentState ->
            currentState.copy(selectedPlaceId = placeId)
        }
    }

    fun retry() {
        loadPlaces()
    }

    private fun loadPlaces() {
        viewModelScope.launch {
            _uiState.update { currentState ->
                currentState.copy(isLoading = true, hasError = false)
            }

            val result = placeRepository.getPlaces()
            val places = result.getOrNull()

            if (places == null) {
                _uiState.update { currentState ->
                    currentState.copy(isLoading = false, hasError = true)
                }
                return@launch
            }

            _uiState.update { currentState ->
                currentState.copy(
                    places = places.filter { place ->
                        place.coordinates?.isValid == true
                    },
                    isLoading = false,
                    hasError = false
                )
            }
        }
    }
}
