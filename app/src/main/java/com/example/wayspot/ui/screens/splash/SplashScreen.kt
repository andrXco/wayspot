package com.example.wayspot.ui.screens.splash

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.splash.components.SplashActionsSection
import com.example.wayspot.ui.screens.splash.components.SplashBackground
import com.example.wayspot.ui.screens.splash.components.SplashBrandingSection
import com.example.wayspot.ui.theme.ArenaDorado
import com.example.wayspot.ui.theme.BlancoCalido
import com.example.wayspot.ui.theme.Carbon
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun SplashScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDarkTheme = isSystemInDarkTheme()

    val foregroundColor = if (isDarkTheme) {
        BlancoCalido
    } else {
        Carbon
    }

    val backgroundOverlay = Brush.verticalGradient(
        colors = if (isDarkTheme) {
            listOf(
                Carbon.copy(alpha = 0.72f),
                Carbon.copy(alpha = 0.72f)
            )
        } else {
            listOf(
                Carbon.copy(alpha = 0.12f),
                BlancoCalido.copy(alpha = 0.04f),
                BlancoCalido.copy(alpha = 0.18f)
            )
        }
    )

    SplashContent(
        isDarkTheme = isDarkTheme,
        foregroundColor = foregroundColor,
        backgroundOverlay = backgroundOverlay,
        onLoginClick = onLoginClick,
        onSignUpClick = onSignUpClick,
        modifier = modifier
    )
}

@Composable
fun SplashContent(
    isDarkTheme: Boolean,
    foregroundColor: Color,
    backgroundOverlay: Brush,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    SplashBackground(
        backgroundOverlay = backgroundOverlay,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.7f))

            SplashBrandingSection(
                isDarkTheme = isDarkTheme,
                foregroundColor = foregroundColor,
                accentColor = ArenaDorado,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.weight(1f))

            SplashActionsSection(
                onLoginClick = onLoginClick,
                onSignUpClick = onSignUpClick,
                foregroundColor = foregroundColor,
                modifier = Modifier
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun SplashScreenPreview() {
    WayspotTheme {
        SplashScreen(
            onLoginClick = {},
            onSignUpClick = {}
        )
    }
}
