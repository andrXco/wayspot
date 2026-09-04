package com.example.wayspot.ui.screens.explore

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewDataPopular
import com.example.wayspot.data.model.ExploreCategory
import com.example.wayspot.data.model.ExploreCategoryRules
import com.example.wayspot.data.model.SavedPlace
import com.example.wayspot.data.model.SavedPlacesRules
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExploreViewModel : ViewModel() {

    private val allPlaces = PreviewDataPopular.listPlaces

    private val _uiState = MutableStateFlow(
        ExploreState(
            categories = ExploreCategoryRules.categories,
            places = ExploreCategoryRules.filterPlaces(
                places = allPlaces,
                selectedCategory = ExploreCategoryRules.initialCategory
            )
        )
    )

    val uiState: StateFlow<ExploreState> = _uiState

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
