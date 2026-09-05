package com.example.wayspot.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())

    val uiState: StateFlow<LoginState> = _uiState

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
}