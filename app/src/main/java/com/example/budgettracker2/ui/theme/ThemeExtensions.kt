package com.example.budgettracker2.ui.theme

// ui/theme/ThemeExtensions.kt

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Brush

@Composable
fun getPocketBrush(colorName: String?): Brush {
    val isDarkMode = isSystemInDarkTheme()
    val brushPair = PocketBrushes.getBrushPair(colorName)
    return if (isDarkMode) brushPair.dark else brushPair.light
}