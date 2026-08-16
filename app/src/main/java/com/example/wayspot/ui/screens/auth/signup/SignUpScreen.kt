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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.auth.components.AuthHeader
import com.example.wayspot.ui.screens.auth.components.AuthSwitchPrompt
import com.example.wayspot.ui.screens.auth.components.AuthTextField
import com.example.wayspot.ui.screens.auth.signup.components.PasswordStrengthIndicator
import com.example.wayspot.ui.screens.auth.signup.components.PasswordVisibilityButton
import com.example.wayspot.ui.screens.auth.signup.components.SignUpActionButton
import com.example.wayspot.ui.screens.auth.signup.components.TermsAcceptance
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun SignUpScreen(
    onSignUpClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var confirmarContrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }
    var termsAccepted by remember { mutableStateOf(false) }

    SignUpContent(
        nombre = nombre,
        onNombreChange = { nombre = it },
        correo = correo,
        onCorreoChange = { correo = it },
        contrasena = contrasena,
        onContrasenaChange = { contrasena = it },
        confirmarContrasena = confirmarContrasena,
        onConfirmarContrasenaChange = { confirmarContrasena = it },
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
        confirmPasswordVisible = confirmPasswordVisible,
        onToggleConfirmPasswordVisibility = {
            confirmPasswordVisible = !confirmPasswordVisible
        },
        termsAccepted = termsAccepted,
        onTermsAcceptedChange = { termsAccepted = it },
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

        AuthTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = stringResource(R.string.username_label),
            placeholder = stringResource(R.string.username_placeholder),
            leadingIcon = Icons.Outlined.Person,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthTextField(
            value = correo,
            onValueChange = onCorreoChange,
            label = stringResource(R.string.label_email),
            placeholder = stringResource(R.string.email_placeholder),
            leadingIcon = Icons.Outlined.Email,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthTextField(
            value = contrasena,
            onValueChange = onContrasenaChange,
            label = stringResource(R.string.label_password),
            placeholder = stringResource(R.string.password_minimum_placeholder),
            leadingIcon = Icons.Outlined.Lock,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),
            trailingIcon = {
                PasswordVisibilityButton(
                    visible = passwordVisible,
                    onClick = onTogglePasswordVisibility
                )
            }
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthTextField(
            value = confirmarContrasena,
            onValueChange = onConfirmarContrasenaChange,
            label = stringResource(R.string.confirm_password_label),
            placeholder = stringResource(R.string.confirm_password_placeholder),
            leadingIcon = Icons.Outlined.VerifiedUser,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (confirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                PasswordVisibilityButton(
                    visible = confirmPasswordVisible,
                    onClick = onToggleConfirmPasswordVisibility
                )
            },
            isError = passwordsDoNotMatch,
            supportingText = if (passwordsDoNotMatch) {
                stringResource(R.string.passwords_do_not_match)
            } else {
                null
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordStrengthIndicator(
            password = contrasena,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TermsAcceptance(
            checked = termsAccepted,
            onCheckedChange = onTermsAcceptedChange,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(18.dp))

        SignUpActionButton(
            onClick = onSignUpClick,
            enabled = termsAccepted && !passwordsDoNotMatch
        )

        Spacer(modifier = Modifier.height(18.dp))

        AuthSwitchPrompt(
            prompt = stringResource(R.string.already_have_account_prompt),
            action = stringResource(R.string.login_action),
            onClick = onBackToLoginClick
        )

        Spacer(modifier = Modifier.height(12.dp))
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
