package com.example.wayspot.ui.screens.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
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
import com.example.wayspot.ui.screens.auth.login.components.ForgotPasswordButton
import com.example.wayspot.ui.screens.auth.login.components.LoginActionButton
import com.example.wayspot.ui.screens.auth.login.components.SocialLoginSection
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    onForgotPasswordClick: () -> Unit = {},
    onGoogleClick: () -> Unit = {}
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LoginContent(
        usuario = usuario,
        onUsuarioChange = { usuario = it },
        contrasena = contrasena,
        onContrasenaChange = { contrasena = it },
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
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

        AuthTextField(
            value = usuario,
            onValueChange = onUsuarioChange,
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
            placeholder = stringResource(R.string.password_placeholder),
            leadingIcon = Icons.Outlined.Lock,
            modifier = Modifier.fillMaxWidth(),
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        painter = painterResource(
                            if (passwordVisible) R.drawable.hidden else R.drawable.view
                        ),
                        contentDescription = stringResource(
                            if (passwordVisible) {
                                R.string.hide_password_content_description
                            } else {
                                R.string.show_password_content_description
                            }
                        ),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }
        )

        ForgotPasswordButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier.align(Alignment.End)
        )

        LoginActionButton(onClick = onLoginClick)

        SocialLoginSection(onGoogleClick = onGoogleClick)

        Spacer(modifier = Modifier.height(32.dp))

        AuthSwitchPrompt(
            prompt = stringResource(R.string.no_account_prompt),
            action = stringResource(R.string.register_action),
            onClick = onSignUpClick
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
