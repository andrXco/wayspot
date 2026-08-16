package com.example.wayspot.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,
    onPrimary = Carbon,
    primaryContainer = VerdeBosque,
    onPrimaryContainer = BlancoCalido,
    inversePrimary = VerdeBosque,
    secondary = VerdeSalvia,
    onSecondary = Carbon,
    secondaryContainer = VerdeBosque,
    onSecondaryContainer = BlancoCalido,
    tertiary = ArenaDorado,
    onTertiary = Carbon,
    tertiaryContainer = Terracota,
    onTertiaryContainer = Carbon,
    background = Carbon,
    onBackground = BlancoCalido,
    surface = Carbon,
    onSurface = BlancoCalido,
    surfaceVariant = Gris,
    onSurfaceVariant = GrisClaro,
    surfaceTint = VerdeClaro,
    inverseSurface = BlancoCalido,
    inverseOnSurface = Carbon,
    error = Terracota,
    onError = Blanco,
    errorContainer = Terracota,
    onErrorContainer = Carbon,
    outline = VerdeSalvia,
    outlineVariant = Gris,
    scrim = Carbon
)

private val LightColorScheme = lightColorScheme(
    primary = VerdeBosque,
    onPrimary = Blanco,
    secondary = VerdeSalvia,
    onSecondary = Carbon,
    tertiary = Terracota,
    onTertiary = Blanco,
    background = BlancoCalido,
    onBackground = Carbon,
    surface = BlancoCalido,
    onSurface = Carbon
)

@Composable
fun WayspotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        /*dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }*/

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
