package com.example.wayspot

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import dagger.hilt.android.AndroidEntryPoint

/** Punto de entrada Android que instala el splash y delega el montaje de Compose en `WaySpotApp`. */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        splashScreen.configureWaySpotExitAnimation()
        setWaySpotContent()
    }
}
