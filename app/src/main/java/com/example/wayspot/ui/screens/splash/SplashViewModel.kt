package com.example.wayspot.ui.screens.splash

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SplashViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SplashState())

    val uiState: StateFlow<SplashState> = _uiState
}