package com.example.wayspot.ui.screens.auth.login.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.R
import com.example.wayspot.ui.screens.auth.components.AuthSwitchPrompt

@Composable
internal fun LoginActionsSection(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoginActionButton(onClick = onLoginClick, enabled = !isLoading)
        if (isLoading) { CircularProgressIndicator(modifier = Modifier.padding(vertical = 12.dp)) }
        if (errorMessage != null) { Text(text = errorMessage, color = MaterialTheme.colorScheme.error) }
        Spacer(modifier = Modifier.height(32.dp))
        AuthSwitchPrompt(
            prompt = stringResource(R.string.no_account_prompt),
            action = stringResource(R.string.register_action),
            onClick = onSignUpClick
        )
    }
}

@Composable
fun ForgotPasswordButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Text(text = stringResource(R.string.forgot_password))
    }
}

@Composable
fun LoginActionButton(
    onClick: () -> Unit,
    enabled: Boolean = true,
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
            text = stringResource(R.string.welcome_login_button),
            fontWeight = FontWeight.Bold
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ForgotPasswordPreview() {
    WayspotTheme {
        ForgotPasswordButton(
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun LoginActionButtonPreview() {
    WayspotTheme {
        LoginActionButton(
            onClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}

@WayspotMultiPreview
@Composable
private fun LoginActionsSectionPreview() {
    WayspotTheme {
        LoginActionsSection(
            onLoginClick = {},
            onSignUpClick = {},
            isLoading = false,
            errorMessage = null,
            modifier = Modifier.padding(16.dp)
        )
    }
}
