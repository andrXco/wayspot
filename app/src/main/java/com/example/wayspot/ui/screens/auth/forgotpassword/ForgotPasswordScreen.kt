package com.example.wayspot.ui.screens.auth.forgotpassword

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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.auth.forgotpassword.components.ForgotPasswordActionsSection
import com.example.wayspot.ui.screens.auth.forgotpassword.components.ForgotPasswordFormSection
import com.example.wayspot.ui.screens.auth.forgotpassword.components.ForgotPasswordHeader
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun ForgotPasswordScreen(
    onSendClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember {
        mutableStateOf("")
    }

    ForgotPasswordContent(
        email = email,
        onEmailChange = { email = it },
        onSendClick = onSendClick,
        onBackToLoginClick = onBackToLoginClick,
        modifier = modifier
    )
}

@Composable
fun ForgotPasswordContent(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .imePadding()
            .padding(
                horizontal = 24.dp,
                vertical = 20.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(
            modifier = Modifier.height(30.dp)
        )

        ForgotPasswordHeader(
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        ForgotPasswordFormSection(
            email = email,
            onEmailChange = onEmailChange,
            onSendClick = onSendClick,
            modifier = Modifier.fillMaxWidth()
        )

        ForgotPasswordActionsSection(
            onBackToLoginClick = onBackToLoginClick,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@WayspotMultiPreview
@Composable
private fun ForgotPasswordPreview() {
    WayspotTheme {
        ForgotPasswordContent(
            email = "",
            onEmailChange = {},
            onSendClick = {},
            onBackToLoginClick = {}
        )
    }
}