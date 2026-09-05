package com.example.wayspot.ui.screens.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.wayspot.R

@Composable
fun SplashBackground(
    backgroundOverlay: Brush,
    entranceOverlayColor: Color,
    entranceOverlayAlpha: Float,
    contentAlpha: Float,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.splash_mountain_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundOverlay)
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    entranceOverlayColor.copy(alpha = entranceOverlayAlpha)
                )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    alpha = contentAlpha
                }
        ) {
            content()
        }
    }
}
