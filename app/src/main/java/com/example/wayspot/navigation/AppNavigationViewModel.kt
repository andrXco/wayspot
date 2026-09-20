package com.example.wayspot.navigation

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.Place
import com.example.wayspot.data.model.SavedPlaceList
import com.example.wayspot.data.model.SavedPlacesRules
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Mantiene los datos transversales de navegación que varias pantallas consumen o actualizan.
 *
 * El estado privado evita que cada destino cree una copia independiente del perfil o de los
 * lugares guardados durante la navegación.
 */
@HiltViewModel
class AppNavigationViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppNavigationState())
    val uiState: StateFlow<AppNavigationState> = _uiState

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        _uiState.update { currentState ->
            currentState.copy(
                savedPlaces = PreviewData.savedPlaces
            )
        }
        syncAuthenticatedUser()
    }

    fun syncAuthenticatedUser() {
        val avatarUrl = authRepository.currentUser?.photoUrl?.toString()
        _uiState.update { currentState ->
            currentState.copy(
                userProfile = PreviewData.userProfile.copy(avatarUrl = avatarUrl)
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

    fun updateUserAvatar(avatarUrl: String) {
        _uiState.update { currentState ->
            currentState.copy(
                userProfile = currentState.userProfile?.copy(avatarUrl = avatarUrl)
            )
        }
    }

    fun resetUserProfile() {
        _uiState.update { currentState ->
            currentState.copy(
                userProfile = PreviewData.userProfile.copy(avatarUrl = null)
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
