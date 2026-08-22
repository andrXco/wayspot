package com.example.wayspot.ui.screens.auth.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.ui.screens.auth.components.AuthPasswordField
import com.example.wayspot.ui.screens.auth.components.AuthTextField

@Composable
internal fun LoginFormSection(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AuthTextField(
            value = email,
            onValueChange = onEmailChange,
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

        AuthPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.label_password),
            placeholder = stringResource(R.string.password_placeholder),
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            modifier = Modifier.fillMaxWidth()
        )

        ForgotPasswordButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun LoginFormSectionPreview() {
    WayspotTheme {
        LoginFormSection(
            email = "",
            onEmailChange = {},
            password = "",
            onPasswordChange = {},
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onForgotPasswordClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
