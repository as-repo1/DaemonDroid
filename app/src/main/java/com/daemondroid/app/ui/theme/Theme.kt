package com.daemondroid.app.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
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

// ─────────────────────────────────────────────────────────────────────────────
// DaemonDroid Material 3 Theme
// Dark: moody terminal — near-black bg, electric violet primary, cyan accents
// Light: clean Material 3 with same brand violet
// ─────────────────────────────────────────────────────────────────────────────

private val DarkColorScheme = darkColorScheme(
    primary = DaemonPurpleLight,
    onPrimary = DaemonPurpleDark,
    primaryContainer = DaemonPurpleContainer,
    onPrimaryContainer = OnDaemonPurpleContainer,
    secondary = CyanAccent,
    onSecondary = DarkBackground,
    secondaryContainer = Color(0xFF003640),
    onSecondaryContainer = Color(0xFF9EEFFD),
    tertiary = GreenSuccess,
    onTertiary = Color(0xFF003920),
    tertiaryContainer = Color(0xFF005229),
    onTertiaryContainer = Color(0xFF95F7B5),
    error = RedDanger,
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,
    inverseSurface = TextPrimary,
    inverseOnSurface = DarkBackground,
    inversePrimary = DaemonPurple,
    scrim = Color(0xFF000000)
)

private val LightColorScheme = lightColorScheme(
    primary = DaemonPurple,
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEDE0FF),
    onPrimaryContainer = Color(0xFF22005D),
    secondary = CyanAccentDim,
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFCCF4FF),
    onSecondaryContainer = Color(0xFF001F26),
    tertiary = Color(0xFF006E2A),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF95F7B5),
    onTertiaryContainer = Color(0xFF002109),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    background = LightBackground,
    onBackground = TextOnLight,
    surface = LightSurface,
    onSurface = TextOnLight,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = Color(0xFF44444F),
    outline = LightOutline,
)

@Composable
fun DaemonDroidTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color: disabled by default to preserve our custom terminal branding
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme: ColorScheme = when {
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
            // Colors are handled by enableEdgeToEdge() in MainActivity
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = DaemonTypography,
        content = content
    )
}
