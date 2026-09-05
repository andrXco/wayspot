package com.example.wayspot.ui.screens.editprofile.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wayspot.R

@Composable
fun EditProfileDeleteConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmClick: () -> Unit,
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
            Text(
                text = stringResource(
                    R.string.edit_profile_delete_confirmation
                )
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmClick
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
