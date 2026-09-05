package com.example.wayspot

import android.os.Bundle
import android.view.animation.DecelerateInterpolator
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            splashScreenView.view
                .animate()
                .alpha(0f)
                .setDuration(SYSTEM_SPLASH_EXIT_DURATION_MS)
                .setInterpolator(DecelerateInterpolator())
                .withEndAction {
                    splashScreenView.remove()
                }
                .start()
        }

        setContent {
            WaySpotApp()
        }
    }

    private companion object {
        const val SYSTEM_SPLASH_EXIT_DURATION_MS = 450L
    }
}
