package com.example.wayspot.ui.screens.auth.forgotpassword

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordState())
    val uiState: StateFlow<ForgotPasswordState> = _uiState

    fun updateEmail(input: String) {
        _uiState.update { currentState ->
            currentState.copy(
                email = input, failure = null, isSent = false
            )
        }
    }

    fun sendReset() {
        val email = _uiState.value.email
        if (!AuthRules.isValidEmail(email)) { _uiState.update { it.copy(failure = AuthFailure.InvalidEmail) }; return }
        viewModelScope.launch(ioDispatcher) {
            _uiState.update { it.copy(isLoading = true, failure = null) }
            val outcome = authRepository.sendPasswordReset(email)
            _uiState.update {
                when (outcome) {
                    is AuthOutcome.Success -> it.copy(isLoading = false, isSent = true)
                    is AuthOutcome.Failure -> it.copy(isLoading = false, failure = outcome.reason)
                }
            }
        }
    }
}
