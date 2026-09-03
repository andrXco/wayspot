package com.example.wayspot.ui.screens.auth.forgotpassword.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.screens.auth.components.AuthTextField

@Composable
fun ForgotPasswordFormSection(
    email: String,
    onEmailChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        AuthTextField(
            value = email,
            onValueChange = onEmailChange,
            label = stringResource(R.string.correo_electr_nico),
            placeholder = stringResource(R.string.tu_correo_com),
            leadingIcon = Icons.Outlined.Email,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            )
        )

        Spacer(
            modifier = Modifier.height(18.dp)
        )

        Button(
            onClick = onSendClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp),
            shape = MaterialTheme.shapes.medium
        ) {
            Text(
                text = stringResource(R.string.enviar_enlace_de_recuperaci_n),
                fontWeight = FontWeight.Bold
            )
        }
    }
}