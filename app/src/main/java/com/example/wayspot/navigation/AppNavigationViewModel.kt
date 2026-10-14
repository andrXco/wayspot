package com.example.wayspot.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import android.util.Log

import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.place.Place
import com.example.wayspot.data.model.savedplace.SavedPlaceList
import com.example.wayspot.data.model.savedplace.SavedPlacesRules
import com.example.wayspot.data.model.user.UserProfile
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.model.user.UserStats
import com.example.wayspot.data.repository.UserRepository


import dagger.hilt.android.lifecycle.HiltViewModel

import javax.inject.Inject

import kotlinx.coroutines.launch

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
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppNavigationState())
    val uiState: StateFlow<AppNavigationState> = _uiState
    val currentUserId: String?
        get() = authRepository.currentUser?.uid


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
        val firebaseUser = authRepository.currentUser

        if (firebaseUser == null) {
            resetUserProfile()
            return
        }

        val userId = firebaseUser.uid

        _uiState.update { currentState ->
            currentState.copy(
                userProfile = PreviewData.userProfile.copy(
                    name = "",
                    username = "",
                    bio = "",
                    email = firebaseUser.email.orEmpty(),
                    location = "",
                    avatarUrl = firebaseUser.photoUrl?.toString(),
                    initials = "",
                    isVerified = false,
                    stats = UserStats(
                        places = 0,
                        reviews = 0,
                        followers = "0"
                    )
                )
            )
        }

        viewModelScope.launch {
            val result = userRepository.getUserById(userId)

            if (result.isSuccess) {
                val user = result.getOrNull() ?: return@launch

                if (authRepository.currentUser?.uid != userId) {
                    return@launch
                }

                _uiState.update { currentState ->
                    currentState.copy(
                        userProfile = currentState.userProfile?.copy(
                            name = user.name,
                            username = user.username,
                            bio = user.bio.orEmpty(),
                            location = user.location.orEmpty(),
                            initials = user.name.take(1).uppercase()
                        )
                    )
                }
            } else {
                Log.e(
                    "AppNavigationViewModel",
                    "Error al obtener el perfil",
                    result.exceptionOrNull()
                )
            }
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

    fun notifyCommentPublished(reviewId: String) {
        _uiState.update { currentState ->
            currentState.copy(
                commentRefreshReviewId = reviewId,
                commentRefreshVersion = currentState.commentRefreshVersion + 1
            )
        }
    }

    fun consumeCommentRefresh(reviewId: String, version: Int) {
        _uiState.update { currentState ->
            if (
                currentState.commentRefreshReviewId == reviewId &&
                currentState.commentRefreshVersion == version
            ) {
                currentState.copy(
                    commentRefreshReviewId = null,
                    commentRefreshVersion = 0
                )
            } else {
                currentState
            }
        }
    }
}
