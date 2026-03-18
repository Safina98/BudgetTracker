package com.example.budgettracker2.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.budgettracker2.ui.theme.values.fonts.MyFontFamily
@Composable
fun BudgetTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = Color(0xFF887d77),  // Same as light mode for consistency
            secondary = Color(0xFFFFFFFF)
        )
    } else {
        lightColorScheme(
            primary = Color(0xFF887d77),
            secondary = Color(0xFF887d77)
        )
    }

    CompositionLocalProvider(
        LocalPocketBrushes provides mapOf(
            "Dusty Rose" to PocketBrushes.dustyRose,
            "Muted Sage Green" to PocketBrushes.mutedSageGreen,
            "Lavender Purple" to PocketBrushes.lavenderPurple,
            "Cream" to PocketBrushes.cream,
            "Olive" to PocketBrushes.olive,
            "New Color" to PocketBrushes.newColor,
            "Top Bar Color" to PocketBrushes.topBar,
            "Secondary Button" to PocketBrushes.secondaryButton
        )
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = AppTypography,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = if (darkTheme) PocketBrushes.cream.dark else PocketBrushes.cream.light
                    )
            ) {
                content()
            }
        }
    }
}
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = MyFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(fontFamily = MyFontFamily, fontWeight = FontWeight.Normal, fontSize = 14.sp),
    titleLarge = TextStyle(fontFamily = MyFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp),
    titleMedium = TextStyle(fontFamily = MyFontFamily, fontWeight = FontWeight.Bold, fontSize = 18.sp),
    labelSmall = TextStyle(fontFamily = MyFontFamily, fontWeight = FontWeight.Medium, fontSize = 11.sp),
    // ... add other styles as needed
)