package com.example.wayspot.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.components.LogoApp
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

    SignUpContent(
        nombre = nombre,
        onNombreChange = { nombre = it },
        correo = correo,
        onCorreoChange = { correo = it },
        contrasena = contrasena,
        onContrasenaChange = { contrasena = it },
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
    onSignUpClick: () -> Unit,
    onBackToLoginClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        LogoApp()
        
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.signup_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = onNombreChange,
            label = { Text(stringResource(R.string.label_full_name)) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = onCorreoChange,
            label = { Text(stringResource(R.string.label_email)) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = onContrasenaChange,
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(stringResource(R.string.button_signup))
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onBackToLoginClick) {
            Text(stringResource(R.string.button_already_have_account))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun SignUpScreenPreview() {
    WayspotTheme {
        SignUpContent(
            nombre = "",
            onNombreChange = {},
            correo = "",
            onCorreoChange = {},
            contrasena = "",
            onContrasenaChange = {},
            onSignUpClick = {},
            onBackToLoginClick = {}
        )
    }
}
