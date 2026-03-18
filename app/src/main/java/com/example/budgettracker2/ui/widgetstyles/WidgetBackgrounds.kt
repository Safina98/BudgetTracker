package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.budgettracker2.ui.theme.values.fonts.creamCenterDark
import com.example.budgettracker2.ui.theme.values.fonts.creamCenterLight
import com.example.budgettracker2.ui.theme.values.fonts.creamEndDark
import com.example.budgettracker2.ui.theme.values.fonts.creamEndLight
import com.example.budgettracker2.ui.theme.values.fonts.dustyRoseCenterDark
import com.example.budgettracker2.ui.theme.values.fonts.dustyRoseCenterLight
import com.example.budgettracker2.ui.theme.values.fonts.dustyRoseEndDark
import com.example.budgettracker2.ui.theme.values.fonts.dustyRoseEndLight
import com.example.budgettracker2.ui.theme.values.fonts.lavenderCenterDark
import com.example.budgettracker2.ui.theme.values.fonts.lavenderCenterLight
import com.example.budgettracker2.ui.theme.values.fonts.lavenderEndDark
import com.example.budgettracker2.ui.theme.values.fonts.lavenderEndLight
import com.example.budgettracker2.ui.theme.values.fonts.mutedSageCenterDark
import com.example.budgettracker2.ui.theme.values.fonts.mutedSageCenterLight
import com.example.budgettracker2.ui.theme.values.fonts.mutedSageEndDark
import com.example.budgettracker2.ui.theme.values.fonts.mutedSageEndLight
import com.example.budgettracker2.ui.theme.values.fonts.oliveCenterDark
import com.example.budgettracker2.ui.theme.values.fonts.oliveCenterLight
import com.example.budgettracker2.ui.theme.values.fonts.oliveEndDark
import com.example.budgettracker2.ui.theme.values.fonts.oliveEndLight

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
    0.6f to Color(0x85B0A3323),          // Center color at 60%
    1.0f to Color(0xFFB0A3323),          // End color (85 alpha)
    start = Offset(0f, Float.POSITIVE_INFINITY), // Bottom-Left
    end = Offset(Float.POSITIVE_INFINITY, 0f)    // Top-Right
)

val dustyRoseBrushLight =  Brush.linearGradient(
    0.0f to dustyRoseEndLight,
    0.6f to dustyRoseCenterLight,
    1.0f to dustyRoseEndLight,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val mutedSageGreenBrushLight=Brush.linearGradient(
    0.0f to mutedSageEndLight,
    0.6f to mutedSageCenterLight,
    1.0f to mutedSageEndLight,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val lavenderPurpleBrushLight=Brush.linearGradient(
    0.0f to lavenderEndLight,
    0.6f to lavenderCenterLight,
    1.0f to lavenderEndLight,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val creamBrushLight=Brush.linearGradient(
    0.0f to creamEndLight,
    0.6f to creamCenterLight,
    1.0f to creamEndLight,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val oliveBrushLight=Brush.linearGradient(
    0.0f to oliveEndLight,
    0.6f to oliveCenterLight,
    1.0f to oliveEndLight,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val newColor=Brush.linearGradient(
    0.0f to Color(0xFF2C3E30),
    0.6f to Color(0xC12C3E30),
    1.0f to Color(0xFF2C3E30),
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val darkDustyRoseDark = Brush.linearGradient(
    0.0f to dustyRoseEndDark,
    0.6f to dustyRoseCenterDark,
    1.0f to dustyRoseEndDark,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val mutedSageGreenBrushDark=Brush.linearGradient(
    0.0f to mutedSageEndDark,
    0.6f to mutedSageCenterDark,
    1.0f to mutedSageEndDark,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val lavenderPurpleBrushDark=Brush.linearGradient(
    0.0f to lavenderEndDark,
    0.6f to lavenderCenterDark,
    1.0f to lavenderEndDark,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)
val creamBrushDark=Brush.linearGradient(
    0.0f to creamEndDark,
    0.6f to creamCenterDark,
    1.0f to creamEndDark,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)

val oliveBrushDark=Brush.linearGradient(
    0.0f to oliveEndDark,
    0.6f to oliveCenterDark,
    1.0f to oliveEndDark,
    start = Offset(0f, Float.POSITIVE_INFINITY),
    end = Offset(Float.POSITIVE_INFINITY, 0f)
)




