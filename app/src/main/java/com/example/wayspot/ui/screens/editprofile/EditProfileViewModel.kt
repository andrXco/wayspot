package com.example.wayspot.ui.screens.editprofile

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.model.EditProfileRules
import com.example.wayspot.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class EditProfileViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState())

    val uiState: StateFlow<EditProfileState> = _uiState

    fun loadProfile(profile: UserProfile) {
        _uiState.update { currentState ->
            currentState.copy(
                username = profile.username,
                email = profile.email,
                bio = profile.bio,
                location = profile.location,
                avatarUrl = profile.avatarUrl,
                newFollowersEnabled = profile.notificationPreferences.newFollowers,
                reviewCommentsEnabled = profile.notificationPreferences.reviewComments,
                receivedLikesEnabled = profile.notificationPreferences.likesReceived
            )
        }
    }

    fun updateUsername(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                username = EditProfileRules.filterUsername(input)
            )
        }
    }

    fun updateEmail(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = input
            )
        }
    }

    fun updateBio(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                bio = input.take(EditProfileRules.MAX_BIO_LENGTH)
            )
        }
    }

    fun updateLocation(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                location = input
            )
        }
    }

    fun updateAvatarUrl(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                avatarUrl = input
            )
        }
    }

    fun updateNewFollowersEnabled(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                newFollowersEnabled = value
            )
        }
    }

    fun updateReviewCommentsEnabled(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                reviewCommentsEnabled = value
            )
        }
    }

    fun updateReceivedLikesEnabled(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                receivedLikesEnabled = value
            )
        }
    }

    fun showDeleteConfirmation() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteConfirmationVisible = true
            )
        }
    }

    fun hideDeleteConfirmation() {
        _uiState.update { currentState ->
            currentState.copy(
                isDeleteConfirmationVisible = false
            )
        }
    }
}