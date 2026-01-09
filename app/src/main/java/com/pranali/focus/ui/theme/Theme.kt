package com.pranali.focus.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// We prioritize the Light/Cozy theme as the default experience.
private val LightColorScheme = lightColorScheme(
    primary = BurntOrange,
    onPrimary = WarmCream,
    background = SoftBeige,
    onBackground = DarkWarmBrown,
    surface = WarmCream,
    onSurface = DarkWarmBrown,
    secondary = MutedMoss,
    onSecondary = WarmCream,
    outline = InactiveGrey
)

@Composable
fun CozyFocusTheme(
    // We default to light theme for the 'Paper/Cozy' aesthetic
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Note: For MVP we strictly use the Warm Light palette to ensure the vibe is correct.
    // Dark mode implementation would require a separate "Late Night Study" palette.
    val colorScheme = LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CozyTypography,
        content = content
    )
}