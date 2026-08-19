package com.example.wayspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.wayspot.ui.screens.splash.SplashScreen
import com.example.wayspot.ui.theme.WayspotTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WayspotTheme(darkTheme = false) {
                SplashScreen(
                    onLoginClick = {},
                    onSignUpClick = {}
                )
            }
        }
    }
}
