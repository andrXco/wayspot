package com.example.wayspot.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.auth.components.AuthHeader
import com.example.wayspot.ui.screens.auth.components.AuthSwitchPrompt
import com.example.wayspot.ui.screens.auth.components.AuthTextField
import com.example.wayspot.ui.theme.ArenaDorado
import com.example.wayspot.ui.theme.Terracota
import com.example.wayspot.ui.theme.VerdeBosque
import com.example.wayspot.ui.theme.VerdeSalvia
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

        Button(
            onClick = onSignUpClick,
            enabled = termsAccepted && !passwordsDoNotMatch,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.welcome_signup_button),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        AuthSwitchPrompt(
            prompt = stringResource(R.string.already_have_account_prompt),
            action = stringResource(R.string.login_action),
            onClick = onBackToLoginClick
        )

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun PasswordVisibilityButton(
    visible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            painter = painterResource(if (visible) R.drawable.hidden else R.drawable.view),
            contentDescription = stringResource(
                if (visible) {
                    R.string.hide_password_content_description
                } else {
                    R.string.show_password_content_description
                }
            ),
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun PasswordStrengthIndicator(
    password: String,
    modifier: Modifier = Modifier
) {
    val strength = when {
        password.isEmpty() -> 0
        password.length < 8 -> 1
        password.length < 10 -> 2
        password.any(Char::isDigit) && password.any(Char::isUpperCase) -> 4
        else -> 3
    }
    val strengthColor = when (strength) {
        1 -> Terracota
        2 -> ArenaDorado
        3 -> VerdeSalvia
        4 -> VerdeBosque
        else -> MaterialTheme.colorScheme.outlineVariant
    }
    val strengthLabel = when (strength) {
        1 -> R.string.password_strength_low
        2 -> R.string.password_strength_medium
        3 -> R.string.password_strength_high
        4 -> R.string.password_strength_very_high
        else -> R.string.password_strength_not_evaluated
    }

    Column(modifier = modifier) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            repeat(4) { index ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(
                            color = if (index < strength) {
                                strengthColor
                            } else {
                                MaterialTheme.colorScheme.outlineVariant
                            },
                            shape = CircleShape
                        )
                )
            }
        }
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = stringResource(R.string.password_strength, stringResource(strengthLabel)),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun TermsAcceptance(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val linkColor = MaterialTheme.colorScheme.primary
    val termsText = buildAnnotatedString {
        append(stringResource(R.string.terms_acceptance_prefix))
        withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.Medium)) {
            append(stringResource(R.string.terms_of_service))
        }
        append(stringResource(R.string.terms_acceptance_and))
        withStyle(SpanStyle(color = linkColor, fontWeight = FontWeight.Medium)) {
            append(stringResource(R.string.privacy_policy))
        }
    }

    Row(modifier = modifier, verticalAlignment = Alignment.Top) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        Text(
            text = termsText,
            modifier = Modifier.padding(top = 12.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
            lineHeight = 17.sp
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
