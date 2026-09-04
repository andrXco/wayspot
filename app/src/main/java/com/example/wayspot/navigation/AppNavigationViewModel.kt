package com.example.wayspot.navigation

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class AppNavigationViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AppNavigationState())
    val uiState: StateFlow<AppNavigationState> = _uiState

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.update { currentState ->
            currentState.copy(
                userProfile = PreviewData.userProfile,
                savedPlaces = PreviewData.savedPlaces
            )
        }
    }

    fun updateUserProfile(updatedProfile: UserProfile) {
        _uiState.update { currentState ->
            currentState.copy(
                userProfile = updatedProfile
            )
        }
    }

    fun resetUserProfile() {
        _uiState.update { currentState ->
            currentState.copy(
                userProfile = PreviewData.userProfile
            )
        }
    }

    fun resetSavedPlaces() {
        _uiState.update { currentState ->
            currentState.copy(
                savedPlaces = PreviewData.savedPlaces
            )
        }
    }

    fun removeSavedPlace(
        placeId: String,
        list: SavedPlaceList
    ) {
        _uiState.update { currentState ->
            currentState.copy(
                savedPlaces = SavedPlacesRules.removeFromList(
                    savedPlaces = currentState.savedPlaces,
                    placeId = placeId,
                    list = list
                )
            )
        }
    }

    fun toggleSavedPlace(place: Place) {
        _uiState.update { currentState ->
            currentState.copy(
                savedPlaces = SavedPlacesRules.toggleSaved(
                    savedPlaces = currentState.savedPlaces,
                    place = place
                )
            )
        }
    }
}
