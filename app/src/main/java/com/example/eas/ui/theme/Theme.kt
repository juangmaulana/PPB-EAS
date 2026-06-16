package com.example.eas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val CoffeeColorScheme = lightColorScheme(
    primary = DarkGreen,
    onPrimary = Color.White,
    primaryContainer = LightGreen.copy(alpha = 0.25f),
    onPrimaryContainer = DarkGreen,

    secondary = MediumGreen,
    onSecondary = Color.White,
    secondaryContainer = MediumGreen.copy(alpha = 0.15f),
    onSecondaryContainer = DarkGreen,

    tertiary = Brown,
    onTertiary = Color.White,
    tertiaryContainer = Brown.copy(alpha = 0.12f),
    onTertiaryContainer = Brown,

    error = ErrorRed,
    onError = Color.White,
    errorContainer = ErrorRed.copy(alpha = 0.12f),
    onErrorContainer = ErrorRed,

    background = Cream,
    onBackground = Color(0xFF1C1B1F),

    surface = LightCream,
    onSurface = Color(0xFF1C1B1F),
    surfaceVariant = DividerColor,
    onSurfaceVariant = Color(0xFF4A4540),

    outline = Color(0xFF9C8E85),
    outlineVariant = DividerColor,

    inverseSurface = Color(0xFF313033),
    inverseOnSurface = Color(0xFFF4EFF4),
    inversePrimary = LightGreen,
)

@Composable
fun CoffeeBlissTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = CoffeeColorScheme,
        typography = Typography,
        content = content
    )
}
