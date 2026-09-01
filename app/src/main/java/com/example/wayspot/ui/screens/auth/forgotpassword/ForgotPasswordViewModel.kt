package com.example.wayspot.ui.screens.auth.forgotpassword

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

class ForgotPasswordViewModel : ViewModel() {

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