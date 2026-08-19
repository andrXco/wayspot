package com.example.wayspot.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.wayspot.model.Places
import com.example.wayspot.ui.screens.auth.login.LoginScreen
import com.example.wayspot.ui.screens.auth.signup.SignUpScreen
import com.example.wayspot.ui.screens.home.HomeScreen
import com.example.wayspot.ui.screens.notifications.NotificationsScreen
import com.example.wayspot.ui.screens.placedetail.PlaceDetailScreen
import com.example.wayspot.ui.screens.splash.SplashScreen

@Composable
fun AppNavigation(
    modifier: Modifier = Modifier
) {

    var currentRoute by remember {
        mutableStateOf(Routes.SPLASH)
    }

    var homeRoute by remember {
        mutableStateOf(Routes.HOME)
    }

    var selectedPlace by remember {
        mutableStateOf<Places?>(null)
    }

    AppScaffold(
        currentRoute = currentRoute,
        homeRoute = homeRoute,
        onNavItemClick = {
            homeRoute = it
        },
        onNotificationsClick = {
            currentRoute = Routes.NOTIFICATIONS
        },
        modifier = modifier.fillMaxSize()
    ) { contentModifier ->

        AppContent(
            currentRoute = currentRoute,
            homeRoute = homeRoute,
            selectedPlace = selectedPlace,
            onRouteChange = {
                currentRoute = it
            },
            onPlaceClick = { place ->
                selectedPlace = place
                currentRoute = Routes.PLACE_DETAIL
            },
            modifier = contentModifier
        )
    }
}

@Composable
private fun AppContent(
    currentRoute: String,
    homeRoute: String,
    selectedPlace: Places?,
    onRouteChange: (String) -> Unit,
    onPlaceClick: (Places) -> Unit,
    modifier: Modifier = Modifier
) {

    when (currentRoute) {

        Routes.SPLASH -> {
            SplashScreen(
                onLoginClick = {
                    onRouteChange(Routes.LOGIN)
                },
                onSignUpClick = {
                    onRouteChange(Routes.SIGNUP)
                },
                modifier = modifier
            )
        }

        Routes.LOGIN -> {
            LoginScreen(
                onLoginClick = {
                    onRouteChange(Routes.HOME)
                },
                onSignUpClick = {
                    onRouteChange(Routes.SIGNUP)
                },
                modifier = modifier
            )
        }

        Routes.SIGNUP -> {
            SignUpScreen(
                onSignUpClick = {
                    onRouteChange(Routes.LOGIN)
                },
                onBackToLoginClick = {
                    onRouteChange(Routes.LOGIN)
                },
                modifier = modifier
            )
        }

        Routes.HOME -> {
            HomeScreen(
                currentRoute = homeRoute,
                onPlaceClick = onPlaceClick,
                modifier = modifier
            )
        }

        Routes.PLACE_DETAIL -> {
            selectedPlace?.let { place ->
                PlaceDetailScreen(
                    place = place,
                    onBackClick = {
                        onRouteChange(Routes.HOME)
                    },
                    modifier = modifier
                )
            }
        }

        Routes.NOTIFICATIONS -> {
            NotificationsScreen(
                onBackClick = {
                    onRouteChange(Routes.HOME)
                },
                modifier = modifier
            )
        }
    }
}