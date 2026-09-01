package com.example.wayspot.ui.screens.auth.signup

data class SignUpState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val termsAccepted: Boolean = false
)