package com.example.wayspot.ui.screens.profile

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.local.PreviewData
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Expone los datos del perfil y delega el cierre de sesión al repositorio de autenticación. */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())

    val uiState: StateFlow<ProfileState> = _uiState

    init {
        loadReviews()
    }

    private fun loadReviews() {
        _uiState.update { currentState ->
            currentState.copy(
                reviews = PreviewData.listReviews
            )
        }
    }

    fun loadProfile(profile: UserProfile) {
        _uiState.update { currentState ->
            currentState.copy(
                userProfile = profile
            )
        }
    }

    fun signOut() {
        authRepository.signOut()
    }
}
