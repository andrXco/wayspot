package com.example.wayspot.ui.screens.explore

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.model.ExploreCategory
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ExploreViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(ExploreState())

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
                selectedCategory = category
            )
        }
    }
}