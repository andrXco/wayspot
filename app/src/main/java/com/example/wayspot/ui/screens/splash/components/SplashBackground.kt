package com.example.wayspot.ui.screens.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun SplashBackground(
    backgroundOverlay: Brush,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.wallpaper_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundOverlay)
        )

        content()
    }
}
@WayspotMultiPreview
@Composable
private fun SplashBackgroundPreview() {
    WayspotTheme {
        SplashBackground(
            backgroundOverlay = Brush.verticalGradient(
                colors = listOf(
                    MaterialTheme.colorScheme.background.copy(alpha = 0.2f),
                    MaterialTheme.colorScheme.background.copy(alpha = 0.9f)
                )
            ),
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = "WaySpot",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(24.dp)
            )
        }
    }
}
