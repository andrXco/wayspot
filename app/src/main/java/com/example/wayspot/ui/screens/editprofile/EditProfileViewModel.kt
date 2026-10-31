package com.example.wayspot.ui.screens.editprofile

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.user.EditProfileRules
import com.example.wayspot.data.model.user.UserProfile
import com.example.wayspot.data.repository.StorageRepository
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/** Coordina la edición del perfil y aplica las reglas compartidas antes de exponer el estado. */

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val storageRepository: StorageRepository,
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {


    private val _uiState = MutableStateFlow(EditProfileState())
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
                    avatarUploadErrorMessage = null,
                    uploadedAvatarUrl = null
                )
            )
        }

        viewModelScope.launch {

            val storageResult = storageRepository.uploadProfileImage(uri)
            val uploadedUrl = storageResult.getOrNull()

            val result = if (uploadedUrl != null) {
                val userId = authRepository.currentUser?.uid

                if (userId != null) {
                    userRepository.updateUserAvatar(userId, uploadedUrl)
                } else {
                    Result.failure(Exception("Usuario no autenticado"))
                }
            } else {
                Result.failure(
                    storageResult.exceptionOrNull()
                        ?: Exception("No se pudo subir la foto de perfil")
                )
            }
            if (result.isSuccess && uploadedUrl != null) {

                _uiState.update { currentState ->
                    withValidation(
                        currentState.copy(
                            avatarUrl = uploadedUrl,
                            selectedAvatarUri = null,
                            isUploadingAvatar = false,
                            avatarUploadErrorMessage = null,
                            uploadedAvatarUrl = uploadedUrl
                        )
                    )
                }
            } else {
                val mensaje =
                    result.exceptionOrNull()?.message
                        ?: "No se pudo subir la foto de perfil"

                _uiState.update { currentState ->
                    withValidation(
                        currentState.copy(
                            selectedAvatarUri = null,
                            isUploadingAvatar = false,
                            avatarUploadErrorMessage = mensaje
                        )
                    )
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

    fun saveProfile(
        onResult: (Result<UserProfile>) -> Unit
    ) {
        if (_uiState.value.isSaving) return
        val updatedProfile = profileForSaving()

        if (updatedProfile == null) {
            onResult(Result.failure(Exception("Datos del perfil no válidos")))
            return
        }

        val firebaseUser = authRepository.currentUser

        if (firebaseUser == null) {
            onResult(Result.failure(Exception("Usuario no autenticado")))
            return
        }

        if (updatedProfile.email != firebaseUser.email.orEmpty()) {
            onResult(Result.failure(Exception("La edición del correo aún no está disponible")))
            return
        }

        _uiState.update { currentState ->
            withValidation(
                currentState.copy(isSaving = true)
            )
        }

        viewModelScope.launch {
            val result = userRepository.updateUser(
                name = updatedProfile.name,
                username = updatedProfile.username,
                country = updatedProfile.location,
                bio = updatedProfile.bio,
                userId = firebaseUser.uid,
                avatarUrl = updatedProfile.avatarUrl,
                notificationPreferences = updatedProfile.notificationPreferences
            )


            _uiState.update { currentState ->
                withValidation(
                    currentState.copy(isSaving = false)
                )
            }


            if (result.isSuccess) {
                onResult(Result.success(updatedProfile))
            } else {
                onResult(
                    Result.failure(
                        result.exceptionOrNull()
                            ?: Exception("No se pudo actualizar el perfil")
                    )
                )
            }
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
        isSaveEnabled = !state.isUploadingAvatar &&
                !state.isSaving &&
                EditProfileRules.canSave(
                    username = state.username,
                    email = state.email,
                    location = state.location
                )
        )
}
