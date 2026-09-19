package com.example.wayspot.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import com.example.wayspot.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch



/**
 * Gestiona el formulario de registro y solicita la creación de la cuenta mediante el repositorio.
 *
 * Publica navegación y error como eventos separados para que la pantalla los consuma y restablezca.
 */
@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())

    val uiState: StateFlow<SignUpState> = _uiState

    private val _navigateToHome = MutableStateFlow(false)
    val navigateToHome: StateFlow<Boolean> = _navigateToHome.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage: StateFlow<String> = _errorMessage.asStateFlow()

    private val _mostrarMensajeError = MutableStateFlow(false)
    val mostrarMensajeError: StateFlow<Boolean> = _mostrarMensajeError.asStateFlow()

    fun updateNombre(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                nombre = input
            )
        }
    }

    fun updateCorreo(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                correo = input
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

    fun updateConfirmarContrasena(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                confirmarContrasena = input
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

    fun toggleConfirmPasswordVisibility() {
        val valorActual = _uiState.value.confirmPasswordVisible

        _uiState.update { currentState ->
            currentState.copy(
                confirmPasswordVisible = !valorActual
            )
        }
    }

    fun updateTermsAccepted(value: Boolean) {
        _uiState.update { currentState ->
            currentState.copy(
                termsAccepted = value
            )
        }
    }
    fun signUp() {
        viewModelScope.launch {
            try {
                authRepository.signUp(
                    email = _uiState.value.correo,
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
