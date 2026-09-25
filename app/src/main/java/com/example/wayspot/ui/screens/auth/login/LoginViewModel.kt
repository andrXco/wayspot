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

/**
 * Conserva el estado del formulario e inicia la autenticación con el repositorio.
 *
 * Los flujos de navegación y error son eventos de una sola lectura que la pantalla restablece
 * después de consumirlos.
 */
@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())

    val uiState: StateFlow<LoginState> = _uiState

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    private val _mostrarMensajeError = MutableStateFlow(false)
    val mostrarMensajeError: StateFlow<Boolean> = _mostrarMensajeError.asStateFlow()
    private val _mensajeError = MutableStateFlow("")
    val mensajeError: StateFlow<String> = _mensajeError.asStateFlow()

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
            val result = authRepository.signIn(
                email = _uiState.value.usuario.trim(),
                password = _uiState.value.contrasena
            )
            if (result.isSuccess) {
                _navigateToHome.value = true
            } else {
                _mensajeError.value =
                    result.exceptionOrNull()?.message ?: "Error al iniciar sesión"

                _mostrarMensajeError.value = true
            }
        }
    }

    fun onNavigationHandled() {
        _navigateToHome.value = false
    }

    fun dismissError() {
        _mostrarMensajeError.value = false
    }
}
