package com.example.wayspot.ui.screens.auth.login

/** Estado inmutable del formulario de inicio de sesión y de su control visual de contraseña. */
data class LoginState(
    val usuario: String = "",
    val contrasena: String = "",
    val passwordVisible: Boolean = false
)
