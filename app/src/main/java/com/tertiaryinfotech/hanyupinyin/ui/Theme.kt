package com.tertiaryinfotech.hanyupinyin.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

/**
 * Brand theme tokens. White theme — light background with a warm red accent inspired by
 * traditional Chinese red. Mirrors the iOS Theme; the iOS app pins a light appearance,
 * so the Android app uses the same light scheme in both system modes.
 */
object Brand {
    /** Primary accent — key buttons, active states, tab tint. (China red) */
    val accent = Color(0xFFE21E2E)
    /** Secondary accent — links, selected chips. */
    val secondary = Color(0xFF1473C7)
    /** Success / correct state. */
    val success = Color(0xFF219E5C)
    /** Error / wrong state. */
    val error = Color(0xFFD9332E)
    /** Highlight — tone marks, badges. */
    val highlight = Color(0xFFF29E1A)

    /** App background — clean near-white. */
    val background = Color(0xFFF7F7FA)
    /** Card / elevated surface — pure white. */
    val card = Color(0xFFFFFFFF)
    /** Subtle fills, chips. */
    val surface = Color(0xFFF0F0F5)

    /** Primary text — explicit dark ink for AA contrast on the off-white bg. */
    val ink = Color(0xFF1A1C21)
    /** Secondary text. */
    val mutedInk = Color(0xFF6B707A)

    // Extra card colours used by the practice catalog (match the iOS session colours).
    val orange = Color(0xFFE67319)
    val teal = Color(0xFF008C8C)
    val blue = Color(0xFF336BD9)
    val purple = Color(0xFF664DB3)
    val magenta = Color(0xFFCC2666)
    val violet = Color(0xFF8C47B8)
}

private val LightColors = lightColorScheme(
    primary = Brand.accent,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFBDADC),
    onPrimaryContainer = Color(0xFF5C0710),
    secondary = Brand.secondary,
    onSecondary = Color.White,
    background = Brand.background,
    onBackground = Brand.ink,
    surface = Brand.background,
    onSurface = Brand.ink,
    surfaceVariant = Brand.card,
    onSurfaceVariant = Brand.mutedInk,
    surfaceContainer = Brand.card,
    surfaceContainerHigh = Brand.surface,
    error = Brand.error,
)

@Composable
fun HanyuPinyinTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = LightColors, content = content)
}
