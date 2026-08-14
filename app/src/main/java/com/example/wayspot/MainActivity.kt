package com.example.wayspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.wayspot.ui.BodyScreen
import com.example.wayspot.ui.LoginScreen
import com.example.wayspot.ui.SignUpScreen
import com.example.wayspot.ui.theme.WayspotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WayspotTheme {
                // Empezamos en la pantalla "home"
                var pantallaActual by remember { mutableStateOf("home") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        when (pantallaActual) {
                            "home" -> {
                                BodyScreen(onComenzarClick = { pantallaActual = "login" })
                            }
                            "login" -> {
                                LoginScreen(
                                    onLoginClick = { pantallaActual = "home" },
                                    onSignUpClick = { pantallaActual = "signup" }
                                )
                            }
                            "signup" -> {
                                SignUpScreen(
                                    onSignUpClick = { pantallaActual = "login" },
                                    onBackToLoginClick = { pantallaActual = "login" }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
