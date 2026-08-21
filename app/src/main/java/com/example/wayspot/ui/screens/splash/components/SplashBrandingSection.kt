package com.example.wayspot.ui.screens.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun SplashBrandingSection(
    isDarkTheme: Boolean,
    foregroundColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                .background(
                    color = accentColor,
                    shape = MaterialTheme.shapes.small
                )
        )
    }
}

@WayspotMultiPreview
@Composable
private fun SplashBrandingSectionPreview() {
    WayspotTheme {
        SplashBrandingSection(
            isDarkTheme = false,
            foregroundColor = MaterialTheme.colorScheme.onBackground,
            accentColor = MaterialTheme.colorScheme.primary
        )
    }
}
