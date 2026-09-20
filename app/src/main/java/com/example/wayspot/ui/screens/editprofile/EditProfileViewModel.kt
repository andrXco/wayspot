package com.example.wayspot.ui.screens.editprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.EditProfileRules
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.data.repository.StorageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Coordina la edición del perfil y aplica las reglas compartidas antes de exponer el estado. */
@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val storageRepository: StorageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState()) // ***

    val uiState: StateFlow<EditProfileState> = _uiState

    fun loadProfile(profile: UserProfile) {
        _uiState.update { currentState ->
            if (currentState.originalProfile != null) {
                return@update currentState
            }

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

    fun uploadAvatar(uri: Uri) {
        if (_uiState.value.isUploadingAvatar) return
        _uiState.update { currentState ->
            withValidation(
                currentState.copy(
                    selectedAvatarUri = uri,
                    isUploadingAvatar = true,
                    hasAvatarUploadError = false,
                    uploadedAvatarUrl = null
                )
            )
        }

        viewModelScope.launch {
            val result = storageRepository.uploadProfileImage(uri)
            val uploadedUrl = result.getOrNull()
            if (result.isSuccess && uploadedUrl != null) {
                _uiState.update { currentState ->
                    withValidation(currentState.copy(
                        avatarUrl = uploadedUrl,
                        selectedAvatarUri = null,
                        isUploadingAvatar = false,
                        uploadedAvatarUrl = uploadedUrl
                    ))
                }
            } else {
                _uiState.update { currentState ->
                    withValidation(currentState.copy(
                        selectedAvatarUri = null,
                        isUploadingAvatar = false,
                        hasAvatarUploadError = true
                    ))
                }
            }
        }
    }

    fun consumeUploadedAvatar() {
        _uiState.update { currentState ->
            currentState.copy(uploadedAvatarUrl = null)
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
        isSaveEnabled = !state.isUploadingAvatar && EditProfileRules.canSave(
            username = state.username,
            email = state.email,
            location = state.location
        )
    )
}
