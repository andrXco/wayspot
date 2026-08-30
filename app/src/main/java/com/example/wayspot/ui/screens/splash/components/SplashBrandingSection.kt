package com.example.wayspot.ui.screens.splash.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
    foregroundColor: Color,
    accentColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(84.dp)
                .clip(RoundedCornerShape(20.dp))
        ) {
            Image(
                painter = painterResource(R.drawable.branding_icono_oscuro_wayspot),
                contentDescription = stringResource(R.string.logo_content_description),
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        scaleX = 1.12f
                        scaleY = 1.12f
                    },
                contentScale = ContentScale.Crop
            )
        }

        Text(
            text = stringResource(R.string.app_title),
            modifier = Modifier,
            fontSize = 40.sp,
            lineHeight = 44.sp,
            fontWeight = FontWeight.ExtraBold,
            color = foregroundColor
        )

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)
        ) {
            BrandingDivider(
                color = accentColor,
                modifier = Modifier
            )

            Text(
                text = stringResource(R.string.welcome_tagline),
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                color = foregroundColor.copy(alpha = 0.72f),
                textAlign = TextAlign.Center
            )

            BrandingDivider(
                color = accentColor,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun BrandingDivider(
    color: Color,
    modifier: Modifier = Modifier
) {
    androidx.compose.foundation.layout.Box(
        modifier = modifier
            .width(36.dp)
            .height(1.dp)
            .background(
                color = color.copy(alpha = 0.72f),
                shape = MaterialTheme.shapes.small
            )
    )
}

@WayspotMultiPreview
@Composable
private fun SplashBrandingSectionPreview() {
    WayspotTheme {
        SplashBrandingSection(
            foregroundColor = MaterialTheme.colorScheme.onBackground,
            accentColor = MaterialTheme.colorScheme.tertiary
        )
    }
}
