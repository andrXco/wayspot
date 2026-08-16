package com.example.wayspot.navigation

import androidx.compose.runtime.*
import com.example.wayspot.ui.screens.auth.LoginScreen
import com.example.wayspot.ui.screens.auth.SignUpScreen
import com.example.wayspot.ui.screens.home.HomeScreen
import com.example.wayspot.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    var currentRoute by remember { mutableStateOf(Routes.SPLASH) }

    when (currentRoute) {
        Routes.SPLASH -> {
            SplashScreen(onComenzarClick = { currentRoute = Routes.LOGIN })
        }
        Routes.LOGIN -> {
            LoginScreen(
                onLoginClick = { currentRoute = Routes.HOME },
                onSignUpClick = { currentRoute = Routes.SIGNUP }
            )
        }
        Routes.SIGNUP -> {
            SignUpScreen(
                onSignUpClick = { currentRoute = Routes.LOGIN },
                onBackToLoginClick = { currentRoute = Routes.LOGIN }
            )
        }
        Routes.HOME -> {
            HomeScreen()
        }
    }
}
