package com.example.wayspot.ui.screens.savedplaces

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.SavedPlacesRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import java.util.Locale

class SavedPlacesViewModel : ViewModel() {

    private var allSavedPlaces: List<SavedPlace> = emptyList()
    private var searchTermsByPlaceId: Map<String, List<String>> = emptyMap()
    private var searchLocale: Locale = Locale.getDefault()

    private val _uiState = MutableStateFlow(SavedPlacesState())

    val uiState: StateFlow<SavedPlacesState> = _uiState

    fun updateSearchQuery(input: String) {
        _uiState.update { currentState ->
            deriveState(
                currentState.copy(
                    searchQuery = input
                )
            )
        }
    }

    fun updateSelectedList(list: SavedPlaceList) {
        _uiState.update { currentState ->
            deriveState(
                currentState.copy(
                    selectedList = list
                )
            )
        }
    }

    fun updateSavedPlaces(
        savedPlaces: List<SavedPlace>,
        localizedSearchTerms: Map<String, List<String>>,
        locale: Locale
    ) {
        allSavedPlaces = savedPlaces
        searchTermsByPlaceId = localizedSearchTerms
        searchLocale = locale

        _uiState.update(::deriveState)
    }

    private fun deriveState(currentState: SavedPlacesState): SavedPlacesState {
        val normalizedQuery = currentState.searchQuery
            .trim()
            .lowercase(searchLocale)

        val visibleSavedPlaces = allSavedPlaces.filter { savedPlace ->
            currentState.selectedList in savedPlace.lists &&
                (
                    normalizedQuery.isEmpty() ||
                        searchTermsByPlaceId[savedPlace.place.id]
                            .orEmpty()
                            .any { value ->
                                value
                                    .lowercase(searchLocale)
                                    .contains(normalizedQuery)
                            }
                )
        }

        return currentState.copy(
            destinationCount = allSavedPlaces.size,
            savedPlaces = visibleSavedPlaces,
            counts = SavedPlacesRules.countByList(allSavedPlaces)
        )
    }
}
