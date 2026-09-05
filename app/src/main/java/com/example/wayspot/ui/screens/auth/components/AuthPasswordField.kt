package com.example.wayspot.ui.screens.auth.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
internal fun AuthPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: ImageVector = Icons.Outlined.Lock,
    imeAction: ImeAction = ImeAction.Done,
    isError: Boolean = false,
    supportingText: String? = null
) {
    AuthTextField(
        value = value,
        onValueChange = onValueChange,
        label = label,
        placeholder = placeholder,
        leadingIcon = leadingIcon,
        modifier = modifier,
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
        ),
        trailingIcon = {
            PasswordVisibilityButton(
                visible = passwordVisible,
                onClick = onTogglePasswordVisibility
            )
        },
        isError = isError,
        supportingText = supportingText
    )
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

@WayspotMultiPreview
@Composable
private fun AuthPasswordFieldPreview() {
    WayspotTheme {
        AuthPasswordField(
            value = stringResource(R.string.password123),
            onValueChange = {},
            label = stringResource(R.string.contrase_a),
            placeholder = stringResource(R.string.introduce_tu_contrase_a),
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
