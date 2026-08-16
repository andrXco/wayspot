package com.example.wayspot.ui.screens.auth.signup.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.screens.auth.components.AuthPasswordField
import com.example.wayspot.ui.screens.auth.components.AuthTextField

@Composable
internal fun SignUpFormSection(
    username: String,
    onUsernameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    confirmPasswordVisible: Boolean,
    onToggleConfirmPasswordVisibility: () -> Unit,
    passwordsDoNotMatch: Boolean,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        AuthTextField(
            value = username,
            onValueChange = onUsernameChange,
            label = stringResource(R.string.username_label),
            placeholder = stringResource(R.string.username_placeholder),
            leadingIcon = Icons.Outlined.Person,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
        )

        FormFieldSpacer()

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

        FormFieldSpacer()

        AuthPasswordField(
            value = password,
            onValueChange = onPasswordChange,
            label = stringResource(R.string.label_password),
            placeholder = stringResource(R.string.password_minimum_placeholder),
            passwordVisible = passwordVisible,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            modifier = Modifier.fillMaxWidth(),
            imeAction = ImeAction.Next
        )

        FormFieldSpacer()

        AuthPasswordField(
            value = confirmPassword,
            onValueChange = onConfirmPasswordChange,
            label = stringResource(R.string.confirm_password_label),
            placeholder = stringResource(R.string.confirm_password_placeholder),
            passwordVisible = confirmPasswordVisible,
            onTogglePasswordVisibility = onToggleConfirmPasswordVisibility,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = Icons.Outlined.VerifiedUser,
            isError = passwordsDoNotMatch,
            supportingText = if (passwordsDoNotMatch) {
                stringResource(R.string.passwords_do_not_match)
            } else {
                null
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        PasswordStrengthIndicator(
            password = password,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun FormFieldSpacer() {
    Spacer(modifier = Modifier.height(14.dp))
}
