package com.example.wayspot.ui.screens.publicprofile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PublicProfileViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(PublicProfileState())
    val uiState: StateFlow<PublicProfileState> = _uiState

    fun loadUser(id: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null, user = null, reviews = emptyList()) }
        viewModelScope.launch {
            val user = userRepository.getUserById(id)
            if (user.isFailure) {
                _uiState.update { it.copy(isLoading = false, errorMessage = user.exceptionOrNull()?.message) }
                return@launch
            }
            val reviews = userRepository.getReviewsByUserId(id)
            _uiState.update {
                it.copy(
                    user = user.getOrNull(),
                    reviews = reviews.getOrNull().orEmpty(),
                    isLoading = false,
                    errorMessage = reviews.exceptionOrNull()?.message
                )
            }
        }
    }
}
