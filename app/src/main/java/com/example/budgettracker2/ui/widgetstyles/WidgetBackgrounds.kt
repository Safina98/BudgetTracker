package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val midnightGreen = Brush.linearGradient(
    0.0f to Color(0xFF105666),          // Start color
    0.6f to Color(0xFF105666),          // Center color at 60%
    1.0f to Color(0x85105666),          // End color (85 alpha)
    start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
    end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
)

val roseBrown = Brush.linearGradient(
    0.0f to Color(0xFFEBD3969C),          // Start color
    0.6f to Color(0xFFD3969C),          // Center color at 60%
    1.0f to Color(0x85D3969C),          // End color (85 alpha)
    start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
    end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
)

val beige = Brush.linearGradient(
    0.0f to Color(0xFFEBF7f4d5),          // Start color
    0.6f to Color(0xFFF7f4d5),          // Center color at 60%
    1.0f to Color(0x85F7f4d5),          // End color (85 alpha)
    start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
    end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
)

val darkGreen = Brush.linearGradient(
    0.0f to Color(0xFFEB0A3323),          // Start color
    0.6f to Color(0xFFB0A3323),          // Center color at 60%
    1.0f to Color(0x85B0A3323),          // End color (85 alpha)
    start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
    end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
)

