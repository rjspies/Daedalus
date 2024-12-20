package com.rjspies.daedalus.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.rjspies.daedalus.R

val bodyFontFamily = FontFamily(
    Font(
        resId = R.font.athiti_extra_light,
        weight = FontWeight.ExtraLight,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.athiti_light,
        weight = FontWeight.Light,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.athiti_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.athiti_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.athiti_semi_bold,
        weight = FontWeight.SemiBold,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.athiti_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal,
    ),
)

val displayFontFamily = FontFamily(
    Font(
        resId = R.font.cardo_regular,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.cardo_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.cardo_italic,
        weight = FontWeight.Normal,
        style = FontStyle.Italic,
    ),
)

// Default Material 3 typography values
val baseline = Typography()

val DaedalusTypography = Typography(
    displayLarge = baseline.displayLarge.copy(fontFamily = displayFontFamily),
    displayMedium = baseline.displayMedium.copy(fontFamily = displayFontFamily),
    displaySmall = baseline.displaySmall.copy(fontFamily = displayFontFamily),
    headlineLarge = baseline.headlineLarge.copy(fontFamily = displayFontFamily),
    headlineMedium = baseline.headlineMedium.copy(fontFamily = displayFontFamily),
    headlineSmall = baseline.headlineSmall.copy(fontFamily = displayFontFamily),
    titleLarge = baseline.titleLarge.copy(fontFamily = displayFontFamily),
    titleMedium = baseline.titleMedium.copy(fontFamily = displayFontFamily),
    titleSmall = baseline.titleSmall.copy(fontFamily = displayFontFamily),
    bodyLarge = baseline.bodyLarge.copy(fontFamily = bodyFontFamily),
    bodyMedium = baseline.bodyMedium.copy(fontFamily = bodyFontFamily),
    bodySmall = baseline.bodySmall.copy(fontFamily = bodyFontFamily),
    labelLarge = baseline.labelLarge.copy(fontFamily = bodyFontFamily),
    labelMedium = baseline.labelMedium.copy(fontFamily = bodyFontFamily),
    labelSmall = baseline.labelSmall.copy(fontFamily = bodyFontFamily),
)
