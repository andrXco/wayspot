package com.example.wayspot.ui.screens.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeState())

    val uiState: StateFlow<HomeState> = _uiState

    fun updateSearchText(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                searchText = input
            )
        }
    }
}