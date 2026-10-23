package com.example.wayspot.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.UserProfile
import com.example.wayspot.data.repository.AuthRepository
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


/** Expone los datos del perfil y delega el cierre de sesión al repositorio de autenticación. */
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileState())

    val uiState: StateFlow<ProfileState> = _uiState

    fun loadReviews(userId: String) {
        viewModelScope.launch {
            val result = userRepository.getReviewsByUserId(userId)

            if (result.isSuccess) {
                val reviews = result.getOrNull()

                if (reviews != null) {
                    _uiState.update { currentState ->
                        currentState.copy(
                            reviews = reviews
                        )
                    }
                }
            }
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