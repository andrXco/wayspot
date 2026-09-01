package com.example.wayspot.ui.screens.savedplaces

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.model.SavedPlaceList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class SavedPlacesViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SavedPlacesState())

    val uiState: StateFlow<SavedPlacesState> = _uiState

    fun updateSearchQuery(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchQuery = input
            )
        }
    }

    fun updateSelectedList(list: SavedPlaceList) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedList = list
            )
        }
    }
}