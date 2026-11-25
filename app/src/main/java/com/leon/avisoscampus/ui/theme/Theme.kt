@file:OptIn(androidx.compose.ui.text.ExperimentalTextApi::class)

package com.leon.avisoscampus.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.leon.avisoscampus.R

private val Outfit = FontFamily(
    Font(R.font.outfit, FontWeight.Normal, variationSettings = FontVariation.Settings(FontVariation.weight(400))),
    Font(R.font.outfit, FontWeight.Medium, variationSettings = FontVariation.Settings(FontVariation.weight(500))),
    Font(R.font.outfit, FontWeight.SemiBold, variationSettings = FontVariation.Settings(FontVariation.weight(600))),
    Font(R.font.outfit, FontWeight.Bold, variationSettings = FontVariation.Settings(FontVariation.weight(700)))
)

private val Luz = lightColorScheme(
    primary = Color(0xFF244DDE), onPrimary = Color.White,
    primaryContainer = Color(0xFFE0E7FF), onPrimaryContainer = Color(0xFF142D91),
    secondary = Color(0xFFA34031), onSecondary = Color.White,
    secondaryContainer = Color(0xFFFFDAD3), onSecondaryContainer = Color(0xFF622318),
    tertiary = Color(0xFF615294), onTertiary = Color.White,
    tertiaryContainer = Color(0xFFE7DEFF), onTertiaryContainer = Color(0xFF32245D),
    background = Color(0xFFF6F8FF), onBackground = Color(0xFF14204C),
    surface = Color(0xFFFDFDFF), onSurface = Color(0xFF14204C),
    surfaceVariant = Color(0xFFE9EDFA), onSurfaceVariant = Color(0xFF505B7A),
    surfaceDim = Color(0xFFDCE0EC), surfaceBright = Color(0xFFFDFDFF),
    surfaceContainerLowest = Color.White, surfaceContainerLow = Color(0xFFF2F5FF),
    surfaceContainer = Color(0xFFEDF1FC), surfaceContainerHigh = Color(0xFFE6ECFB),
    surfaceContainerHighest = Color(0xFFDDE5F7),
    outline = Color(0xFF717B98), outlineVariant = Color(0xFFD4DBED),
    error = Color(0xFFAE2E38), onError = Color.White,
    errorContainer = Color(0xFFFFDADB), onErrorContainer = Color(0xFF6D101C),
    inverseSurface = Color(0xFF26304F), inverseOnSurface = Color(0xFFF2F4FF),
    inversePrimary = Color(0xFFBAC7FF), scrim = Color(0xFF0D1530)
)

private val Noche = darkColorScheme(
    primary = Color(0xFFBAC7FF), onPrimary = Color(0xFF142D91),
    primaryContainer = Color(0xFF233D9D), onPrimaryContainer = Color(0xFFE0E7FF),
    secondary = Color(0xFFFFB4A6), onSecondary = Color(0xFF65291E),
    secondaryContainer = Color(0xFF823728), onSecondaryContainer = Color(0xFFFFDAD3),
    tertiary = Color(0xFFCAC3FF), onTertiary = Color(0xFF35265F),
    tertiaryContainer = Color(0xFF4D3D79), onTertiaryContainer = Color(0xFFE7DEFF),
    background = Color(0xFF0D1530), onBackground = Color(0xFFE7ECFF),
    surface = Color(0xFF111A36), onSurface = Color(0xFFE7ECFF),
    surfaceVariant = Color(0xFF323C59), onSurfaceVariant = Color(0xFFC0CAE6),
    surfaceDim = Color(0xFF0D1530), surfaceBright = Color(0xFF333D5A),
    surfaceContainerLowest = Color(0xFF091025), surfaceContainerLow = Color(0xFF151F3C),
    surfaceContainer = Color(0xFF1A2544), surfaceContainerHigh = Color(0xFF24304F),
    surfaceContainerHighest = Color(0xFF2E3A59),
    outline = Color(0xFF8B97B6), outlineVariant = Color(0xFF414D6B),
    error = Color(0xFFFFB2B8), onError = Color(0xFF650D1A),
    errorContainer = Color(0xFF8E2230), onErrorContainer = Color(0xFFFFDADB),
    inverseSurface = Color(0xFFE7ECFF), inverseOnSurface = Color(0xFF202B49),
    inversePrimary = Color(0xFF244DDE), scrim = Color.Black
)

private fun estilo(size: Int, line: Int, weight: FontWeight = FontWeight.Normal) = TextStyle(
    fontFamily = Outfit, fontSize = size.sp, lineHeight = line.sp, fontWeight = weight,
    letterSpacing = if (size >= 24) (-.4).sp else 0.sp
)

private val Tipografia = Typography(
    displayLarge = estilo(42, 48, FontWeight.SemiBold),
    displayMedium = estilo(36, 42, FontWeight.SemiBold),
    displaySmall = estilo(32, 38, FontWeight.SemiBold),
    headlineLarge = estilo(28, 34, FontWeight.SemiBold),
    headlineMedium = estilo(26, 32, FontWeight.SemiBold),
    headlineSmall = estilo(24, 30, FontWeight.SemiBold),
    titleLarge = estilo(22, 28, FontWeight.SemiBold),
    titleMedium = estilo(18, 24, FontWeight.SemiBold),
    titleSmall = estilo(16, 22, FontWeight.Medium),
    bodyLarge = estilo(16, 24), bodyMedium = estilo(14, 21), bodySmall = estilo(12, 18),
    labelLarge = estilo(14, 20, FontWeight.SemiBold),
    labelMedium = estilo(14, 20, FontWeight.Medium),
    labelSmall = estilo(12, 18, FontWeight.Medium)
)

@Composable
fun AvisosCampusTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (isSystemInDarkTheme()) Noche else Luz,
        typography = Tipografia,
        content = content
    )
}
