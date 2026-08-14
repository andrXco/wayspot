@file:JvmName("SplashScreenKt")

package com.example.wayspot.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wayspot.R

@Composable
fun MensajeBienvenida(nombre: String, modifier: Modifier = Modifier){
    Text(stringResource(R.string.welcome_message, nombre),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Blue,
        modifier = modifier
    )
}

@Composable
fun LogoApp(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(R.drawable.branding_logo_wayspot),
        contentDescription = stringResource(R.string.logo_content_description),
        modifier = modifier.height(120.dp)
    )
}

@Composable
fun BodyScreen(onComenzarClick: () -> Unit, modifier: Modifier = Modifier){
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LogoApp()
        Spacer(modifier = Modifier.height(16.dp))
        MensajeBienvenida(stringResource(R.string.app_title))
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = { onComenzarClick() }) {
            Text(stringResource(R.string.button_start))
        }
    }
}

@Composable
@Preview(showBackground = true)
fun BodyHomeScreenPreview(){
    BodyScreen(onComenzarClick = {})
}
