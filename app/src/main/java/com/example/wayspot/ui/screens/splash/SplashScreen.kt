package com.example.wayspot.ui.screens.splash

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
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
    val foregroundColor = if (isDarkTheme) BlancoCalido else Carbon
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

    Box(
        modifier = modifier.fillMaxSize()
    ) {
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

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.7f))

            Image(
                painter = painterResource(
                    if (isDarkTheme) {
                        R.drawable.branding_icono_oscuro_wayspot
                    } else {
                        R.drawable.branding_icono_claro_wayspot
                    }
                ),
                contentDescription = stringResource(R.string.logo_content_description),
                modifier = Modifier
                    .size(132.dp)
                    .clip(RoundedCornerShape(32.dp))
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.app_title),
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = foregroundColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.welcome_tagline),
                fontSize = 16.sp,
                color = foregroundColor.copy(alpha = 0.82f),
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier
                    .padding(top = 18.dp)
                    .width(56.dp)
                    .height(4.dp)
                    .background(ArenaDorado, MaterialTheme.shapes.small)
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    text = stringResource(R.string.welcome_login_button),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onSignUpClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = MaterialTheme.shapes.medium,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = stringResource(R.string.welcome_signup_button),
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Text(
                text = stringResource(R.string.welcome_terms),
                modifier = Modifier.fillMaxWidth(),
                fontSize = 11.sp,
                lineHeight = 15.sp,
                color = foregroundColor.copy(alpha = 0.68f),
                textAlign = TextAlign.Center
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
