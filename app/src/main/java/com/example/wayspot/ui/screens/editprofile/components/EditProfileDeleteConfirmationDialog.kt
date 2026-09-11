package com.example.wayspot.ui.screens.editprofile.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R

@Composable
fun EditProfileDeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        title = {
            Text(
                text = stringResource(
                    R.string.edit_profile_delete_account
                )
            )
        },
        text = {
            androidx.compose.foundation.layout.Column {
                Text(text = stringResource(R.string.edit_profile_delete_confirmation))
                OutlinedTextField(value = password, onValueChange = onPasswordChange, label = { Text(stringResource(R.string.contrase_a)) })
                if (errorMessage != null) Text(text = errorMessage, color = androidx.compose.material3.MaterialTheme.colorScheme.error)
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
                , enabled = password.isNotBlank() && !isLoading
            ) {
                Text(
                    text = stringResource(
                        R.string.edit_profile_delete_account
                    )
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismissRequest
            ) {
                Text(
                    text = stringResource(
                        R.string.edit_profile_cancel
                    )
                )
            }
        },
        modifier = modifier
    )
}
