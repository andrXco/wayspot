package com.example.wayspot.ui.screens.splash

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.components.LogoApp
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun SplashScreen(
    onComenzarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoApp()
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = stringResource(R.string.welcome_message, stringResource(R.string.app_title)),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(onClick = { onComenzarClick() }) {
            Text(stringResource(R.string.button_start))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SplashScreenPreview() {
    WayspotTheme {
        SplashScreen(onComenzarClick = {})
    }
}
