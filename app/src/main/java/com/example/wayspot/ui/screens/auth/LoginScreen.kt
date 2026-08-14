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
fun LoginScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
            onValueChange = { usuario = it },
            label = { Text(stringResource(R.string.label_user)) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = { Text(stringResource(R.string.label_password)) },
            modifier = Modifier.fillMaxWidth(0.8f)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { onLoginClick() },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            Text(stringResource(R.string.button_login))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { onSignUpClick() }) {
            Text(stringResource(R.string.button_no_account))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginPreview() {
    WayspotTheme {
        LoginScreen(onLoginClick = {}, onSignUpClick = {})
    }
}
