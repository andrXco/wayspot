package com.example.wayspot.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = VerdeBosque,
    onPrimary = Blanco,

    primaryContainer = VerdeClaro,
    onPrimaryContainer = Carbon,

    secondary = VerdeSalvia,
    onSecondary = Carbon,

    secondaryContainer = GrisClaro,
    onSecondaryContainer = Carbon,

    tertiary = Terracota,
    onTertiary = Blanco,

    tertiaryContainer = ArenaDorado,
    onTertiaryContainer = Carbon,

    background = BlancoCalido,
    onBackground = Carbon,

    surface = Blanco,
    onSurface = Carbon,

    surfaceVariant = GrisClaro,
    onSurfaceVariant = Gris,

    outline = VerdeSalvia,

    error = Terracota,
    onError = Blanco
)

private val DarkColorScheme = darkColorScheme(
    primary = VerdeClaro,
    onPrimary = Carbon,

    primaryContainer = VerdeBosque,
    onPrimaryContainer = BlancoCalido,

    secondary = VerdeSalvia,
    onSecondary = Carbon,

    secondaryContainer = Gris,
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

    outline = VerdeSalvia,

    error = Terracota,
    onError = Blanco
)

@Composable
fun WayspotTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}