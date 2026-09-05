package com.example.wayspot.ui.screens.splash

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.screens.splash.components.SplashActionsSection
import com.example.wayspot.ui.screens.splash.components.SplashBackground
import com.example.wayspot.ui.screens.splash.components.SplashBrandingSection
import com.example.wayspot.ui.screens.splash.components.SplashDestinationChipsSection
import com.example.wayspot.ui.theme.WayspotTheme
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    splashViewModel: SplashViewModel,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by splashViewModel.uiState.collectAsState()

    val isDarkTheme = isSystemInDarkTheme()
    val colorScheme = MaterialTheme.colorScheme

    val foregroundColor = if (isDarkTheme) {
        colorScheme.onBackground
    } else {
        colorScheme.onPrimary
    }

    val primaryColor = if (isDarkTheme) {
        colorScheme.primaryContainer
    } else {
        colorScheme.primary
    }

    val onPrimaryColor = if (isDarkTheme) {
        colorScheme.onPrimaryContainer
    } else {
        colorScheme.onPrimary
    }

    val accentColor = if (isDarkTheme) {
        colorScheme.tertiary
    } else {
        colorScheme.tertiaryContainer
    }

    val backgroundOverlay = Brush.verticalGradient(
        colorStops = arrayOf(
            0f to colorScheme.scrim.copy(alpha = 0.56f),
            0.35f to primaryColor.copy(alpha = 0.18f),
            0.65f to colorScheme.scrim.copy(alpha = 0.58f),
            0.85f to colorScheme.scrim.copy(alpha = 0.9f),
            1f to colorScheme.scrim.copy(alpha = 0.96f)
        )
    )

    if (state.isReady) {
        SplashContent(
            foregroundColor = foregroundColor,
            primaryColor = primaryColor,
            onPrimaryColor = onPrimaryColor,
            accentColor = accentColor,
            entranceOverlayColor = colorScheme.scrim,
            backgroundOverlay = backgroundOverlay,
            onLoginClick = onLoginClick,
            onSignUpClick = onSignUpClick,
            modifier = modifier
        )
    }
}

@Composable
fun SplashContent(
    foregroundColor: Color,
    primaryColor: Color,
    onPrimaryColor: Color,
    accentColor: Color,
    entranceOverlayColor: Color,
    backgroundOverlay: Brush,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier,
    animateEntrance: Boolean = true
) {
    val revealProgress = remember(animateEntrance) {
        Animatable(if (animateEntrance) 0f else 1f)
    }

    LaunchedEffect(animateEntrance) {
        if (animateEntrance) {
            delay(SPLASH_REVEAL_DELAY_MS)
            revealProgress.animateTo(
                targetValue = 1f,
                animationSpec = tween(
                    durationMillis = SPLASH_REVEAL_DURATION_MS,
                    easing = FastOutSlowInEasing
                )
            )
        } else {
            revealProgress.snapTo(1f)
        }
    }

    SplashBackground(
        backgroundOverlay = backgroundOverlay,
        entranceOverlayColor = entranceOverlayColor,
        entranceOverlayAlpha = (1f - revealProgress.value) * INITIAL_DARK_OVERLAY_ALPHA,
        contentAlpha = INITIAL_CONTENT_ALPHA +
            (1f - INITIAL_CONTENT_ALPHA) * revealProgress.value,
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp,
                    vertical = 24.dp
                )
        ) {
            SplashDestinationChipsSection(
                foregroundColor = foregroundColor,
                modifier = Modifier
            )

            Spacer(
                modifier = Modifier.weight(1f)
            )

            SplashBrandingSection(
                foregroundColor = foregroundColor,
                accentColor = accentColor,
                modifier = Modifier
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            SplashActionsSection(
                onLoginClick = onLoginClick,
                onSignUpClick = onSignUpClick,
                foregroundColor = foregroundColor,
                primaryColor = primaryColor,
                onPrimaryColor = onPrimaryColor,
                modifier = Modifier
            )
        }
    }
}

@WayspotMultiPreview
@Composable
private fun SplashScreenPreview() {
    WayspotTheme {
        val colorScheme = MaterialTheme.colorScheme

        SplashContent(
            foregroundColor = colorScheme.onPrimary,
            primaryColor = colorScheme.primary,
            onPrimaryColor = colorScheme.onPrimary,
            accentColor = colorScheme.tertiaryContainer,
            entranceOverlayColor = colorScheme.scrim,
            backgroundOverlay = Brush.verticalGradient(
                colors = listOf(
                    colorScheme.scrim,
                    colorScheme.primary
                )
            ),
            onLoginClick = {},
            onSignUpClick = {},
            animateEntrance = false
        )
    }
}

private const val SPLASH_REVEAL_DELAY_MS = 300L
private const val SPLASH_REVEAL_DURATION_MS = 700
private const val INITIAL_DARK_OVERLAY_ALPHA = 0.32f
private const val INITIAL_CONTENT_ALPHA = 0.58f
