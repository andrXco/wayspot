package com.example.wayspot.ui.screens.explore

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.explore.ExplorePlaceFilter
import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.savedplace.SavedPlace
import com.example.wayspot.data.model.savedplace.SavedPlacesRules
import com.example.wayspot.data.repository.PlaceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Conserva el catálogo completo y expone solo los lugares que coinciden con ambos filtros. */
@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val placeRepository: PlaceRepository
) : ViewModel() {

    private var allPlaces: List<Place> = emptyList()
    private val _uiState = MutableStateFlow(ExploreState())
    val uiState: StateFlow<ExploreState> = _uiState

    init {
        loadPlaces()
    }

    private fun loadPlaces() {
        _uiState.update { it.copy(isLoading = true, loadFailed = false) }
        viewModelScope.launch {
            placeRepository.getExplorePlaces().fold(
                onSuccess = { fetched ->
                    allPlaces = fetched
                    _uiState.update { current ->
                        val categories = ExplorePlaceFilter.categories(fetched)
                        val selected = current.selectedCategory
                            ?.takeIf { it in categories }
                        current.copy(
                            categories = categories,
                            selectedCategory = selected,
                            places = ExplorePlaceFilter.filter(
                                fetched, current.searchText, selected
                            ),
                            isLoading = false,
                            loadFailed = false
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, loadFailed = true) }
                }
            )
        }
    }

    fun updateSearchText(input: String) {
        _uiState.update { current ->
            current.copy(
                searchText = input,
                places = ExplorePlaceFilter.filter(allPlaces, input, current.selectedCategory)
            )
        }
    }

    fun updateSelectedCategory(category: String?) {
        _uiState.update { current ->
            current.copy(
                selectedCategory = category,
                places = ExplorePlaceFilter.filter(allPlaces, current.searchText, category)
            )
        }
    }

    fun updateSavedPlaces(savedPlaces: List<SavedPlace>) {
        _uiState.update { current ->
            current.copy(savedPlaceIds = SavedPlacesRules.savedPlaceIds(savedPlaces))
        }
    }
}
