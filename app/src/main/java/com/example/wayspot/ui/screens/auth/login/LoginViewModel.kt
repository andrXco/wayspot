package com.example.wayspot.ui.screens.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.model.AuthFailure
import com.example.wayspot.data.model.AuthOutcome
import com.example.wayspot.data.model.AuthRules
import com.example.wayspot.data.injection.IoDispatcher
import com.example.wayspot.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginState())

    val uiState: StateFlow<LoginState> = _uiState
    private val _events = MutableSharedFlow<LoginEvent>()
    val events = _events

    fun updateUsuario(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                usuario = input, failure = null
            )
        }
    }

    fun updateContrasena(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                contrasena = input, failure = null
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
        val current = _uiState.value
        val validation = when {
            current.usuario.isBlank() || current.contrasena.isBlank() -> AuthFailure.EmptyFields
            !AuthRules.isValidEmail(current.usuario) -> AuthFailure.InvalidEmail
            else -> null
        }
        if (validation != null) {
            _uiState.update { it.copy(failure = validation) }
            return
        }
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, failure = null) }
            when (val outcome = authRepository.signIn(current.usuario, current.contrasena)) {
                is AuthOutcome.Failure -> _uiState.update { it.copy(isLoading = false, failure = outcome.reason) }
                is AuthOutcome.Success -> {
                    _uiState.update { it.copy(isLoading = false) }
                    _events.emit(LoginEvent.NavigateHome)
                }
            }
        }
    }
}

sealed interface LoginEvent {
    data object NavigateHome : LoginEvent
}
