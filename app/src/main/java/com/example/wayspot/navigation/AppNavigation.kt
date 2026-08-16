package com.example.wayspot.navigation

import androidx.compose.runtime.*
import com.example.wayspot.model.Places
import com.example.wayspot.ui.screens.auth.LoginScreen
import com.example.wayspot.ui.screens.auth.SignUpScreen
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
            HomeScreen(
                currentRoute = homeRoute,
                onNavItemClick = { homeRoute = it },
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
