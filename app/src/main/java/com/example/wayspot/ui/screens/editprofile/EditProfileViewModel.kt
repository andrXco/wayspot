package com.example.wayspot.ui.screens.editprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.injection.IoDispatcher
import com.example.wayspot.data.model.AuthFailure
import com.example.wayspot.data.model.AuthOutcome
import com.example.wayspot.data.model.AuthSession
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import com.example.wayspot.data.model.EditProfileRules
import com.example.wayspot.data.model.UserProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(EditProfileState())

    val uiState: StateFlow<EditProfileState> = _uiState
    private val _events = MutableSharedFlow<EditProfileEvent>()
    val events = _events

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
                isDeleteConfirmationVisible = false, deletePassword = "", deleteFailure = null
            )
        }
    }

    fun updateDeletePassword(value: String) { _uiState.update { it.copy(deletePassword = value, deleteFailure = null) } }

    fun deleteAccount() = viewModelScope.launch(ioDispatcher) {
        val password = _uiState.value.deletePassword
        _uiState.update { it.copy(isDeleting = true, deleteFailure = null) }
        val session = authRepository.currentSession
        if (session == null) {
            showDeleteFailure(AuthFailure.Unknown)
            return@launch
        }
        when (val reauthentication = authRepository.reauthenticate(password)) {
            is AuthOutcome.Failure -> showDeleteFailure(reauthentication.reason)
            is AuthOutcome.Success -> {
                when (val profileDeletion = userRepository.deleteUser(session.uid)) {
                    is AuthOutcome.Failure -> showDeleteFailure(profileDeletion.reason)
                    is AuthOutcome.Success -> {
                        when (val accountDeletion = authRepository.deleteCurrentUser()) {
                            is AuthOutcome.Failure -> restoreProfileAfterFailedDeletion(
                                session = session,
                                originalFailure = accountDeletion.reason
                            )
                            is AuthOutcome.Success -> {
                                _uiState.update {
                                    it.copy(
                                        isDeleting = false,
                                        isDeleteConfirmationVisible = false
                                    )
                                }
                                _events.emit(EditProfileEvent.AccountDeleted)
                            }
                        }
                    }
                }
            }
        }
    }

    private suspend fun restoreProfileAfterFailedDeletion(
        session: AuthSession,
        originalFailure: AuthFailure
    ) {
        val username = session.username
        if (username == null) {
            showDeleteFailure(originalFailure)
            return
        }
        when (val restoration = userRepository.registerUser(session, username)) {
            is AuthOutcome.Success -> showDeleteFailure(originalFailure)
            is AuthOutcome.Failure -> showDeleteFailure(restoration.reason)
        }
    }

    private fun showDeleteFailure(failure: AuthFailure) {
        _uiState.update { it.copy(isDeleting = false, deleteFailure = failure) }
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

sealed interface EditProfileEvent { data object AccountDeleted : EditProfileEvent }
