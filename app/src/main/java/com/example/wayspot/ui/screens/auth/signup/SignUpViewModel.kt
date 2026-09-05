package com.example.wayspot.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update



class SignUpViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())

    val uiState: StateFlow<SignUpState> = _uiState

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
}