package com.example.wayspot.ui.screens.auth.signup

/** Estado inmutable del formulario de registro, incluida la aceptación de términos. */
data class SignUpState(
    val nombre: String = "",
    val correo: String = "",
    val contrasena: String = "",
    val confirmarContrasena: String = "",
    val passwordVisible: Boolean = false,
    val confirmPasswordVisible: Boolean = false,
    val termsAccepted: Boolean = false
)
