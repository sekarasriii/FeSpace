package com.example.fespace.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryBlueLight,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueDark,
    onPrimaryContainer = PrimaryBlueLight,
    
    secondary = SecondaryPurpleLight,
    onSecondary = Color.White,
    secondaryContainer = SecondaryPurple,
    onSecondaryContainer = SecondaryPurpleLight,
    
    tertiary = AccentOrange,
    onTertiary = Color.White,
    
    background = DarkBackground,
    onBackground = NeutralGray100,
    
    surface = DarkSurface,
    onSurface = NeutralGray100,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = NeutralGray300,
    
    error = AccentRed,
    onError = Color.White,
    
    outline = NeutralGray600,
    outlineVariant = NeutralGray700
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = PrimaryBlueLight,
    onPrimaryContainer = PrimaryBlueDark,
    
    secondary = SecondaryPurple,
    onSecondary = Color.White,
    secondaryContainer = SecondaryPurpleLight,
    onSecondaryContainer = SecondaryPurple,
    
    tertiary = AccentOrange,
    onTertiary = Color.White,
    
    background = NeutralGray50,
    onBackground = NeutralGray900,
    
    surface = Color.White,
    onSurface = NeutralGray900,
    surfaceVariant = NeutralGray100,
    onSurfaceVariant = NeutralGray700,
    
    error = AccentRed,
    onError = Color.White,
    
    outline = NeutralGray300,
    outlineVariant = NeutralGray200
)

@Composable
fun FeSpaceTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled for consistent branding
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
    
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}