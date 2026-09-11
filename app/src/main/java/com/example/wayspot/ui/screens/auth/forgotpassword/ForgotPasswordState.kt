package com.example.wayspot.ui.screens.auth.forgotpassword

import com.example.wayspot.data.model.AuthFailure

data class  ForgotPasswordState(
    val email: String = "",
    val isLoading: Boolean = false,
    val failure: AuthFailure? = null,
    val isSent: Boolean = false
)
