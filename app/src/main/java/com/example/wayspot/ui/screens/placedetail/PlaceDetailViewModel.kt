package com.example.wayspot.ui.screens.placedetail

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewDataPopular
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class PlaceDetailViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PlaceDetailState())

    val uiState: StateFlow<PlaceDetailState> = _uiState

    fun loadPlace(placeId: String) {
        val place = PreviewDataPopular.listPlaces.find {
            it.id == placeId
        }

        _uiState.update { currentState ->
            currentState.copy(
                place = place
            )
        }
    }
}