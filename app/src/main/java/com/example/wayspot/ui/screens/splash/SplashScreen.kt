package com.example.wayspot.ui.screens.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme

@Composable
fun SplashScreen(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme
    
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.weight(0.7f))

            Box(
                modifier = Modifier
                    .size(132.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "WS",
                    color = colorScheme.onPrimaryContainer,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.app_title),
                fontSize = 38.sp,
                fontWeight = FontWeight.Bold,
                color = colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.welcome_tagline),
                fontSize = 16.sp,
                color = colorScheme.onBackground.copy(alpha = 0.82f),
                textAlign = TextAlign.Center
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
            ) {
                Text(
                    text = stringResource(R.string.welcome_signup_button),
                    fontWeight = FontWeight.Bold
                )
            }
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
