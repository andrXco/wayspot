package com.example.wayspot.ui.screens.auth.login.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme
import com.example.wayspot.R
import com.example.wayspot.ui.screens.auth.components.AuthSwitchPrompt

@Composable
internal fun LoginActionsSection(
    onLoginClick: () -> Unit,
    onGoogleClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
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

@Composable
fun SocialLoginSection(
    onGoogleClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            HorizontalDivider(modifier = Modifier.weight(1f))
            Text(
                text = stringResource(R.string.or_continue_with),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
            HorizontalDivider(modifier = Modifier.weight(1f))
        }

        OutlinedButton(
            onClick = onGoogleClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = RoundedCornerShape(14.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.icon_google),
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = stringResource(R.string.continue_with_google),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
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
private fun SocialLoginSectionPreview() {
    WayspotTheme {
        SocialLoginSection(
            onGoogleClick = {},
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
            onGoogleClick = {},
            onSignUpClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
