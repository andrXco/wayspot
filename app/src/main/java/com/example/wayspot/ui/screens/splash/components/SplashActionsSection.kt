package com.example.wayspot.ui.screens.splash.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R
import com.example.wayspot.ui.preview.WayspotMultiPreview
import com.example.wayspot.ui.theme.WayspotTheme


@Composable
fun SplashActionsSection(
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    foregroundColor: Color,
    primaryColor: Color,
    onPrimaryColor: Color,
    modifier: Modifier = Modifier
) {
    val buttonShape = RoundedCornerShape(16.dp)

    Column(modifier = modifier) {
        Button(
            onClick = onLoginClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .shadow(
                    elevation = 14.dp,
                    shape = buttonShape,
                    clip = false,
                    ambientColor = primaryColor.copy(alpha = 0.46f),
                    spotColor = primaryColor.copy(alpha = 0.62f)
                ),
            shape = buttonShape,
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryColor,
                contentColor = onPrimaryColor
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 2.dp,
                pressedElevation = 6.dp
            )
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
                .height(56.dp)
                .shadow(
                    elevation = 9.dp,
                    shape = buttonShape,
                    clip = false,
                    ambientColor = foregroundColor.copy(alpha = 0.12f),
                    spotColor = primaryColor.copy(alpha = 0.24f)
                ),
            shape = buttonShape,
            border = BorderStroke(
                width = 1.dp,
                color = foregroundColor.copy(alpha = 0.2f)
            ),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = foregroundColor.copy(alpha = 0.1f),
                contentColor = foregroundColor.copy(alpha = 0.9f)
            )
        ) {
            Text(
                text = stringResource(R.string.welcome_signup_button),
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        Text(
            text = buildAnnotatedString {
                append(stringResource(R.string.welcome_terms_prefix))
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(stringResource(R.string.welcome_terms_service))
                }
                append(stringResource(R.string.welcome_terms_and))
                withStyle(SpanStyle(textDecoration = TextDecoration.Underline)) {
                    append(stringResource(R.string.welcome_terms_privacy))
                }
            },
            modifier = Modifier.fillMaxWidth(),
            fontSize = 12.sp,
            lineHeight = 18.sp,
            color = foregroundColor.copy(alpha = 0.42f),
            textAlign = TextAlign.Center
        )
    }

}

@WayspotMultiPreview
@Composable
private fun SplashActionsSectionPreview() {
    WayspotTheme {
        SplashActionsSection(
            onLoginClick = {},
            onSignUpClick = {},
            foregroundColor = MaterialTheme.colorScheme.onBackground,
            primaryColor = MaterialTheme.colorScheme.primary,
            onPrimaryColor = MaterialTheme.colorScheme.onPrimary
        )
    }
}
