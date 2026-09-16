package com.example.wayspot.ui.screens.auth.login.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R

@Composable
internal fun LoginErrorDialog(
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(R.string.auth_login_error_title)
            )
        },
        text = {
            Text(
                text = stringResource(R.string.auth_login_invalid_credentials)
            )
        },
        confirmButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = stringResource(R.string.auth_error_dismiss)
                )
            }
        },
        modifier = modifier
    )
}
