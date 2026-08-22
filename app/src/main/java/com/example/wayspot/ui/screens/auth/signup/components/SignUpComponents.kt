package com.example.wayspot.ui.screens.auth.signup.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.auth.components.AuthSwitchPrompt
import com.example.wayspot.ui.theme.ArenaDorado
import com.example.wayspot.ui.theme.Terracota
import com.example.wayspot.ui.theme.VerdeBosque
import com.example.wayspot.ui.theme.VerdeSalvia
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
internal fun SignUpActionsSection(
    termsAccepted: Boolean,
    onTermsAcceptedChange: (Boolean) -> Unit,
    signUpEnabled: Boolean,
    onSignUpClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TermsAcceptance(
            checked = termsAccepted,
            onCheckedChange = onTermsAcceptedChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(18.dp))
        SignUpActionButton(
            onClick = onSignUpClick,
            enabled = signUpEnabled
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

@Composable
fun PasswordStrengthIndicator(
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
fun TermsAcceptance(
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

@Composable
fun SignUpActionButton(
    onClick: () -> Unit,
    enabled: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(54.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = stringResource(R.string.welcome_signup_button),
            fontWeight = FontWeight.Bold
        )
    }
}

// Previews

@WayspotMultiPreview
@Composable
private fun PasswordStrengthIndicatorPreview() {
    WayspotTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PasswordStrengthIndicator(password = "")
            PasswordStrengthIndicator(password = "weak")
            PasswordStrengthIndicator(password = "Strong123")
        }
    }
}

@WayspotMultiPreview
@Composable
private fun TermsAcceptancePreview() {
    WayspotTheme {
        TermsAcceptance(
            checked = true, 
            onCheckedChange = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun SignUpActionButtonPreview() {
    WayspotTheme {
        SignUpActionButton(
            onClick = {}, 
            enabled = true,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun SignUpActionsSectionPreview() {
    WayspotTheme {
        SignUpActionsSection(
            termsAccepted = true,
            onTermsAcceptedChange = {},
            signUpEnabled = true,
            onSignUpClick = {},
            onBackToLoginClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
