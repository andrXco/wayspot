package com.example.wayspot.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())

    val uiState: StateFlow<LoginState> = _uiState

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _mostrarMensajeError = MutableStateFlow(false)
    val mostrarMensajeError: StateFlow<Boolean> = _mostrarMensajeError.asStateFlow()

    fun updateUsuario(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                usuario = input
            )
        }
    }

    fun updateContrasena(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                contrasena = input
            )
        }
    }

    fun togglePasswordVisibility() {
        val valorActual = _uiState.value.passwordVisible

        _uiState.update { currentState ->
            currentState.copy(
                passwordVisible = !valorActual
            )
        }
    }

    fun signIn() {
        viewModelScope.launch {
            try {
                authRepository.signIn(
                    email = _uiState.value.usuario.trim(),
                    password = _uiState.value.contrasena
                )
                _navigateToHome.value = true
            } catch (exception: Exception) {
                _errorMessage.value = exception.localizedMessage.orEmpty()
                _mostrarMensajeError.value = true
            }
        }
    }

    fun onNavigationHandled() {
        _navigateToHome.value = false
    }

    fun dismissError() {
        _mostrarMensajeError.value = false
        _errorMessage.value = ""
    }
}
