package com.example.wayspot.ui.screens.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wayspot.data.injection.IoDispatcher
import com.example.wayspot.data.model.AuthFailure
import com.example.wayspot.data.model.AuthOutcome
import com.example.wayspot.data.model.AuthRules
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
class SignUpViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpState())

    val uiState: StateFlow<SignUpState> = _uiState
    private val _events = MutableSharedFlow<SignUpEvent>()
    val events = _events

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
        val current = _uiState.value
        val validation = when {
            current.nombre.isBlank() || current.correo.isBlank() || current.contrasena.isBlank() || current.confirmarContrasena.isBlank() -> AuthFailure.EmptyFields
            !AuthRules.isValidUsername(current.nombre) -> AuthFailure.InvalidCredentials
            !AuthRules.isValidEmail(current.correo) -> AuthFailure.InvalidEmail
            !AuthRules.isValidPassword(current.contrasena) -> AuthFailure.InvalidPassword
            current.contrasena != current.confirmarContrasena -> AuthFailure.PasswordMismatch
            !current.termsAccepted -> AuthFailure.TermsNotAccepted
            else -> null
        }
        if (validation != null) { _uiState.update { it.copy(failure = validation) }; return }
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, failure = null) }
            when (val authOutcome = authRepository.signUp(current.correo, current.contrasena)) {
                is AuthOutcome.Failure -> showFailure(authOutcome.reason)
                is AuthOutcome.Success -> completeRegistration(
                    username = current.nombre
                )
            }
        }
    }

    private suspend fun completeRegistration(username: String) {
        when (val nameOutcome = authRepository.updateDisplayName(username)) {
            is AuthOutcome.Failure -> showFailure(nameOutcome.reason)
            is AuthOutcome.Success -> {
                _uiState.update { it.copy(isLoading = false) }
                _events.emit(SignUpEvent.NavigateHome)
            }
        }
    }

    private fun showFailure(failure: AuthFailure) {
        _uiState.update { it.copy(isLoading = false, failure = failure) }
    }
}

sealed interface SignUpEvent { data object NavigateHome : SignUpEvent }
