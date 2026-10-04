package com.example.wayspot.ui.screens.auth.forgotpassword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/** Conserva el correo de recuperación; el envío se delega mediante una función de devolución de llamada de pantalla. */
@HiltViewModel
class ForgotPasswordViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState

    fun updateEmail(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = input
            )
        }
    }
}
