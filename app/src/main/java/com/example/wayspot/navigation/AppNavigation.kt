package com.example.wayspot.navigation

import androidx.compose.runtime.*
import com.example.wayspot.model.Places
import com.example.wayspot.ui.screens.auth.login.LoginScreen
import com.example.wayspot.ui.screens.auth.signup.SignUpScreen
import com.example.wayspot.ui.screens.home.HomeScreen
import com.example.wayspot.ui.screens.placedetail.PlaceDetailScreen
import com.example.wayspot.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation() {
    var currentRoute by remember { mutableStateOf(Routes.SPLASH) }
    var homeRoute by remember { mutableStateOf(Routes.HOME) }
    var selectedPlace by remember { mutableStateOf<Places?>(null) }

    when (currentRoute) {
        Routes.SPLASH -> {
            SplashScreen(
                onLoginClick = { currentRoute = Routes.LOGIN },
                onSignUpClick = { currentRoute = Routes.SIGNUP }
            )
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
            HomeScreen(
                currentRoute = homeRoute,
                onPlaceClick = { place ->
                    selectedPlace = place
                    currentRoute = Routes.PLACE_DETAIL
                }
            )
        }
        Routes.PLACE_DETAIL -> {
            selectedPlace?.let { place ->
                PlaceDetailScreen(
                    place = place,
                    onBackClick = { currentRoute = Routes.HOME }
                )
            }
        }
    }
}
