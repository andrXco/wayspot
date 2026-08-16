package com.example.wayspot.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wayspot.R

@Composable
fun LogoApp(modifier: Modifier = Modifier) {
    val logoResource = if (isSystemInDarkTheme()) {
        R.drawable.branding_logo_oscuro_wayspot
    } else {
        R.drawable.branding_logo_claro_wayspot
    }

    Image(
        painter = painterResource(logoResource),
        contentDescription = stringResource(R.string.logo_content_description),
        modifier = modifier.height(120.dp)
    )
}
