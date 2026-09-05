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
            withValidation(
                currentState.copy(
                originalProfile = profile,
                initials = profile.initials,
                username = profile.username,
                email = profile.email,
                bio = profile.bio,
                location = profile.location,
                avatarUrl = profile.avatarUrl,
                notificationPreferences = profile.notificationPreferences
                )
            )
        }
    }

    fun updateUsername(input: String) {
        _uiState.update { currentState ->
            withValidation(
                currentState.copy(
                    username = EditProfileRules.filterUsername(input)
                )
            )
        }
    }

    fun updateEmail(input: String) {
        _uiState.update { currentState ->
            withValidation(
                currentState.copy(
                    email = input
                )
            )
        }
    }

    fun updateBio(input: String) {
        _uiState.update { currentState ->
            withValidation(
                currentState.copy(
                    bio = input.take(EditProfileRules.MAX_BIO_LENGTH)
                )
            )
        }
    }

    fun updateLocation(input: String) {
        _uiState.update { currentState ->
            withValidation(
                currentState.copy(
                    location = input
                )
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
                notificationPreferences = currentState.notificationPreferences.copy(
                    newFollowers = value
                )
            )
        }
    }

    fun updateReviewCommentsEnabled(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                notificationPreferences = currentState.notificationPreferences.copy(
                    reviewComments = value
                )
            )
        }
    }

    fun updateReceivedLikesEnabled(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                notificationPreferences = currentState.notificationPreferences.copy(
                    likesReceived = value
                )
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

    fun profileForSaving(): UserProfile? {
        val currentState = _uiState.value
        val originalProfile = currentState.originalProfile

        if (!currentState.isSaveEnabled || originalProfile == null) {
            return null
        }

        return EditProfileRules.normalizeProfile(
            profile = originalProfile,
            username = currentState.username,
            email = currentState.email,
            bio = currentState.bio,
            location = currentState.location,
            avatarUrl = currentState.avatarUrl,
            preferences = currentState.notificationPreferences
        )
    }

    private fun withValidation(state: EditProfileState): EditProfileState = state.copy(
        isSaveEnabled = EditProfileRules.canSave(
            username = state.username,
            email = state.email,
            location = state.location
        )
    )
}
