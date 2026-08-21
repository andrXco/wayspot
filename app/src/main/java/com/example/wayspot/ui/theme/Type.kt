package com.example.wayspot.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.wayspot.R

private val NunitoFontFamily = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_semibold, FontWeight.SemiBold),
    Font(R.font.nunito_bold, FontWeight.Bold),
    Font(R.font.nunito_extrabold, FontWeight.ExtraBold)
)

private val MaterialTypography = Typography()

val WayspotTypography = Typography(
    displayLarge = MaterialTypography.displayLarge.withNunito(FontWeight.ExtraBold),
    displayMedium = MaterialTypography.displayMedium.withNunito(FontWeight.ExtraBold),
    displaySmall = MaterialTypography.displaySmall.withNunito(FontWeight.Bold),
    headlineLarge = MaterialTypography.headlineLarge.withNunito(FontWeight.Bold),
    headlineMedium = MaterialTypography.headlineMedium.withNunito(FontWeight.Bold),
    headlineSmall = MaterialTypography.headlineSmall.withNunito(FontWeight.Bold),
    titleLarge = MaterialTypography.titleLarge.withNunito(FontWeight.Bold),
    titleMedium = MaterialTypography.titleMedium.withNunito(FontWeight.SemiBold),
    titleSmall = MaterialTypography.titleSmall.withNunito(FontWeight.SemiBold),
    bodyLarge = MaterialTypography.bodyLarge.withNunito(FontWeight.Normal),
    bodyMedium = MaterialTypography.bodyMedium.withNunito(FontWeight.Normal),
    bodySmall = MaterialTypography.bodySmall.withNunito(FontWeight.Normal),
    labelLarge = MaterialTypography.labelLarge.withNunito(FontWeight.SemiBold),
    labelMedium = MaterialTypography.labelMedium.withNunito(FontWeight.Medium),
    labelSmall = MaterialTypography.labelSmall.withNunito(FontWeight.Medium)
)

private fun TextStyle.withNunito(fontWeight: FontWeight): TextStyle = copy(
    fontFamily = NunitoFontFamily,
    fontWeight = fontWeight
)
