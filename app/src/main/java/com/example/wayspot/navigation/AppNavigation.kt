package com.example.wayspot.navigation

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.wayspot.data.model.Places
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

    val profileDrawsBehindStatusBar =
        currentRoute == Routes.HOME && homeRoute == Routes.PROFILE
    val useDarkStatusBarIcons = if (profileDrawsBehindStatusBar) {
        isSystemInDarkTheme()
    } else {
        !isSystemInDarkTheme()
    }
    val view = LocalView.current

    SideEffect {
        if (!view.isInEditMode) {
            val window = (view.context as? Activity)?.window
            if (window != null) {
                WindowCompat.getInsetsController(window, window.decorView)
                    .isAppearanceLightStatusBars = useDarkStatusBarIcons
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (profileDrawsBehindStatusBar) {
                    Modifier
                } else {
                    Modifier.statusBarsPadding()
                }
            )
    ) {
        when (currentRoute) {

            Routes.SPLASH -> {
                SplashScreen(
                    onLoginClick = {
                        currentRoute = Routes.LOGIN
                    },
                    onSignUpClick = {
                        currentRoute = Routes.SIGNUP
                    }
                )
            }

            Routes.LOGIN -> {
                LoginScreen(
                    onLoginClick = {
                        currentRoute = Routes.HOME
                    },
                    onSignUpClick = {
                        currentRoute = Routes.SIGNUP
                    }
                )
            }

            Routes.SIGNUP -> {
                SignUpScreen(
                    onSignUpClick = {
                        currentRoute = Routes.LOGIN
                    },
                    onBackToLoginClick = {
                        currentRoute = Routes.LOGIN
                    }
                )
            }

            Routes.HOME -> {
                HomeScreen(
                    currentRoute = homeRoute,

                    onNavItemClick = {
                        homeRoute = it
                    },

                    onNotificationsClick = {
                        currentRoute = Routes.NOTIFICATIONS
                    },

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
                        onBackClick = {
                            currentRoute = Routes.HOME
                        }
                    )
                }
            }

            Routes.NOTIFICATIONS -> {
                NotificationsScreen(
                    onBackClick = {
                        currentRoute = Routes.HOME
                    }
                )
            }
        }
    }
}
