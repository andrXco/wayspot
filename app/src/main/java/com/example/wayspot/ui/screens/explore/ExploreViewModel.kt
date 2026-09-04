package com.example.wayspot.ui.screens.explore

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.ExploreCategory
import com.example.wayspot.data.model.ExploreCategoryRules
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlacesRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExploreViewModel : ViewModel() {

    private var allPlaces: List<Place> = emptyList()

    private val _uiState = MutableStateFlow(ExploreState())

    val uiState: StateFlow<ExploreState> = _uiState

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        allPlaces = PreviewDataPopular.listPlaces

        _uiState.update { currentState ->
            currentState.copy(
                categories = ExploreCategoryRules.categories,
                places = ExploreCategoryRules.filterPlaces(
                    places = allPlaces,
                    selectedCategory = currentState.selectedCategory
                )
            )
        }
    }

    fun updateSearchText(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchText = input
            )
        }
    }

    fun updateSelectedCategory(category: ExploreCategory) {
        _uiState.update { currentState ->
            currentState.copy(
                selectedCategory = category,
                places = ExploreCategoryRules.filterPlaces(
                    places = allPlaces,
                    selectedCategory = category
                )
            )
        }
    }

    fun updateSavedPlaces(savedPlaces: List<SavedPlace>) {
        _uiState.update { currentState ->
            currentState.copy(
                savedPlaceIds = SavedPlacesRules.savedPlaceIds(savedPlaces)
            )
        }
    }
}
