package com.example.wayspot.ui.screens.placedetail

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewDataPopular
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Resuelve el identificador de ruta en el proveedor de lugares y publica el detalle encontrado. */
@HiltViewModel
class PlaceDetailViewModel @Inject constructor() : ViewModel() {

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
