package com.example.wayspot.ui.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.auth.components.AuthHeader
import com.example.wayspot.ui.screens.auth.login.components.LoginActionsSection
import com.example.wayspot.ui.screens.auth.login.components.LoginFormSection
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun LoginScreen(
    loginViewModel: LoginViewModel,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    onForgotPasswordClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {}
) {
    val state by loginViewModel.uiState.collectAsState()

    LoginContent(
        usuario = state.usuario,
        onUsuarioChange = {
            loginViewModel.updateUsuario(it)
        },
        contrasena = state.contrasena,
        onContrasenaChange = {
            loginViewModel.updateContrasena(it)
        },
        passwordVisible = state.passwordVisible,
        onTogglePasswordVisibility = {
            loginViewModel.togglePasswordVisibility()
        },
        onLoginClick = onLoginClick,
        onSignUpClick = onSignUpClick,
        onForgotPasswordClick = onForgotPasswordClick,
        onGoogleClick = onGoogleClick,
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    usuario: String,
    onUsuarioChange: (String) -> Unit,
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthHeader(
            title = stringResource(R.string.app_title),
            subtitle = stringResource(R.string.auth_login_tagline)
        )

        Spacer(modifier = Modifier.height(24.dp))

        LoginFormSection(
            email = usuario,
            onEmailChange = onUsuarioChange,
            password = contrasena,
            onPasswordChange = onContrasenaChange,
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            onForgotPasswordClick = onForgotPasswordClick,
            modifier = Modifier.fillMaxWidth(),
        )

        LoginActionsSection(
            onLoginClick = onLoginClick,
            onGoogleClick = onGoogleClick,
            onSignUpClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@WayspotMultiPreview
@Composable
private fun LoginPreview() {
    WayspotTheme {
        LoginContent(
            usuario = "",
            onUsuarioChange = {},
            contrasena = "",
            onContrasenaChange = {},
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onSignUpClick = {},
            onForgotPasswordClick = {},
            onGoogleClick = {}
        )
    }
}