    package com.example.wayspot.ui.screens.auth.signup

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
    import androidx.compose.runtime.getValue
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.res.stringResource
    import androidx.compose.ui.unit.dp
    import com.example.wayspot.R
    import com.example.wayspot.ui.preview.WayspotMultiPreview
    import com.example.wayspot.ui.screens.auth.components.AuthHeader
    import com.example.wayspot.ui.screens.auth.signup.components.SignUpActionsSection
    import com.example.wayspot.ui.screens.auth.signup.components.SignUpFormSection
    import com.example.wayspot.ui.theme.WayspotTheme
    import androidx.compose.runtime.collectAsState

    @Composable
    fun SignUpScreen(
        signUpViewModel: SignUpViewModel,
        onSignUpClick: () -> Unit,
        onBackToLoginClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val state by signUpViewModel.uiState.collectAsState()

        SignUpContent(
            nombre = state.nombre,
            onNombreChange = { signUpViewModel.updateNombre(it) },
            correo = state.correo,
            onCorreoChange = { signUpViewModel.updateCorreo(it) },
            contrasena = state.contrasena,
            onContrasenaChange = { signUpViewModel.updateContrasena(it) },
            confirmarContrasena = state.confirmarContrasena,
            onConfirmarContrasenaChange = {
                signUpViewModel.updateConfirmarContrasena(it)
            },
            passwordVisible = state.passwordVisible,
            onTogglePasswordVisibility = {
                signUpViewModel.togglePasswordVisibility()
            },
            confirmPasswordVisible = state.confirmPasswordVisible,
            onToggleConfirmPasswordVisibility = {
                signUpViewModel.toggleConfirmPasswordVisibility()
            },
            termsAccepted = state.termsAccepted,
            onTermsAcceptedChange = {
                signUpViewModel.updateTermsAccepted(it)
            },
            onSignUpClick = onSignUpClick,
            onBackToLoginClick = onBackToLoginClick,
            modifier = modifier
        )
    }

    @Composable
    fun SignUpContent(
        nombre: String,
        onNombreChange: (String) -> Unit,
        correo: String,
        onCorreoChange: (String) -> Unit,
        contrasena: String,
        onContrasenaChange: (String) -> Unit,
        confirmarContrasena: String,
        onConfirmarContrasenaChange: (String) -> Unit,
        passwordVisible: Boolean,
        onTogglePasswordVisibility: () -> Unit,
        confirmPasswordVisible: Boolean,
        onToggleConfirmPasswordVisibility: () -> Unit,
        termsAccepted: Boolean,
        onTermsAcceptedChange: (Boolean) -> Unit,
        onSignUpClick: () -> Unit,
        onBackToLoginClick: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val passwordsDoNotMatch = confirmarContrasena.isNotEmpty() &&
            contrasena != confirmarContrasena

        Column(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
                .imePadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthHeader(
                title = stringResource(R.string.signup_title),
                subtitle = stringResource(R.string.auth_signup_tagline)
            )

            Spacer(modifier = Modifier.height(26.dp))

            SignUpFormSection(
                username = nombre,
                onUsernameChange = onNombreChange,
                email = correo,
                onEmailChange = onCorreoChange,
                password = contrasena,
                onPasswordChange = onContrasenaChange,
                confirmPassword = confirmarContrasena,
                onConfirmPasswordChange = onConfirmarContrasenaChange,
                passwordVisible = passwordVisible,
                onTogglePasswordVisibility = onTogglePasswordVisibility,
                confirmPasswordVisible = confirmPasswordVisible,
                onToggleConfirmPasswordVisibility = onToggleConfirmPasswordVisibility,
                passwordsDoNotMatch = passwordsDoNotMatch,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            SignUpActionsSection(
                termsAccepted = termsAccepted,
                onTermsAcceptedChange = onTermsAcceptedChange,
                signUpEnabled = termsAccepted && !passwordsDoNotMatch,
                onSignUpClick = onSignUpClick,
                onBackToLoginClick = onBackToLoginClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }

    @WayspotMultiPreview
    @Composable
    private fun SignUpScreenPreview() {
        WayspotTheme {
            SignUpContent(
                nombre = "",
                onNombreChange = {},
                correo = "",
                onCorreoChange = {},
                contrasena = "Wayspot123",
                onContrasenaChange = {},
                confirmarContrasena = "Wayspot123",
                onConfirmarContrasenaChange = {},
                passwordVisible = false,
                onTogglePasswordVisibility = {},
                confirmPasswordVisible = false,
                onToggleConfirmPasswordVisibility = {},
                termsAccepted = true,
                onTermsAcceptedChange = {},
                onSignUpClick = {},
                onBackToLoginClick = {}
            )
        }
    }
