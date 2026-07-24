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

private val LightColorScheme = lightColorScheme(
    primary = PrimaryRed,
    onPrimary = Color.White,
    primaryContainer = PrimaryRedLight,
    onPrimaryContainer = PrimaryRedDark,
    secondary = BlackDark,
    onSecondary = Color.White,
    secondaryContainer = Slate100,
    onSecondaryContainer = BlackDark,
    tertiary = AccentGold,
    background = GrayBackground,
    onBackground = BlackDark,
    surface = WhitePure,
    onSurface = BlackDark,
    surfaceVariant = Slate100,
    onSurfaceVariant = GrayText,
    outline = GrayBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFEF5350),
    onPrimary = Color.Black,
    primaryContainer = PrimaryRedDark,
    onPrimaryContainer = PrimaryRedLight,
    secondary = Color(0xFFE0E0E0),
    onSecondary = Color.Black,
    secondaryContainer = BlackSurface,
    onSecondaryContainer = Color.White,
    tertiary = AccentGold,
    background = BlackDark,
    onBackground = Color.White,
    surface = BlackSurface,
    onSurface = Color.White,
    surfaceVariant = Color(0xFF242424),
    onSurfaceVariant = Color(0xFFB0B0B0),
    outline = Color(0xFF424242)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Set to false to enforce Red + White + Black brand colors
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
