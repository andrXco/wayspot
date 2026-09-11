package com.example.wayspot.ui.screens.auth.login

import com.example.wayspot.data.model.AuthFailure

data class LoginState(
    val usuario: String = "",
    val contrasena: String = "",
    val passwordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val failure: AuthFailure? = null
)
