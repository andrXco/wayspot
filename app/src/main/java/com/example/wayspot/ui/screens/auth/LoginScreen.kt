package com.example.wayspot.ui.screens.auth

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.components.LogoApp
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LoginContent(
        usuario = usuario,
        onUsuarioChange = { usuario = it },
        contrasena = contrasena,
        onContrasenaChange = { contrasena = it },
        passwordVisible = passwordVisible,
        onTogglePasswordVisibility = { passwordVisible = !passwordVisible },
        onLoginClick = onLoginClick,
        onSignUpClick = onSignUpClick,
        modifier = modifier
    )
}

@Composable
fun LoginContent(
    usuario: String,
    onUsuarioChange: (String) -> Unit,
    contrasena: String,
    onContrasenaChange: (String) -> Unit,
    passwordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
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
            text = stringResource(R.string.login_title),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = usuario,
            onValueChange = onUsuarioChange,
            label = { Text(stringResource(R.string.label_user)) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = onContrasenaChange,
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier.fillMaxWidth(0.8f),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = onTogglePasswordVisibility) {
                    Icon(
                        painter = painterResource(id = if (passwordVisible) R.drawable.hidden else R.drawable.view),
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Mostrar contraseña",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onLoginClick,
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(stringResource(R.string.button_login))
        }

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onSignUpClick) {
            Text(stringResource(R.string.button_no_account))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    WayspotTheme {
        LoginContent(
            usuario = "",
            onUsuarioChange = {},
            contrasena = "",
            onContrasenaChange = {},
            passwordVisible = false,
            onTogglePasswordVisibility = {},
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}
