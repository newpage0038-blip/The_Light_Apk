package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = LightSunGold,
    onPrimary = Color.Black,
    primaryContainer = LightEmeraldGreenDark,
    onPrimaryContainer = Color(0xFFA5D6A7),
    secondary = LightTangerine,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF3E2723),
    onSecondaryContainer = Color(0xFFFFCC80),
    tertiary = LightEmeraldGreenLight,
    background = DarkBackground,
    onBackground = Color(0xFFE8F5E9),
    surface = DarkSurface,
    onSurface = Color(0xFFE8F5E9),
    surfaceVariant = DarkCard,
    onSurfaceVariant = Color(0xFFB0BEC5),
    error = AccentRed,
    outline = DarkCardBorder
)

private val LightColorScheme = lightColorScheme(
    primary = LightEmeraldGreen,
    onPrimary = Color.White,
    primaryContainer = LightEmeraldGreenContainer,
    onPrimaryContainer = LightOnEmeraldContainer,
    secondary = LightSunriseOrange,
    onSecondary = Color.White,
    secondaryContainer = LightOrangeContainer,
    onSecondaryContainer = Color(0xFF5D2400),
    tertiary = LightSunGoldDark,
    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    error = AccentRed,
    outline = Color(0xFFCFD8DC)
)

@Composable
fun TheLightAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep signature Akwa Ibom emerald & gold branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
