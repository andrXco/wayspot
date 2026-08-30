package com.example.wayspot.ui.screens.splash.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material.icons.outlined.Eco
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R

@Composable
fun SplashDestinationChipsSection(
    foregroundColor: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SplashDestinationChip(
                text = stringResource(R.string.splash_destination_patagonia),
                icon = Icons.Outlined.Landscape,
                foregroundColor = foregroundColor,
                modifier = Modifier
            )
            SplashDestinationChip(
                text = stringResource(R.string.splash_destination_cappadocia),
                icon = Icons.Outlined.Air,
                foregroundColor = foregroundColor,
                modifier = Modifier
            )
        }

        Row(
            modifier = Modifier,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SplashDestinationChip(
                text = stringResource(R.string.splash_destination_kyoto),
                icon = Icons.Outlined.AccountBalance,
                foregroundColor = foregroundColor,
                modifier = Modifier
            )
            SplashDestinationChip(
                text = stringResource(R.string.splash_destination_machu_picchu),
                icon = Icons.Outlined.Eco,
                foregroundColor = foregroundColor,
                modifier = Modifier
            )
        }
    }
}

@Composable
private fun SplashDestinationChip(
    text: String,
    icon: ImageVector,
    foregroundColor: Color,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(38.dp)
            .widthIn(min = 96.dp)
            .clip(CircleShape)
            .background(foregroundColor.copy(alpha = 0.12f))
            .border(
                border = BorderStroke(
                    width = 1.dp,
                    color = foregroundColor.copy(alpha = 0.16f)
                ),
                shape = CircleShape
            )
            .padding(horizontal = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = foregroundColor.copy(alpha = 0.82f)
        )
        Text(
            text = text,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontWeight = FontWeight.Medium,
            color = foregroundColor.copy(alpha = 0.9f)
        )
    }
}
