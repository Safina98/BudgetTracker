package com.example.budgettracker2.ui.theme

import androidx.compose.ui.geometry.Offset

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.budgettracker2.ui.theme.PocketBrushes.newColor
import com.example.budgettracker2.ui.theme.PocketBrushes.opaqueDustyRose


// Pocket Colors & Brushes
object PocketPalette {

    val topBarEndLight=Color(0xF2bea683)
    val topBarCenterLight= Color(0x5Cbea683)
    val topBarEndDark=Color(0xFF1C1610)
    val topBarCenterDark= Color(0xFF2C2219)

    val secondaryButtonEndLight=Color(0xFFD8A48B)
    val secondaryButtonCenterLight= Color(0xBE7D3E5C)
    val secondaryButtonEndDark=Color(0xFF402615)
    val secondaryButtonCenterDark= Color(0xFF402615)

    val dustyRoseEndLight=Color(0xFFEBD3969C)
    val dustyRoseCenterLight= Color(0x9CC496A1)
    val dustyRoseEndDark=Color(0xFF151515)
    val dustyRoseCenterDark= Color(0xFF391214)

    val oDustyRoseEndLight=Color(0xFFD3969C)
    val oDustyRoseCenterLight= Color(0xFFC496A1)
    val oDustyRoseEndDark=Color(0xFF151515)
    val oDustyRoseCenterDark= Color(0xFF391214)

    val oliveEndLight=Color(0xFF81785a)
    val oliveCenterLight= Color(0x8581785a)
    val oliveEndDark=Color(0xFF070707)
    val oliveCenterDark= Color(0xFF070707)

    val creamEndLight=Color(0x99fffdd0)
    val creamCenterLight= Color(0x5Cfefefa)
    val creamEndDark=Color(0xFF1C1610)
    val creamCenterDark= Color(0xFF3A2E21)

    val mutedSageEndLight=Color(0xFF919D85)
    val mutedSageCenterLight= Color(0x85919D85)
    val mutedSageEndDark=Color(0xFF44422d)
    val mutedSageCenterDark= Color(0xFF44422d)

    val lavenderEndLight=Color(0xFF9288A3)
    val lavenderCenterLight= Color(0x859288A3)
    val lavenderEndDark=Color(0xFF1E1B23)
    val lavenderCenterDark= Color(0xFF462f3f)

    val newColorEndLight=Color(0xFF5A3D55)
    val newColorCenterdLight=Color(0xFF5A3D55)
    val newColorEndDark=Color(0xFF131A21)
    val newColorCenterdDark=Color(0xFF1D2934)

//    val dustyRoseEndDark=Color(0x4A151515)
//    val dustyRoseCenterDark= Color(0xFFEBD3969C)
//    val oliveEndDark=Color(0xFF000000)
//    val oliveCenterDark= Color(0xFF81785a)
//    val creamEndDark=Color(0xFF3D3D3D)
//    val creamCenterDark= Color(0xD5FFFDD0)
//    val mutedSageEndDark=Color(0xFF070707)
//    val mutedSageCenterDark= Color(0xF2919D85)
//    val lavenderEndDark=Color(0xFF1E1B23)
//    val lavenderCenterDark= Color(0xFF9288A3)

}

data class BrushPair(val light: Brush, val dark: Brush)

object PocketBrushes {
    private fun createGradient(endColor: Color, centerColor: Color): Brush {
        return Brush.linearGradient(
            0.0f to endColor,
            0.6f to centerColor,
            1.0f to endColor,
            start = Offset(0f, Float.POSITIVE_INFINITY),
            end = Offset(Float.POSITIVE_INFINITY, 0f)
        )
    }

    val topBar = BrushPair(
        light = createGradient(PocketPalette.topBarEndLight, PocketPalette.topBarCenterLight),
        dark = createGradient(PocketPalette.topBarEndDark, PocketPalette.topBarCenterDark)
    )
    val secondaryButton = BrushPair(
        light = createGradient(PocketPalette.secondaryButtonEndLight, PocketPalette.secondaryButtonCenterLight),
        dark = createGradient(PocketPalette.secondaryButtonEndDark, PocketPalette.secondaryButtonCenterDark)
    )

    val dustyRose = BrushPair(
        light = createGradient(PocketPalette.dustyRoseEndLight, PocketPalette.dustyRoseCenterLight),
        dark = createGradient(PocketPalette.dustyRoseEndDark, PocketPalette.dustyRoseCenterDark)
    )
    val opaqueDustyRose = BrushPair(
        light = createGradient(PocketPalette.oDustyRoseEndLight, PocketPalette.oDustyRoseCenterLight),
        dark = createGradient(PocketPalette.oDustyRoseEndDark, PocketPalette.oDustyRoseCenterDark)
    )

    val mutedSageGreen = BrushPair(
        light = createGradient(PocketPalette.mutedSageEndLight, PocketPalette.mutedSageCenterLight),
        dark = createGradient(PocketPalette.mutedSageEndDark, PocketPalette.mutedSageCenterDark)
    )

    val lavenderPurple = BrushPair(
        light = createGradient(PocketPalette.lavenderEndLight, PocketPalette.lavenderCenterLight),
        dark = createGradient(PocketPalette.lavenderEndDark, PocketPalette.lavenderCenterDark)
    )

    val cream = BrushPair(
        light = createGradient(PocketPalette.creamEndLight, PocketPalette.creamCenterLight),
        dark = createGradient(PocketPalette.creamEndDark, PocketPalette.creamCenterDark)
    )


    val olive = BrushPair(
        light = createGradient(PocketPalette.oliveEndLight, PocketPalette.oliveCenterLight),
        dark = createGradient(PocketPalette.oliveEndDark, PocketPalette.oliveCenterDark)
    )
    val newColor = BrushPair(
        light = createGradient(PocketPalette.newColorEndLight, PocketPalette.newColorCenterdLight),
        dark = createGradient(PocketPalette.newColorEndDark, PocketPalette.newColorCenterdDark)
    )

    fun getBrushPair(colorName: String?): BrushPair = when (colorName) {
        "Dusty Rose" -> dustyRose
        "Muted Sage Green" -> mutedSageGreen
        "Lavender Purple" -> lavenderPurple
        "Cream" -> cream
        "Olive" -> olive
        "New Color" ->newColor
        "Top Bar Color"->topBar
        "Secondary Button"->secondaryButton
        "O Dusty Rose" -> opaqueDustyRose
        else -> dustyRose
    }
}

val LocalPocketBrushes = staticCompositionLocalOf {
    mapOf(
        "Dusty Rose" to PocketBrushes.dustyRose,
        "Muted Sage Green" to PocketBrushes.mutedSageGreen,
        "Lavender Purple" to PocketBrushes.lavenderPurple,
        "Cream" to PocketBrushes.cream,
        "Olive" to PocketBrushes.olive,
        "New Color" to newColor,
        "Top Bar Color" to PocketBrushes.topBar,
        "Secondary Button" to PocketBrushes.secondaryButton,
        "O Dusty Rose" to opaqueDustyRose
    )
}

