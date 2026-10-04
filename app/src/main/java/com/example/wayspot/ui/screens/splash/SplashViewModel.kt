package com.example.wayspot.ui.screens.splash

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/** Determina si existe una sesión autenticada y expone el evento de navegación resultante. */
@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())

    val uiState: StateFlow<SplashState> = _uiState

    private val _navigateHome = MutableStateFlow(false)
    val navigateHome: StateFlow<Boolean> = _navigateHome.asStateFlow()

    init {
        checkUser()
    }

    private fun checkUser() {
        _navigateHome.value = authRepository.currentUser != null
    }

    fun onNavigationHandled() {
        _navigateHome.value = false
    }
}
