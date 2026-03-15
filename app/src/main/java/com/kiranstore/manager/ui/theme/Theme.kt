package com.kiranstore.manager.ui.theme

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// ── Brand Colors ────────────────────────────────────────
val OrangePrimary    = Color(0xFFE8490F)
val OrangeDark       = Color(0xFFC23A08)
val OrangeLight      = Color(0xFFFF7043)
val OrangeSurface    = Color(0xFFFFF3EF)
val OrangeContainer  = Color(0xFFFFE5DC)

val GreenSuccess     = Color(0xFF2E7D32)
val RedError         = Color(0xFFD32F2F)
val RedDanger        = Color(0xFFC62828)
val RedLight         = Color(0xFFFFEBEE)
val AmberWarning     = Color(0xFFF57F17)

val BackgroundLight  = Color(0xFFF5F5F5)
val BackgroundGray   = Color(0xFFF5F5F5)
val SurfaceLight     = Color(0xFFFFFFFF)
val SurfaceCard      = Color(0xFFFFFFFF)
val OnSurfaceGrey    = Color(0xFF757575)
val DividerColor     = Color(0xFFE0E0E0)

val TextPrimary      = Color(0xFF1C1B1F)
val TextSecondary    = Color(0xFF757575)

// ── Light Color Scheme ──────────────────────────────────
private val LightColorScheme = lightColorScheme(
    primary          = OrangePrimary,
    onPrimary        = Color.White,
    primaryContainer = OrangeContainer,
    onPrimaryContainer = OrangeDark,
    secondary        = OrangeDark,
    onSecondary      = Color.White,
    secondaryContainer = OrangeLight.copy(alpha = 0.2f),
    background       = BackgroundLight,
    onBackground     = Color(0xFF1C1B1F),
    surface          = SurfaceLight,
    onSurface        = Color(0xFF1C1B1F),
    surfaceVariant   = Color(0xFFF4F0F0),
    onSurfaceVariant = Color(0xFF49454F),
    outline          = DividerColor,
    error            = RedError,
    onError          = Color.White,
)

@Composable
fun KiranStoreTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography   = KiranTypography,
        content      = content
    )
}
