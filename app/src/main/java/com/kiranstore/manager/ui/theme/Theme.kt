package com.kiranstore.manager.ui.theme

import android.app.Activity
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val KiranColorScheme = lightColorScheme(
    primary = OrangePrimary,
    onPrimary = CardWhite,
    primaryContainer = OrangeLight,
    onPrimaryContainer = OrangeDark,
    secondary = GreenSuccess,
    onSecondary = CardWhite,
    secondaryContainer = GreenLight,
    onSecondaryContainer = GreenSuccess,
    error = RedDanger,
    errorContainer = RedLight,
    background = BackgroundGrey,
    onBackground = TextPrimary,
    surface = CardWhite,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    outline = DividerColor
)

@Composable
fun KiranTheme(content: @Composable () -> Unit) {
    val colorScheme = KiranColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = CardWhite.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = true
        }
    }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
