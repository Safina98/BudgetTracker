package com.example.budgettracker2.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.googlefonts.GoogleFont
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.budgettracker2.R
import com.example.budgettracker2.ui.theme.Typography
import com.example.budgettracker2.ui.theme.values.fonts.Typography



val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val AnticSlabFont = GoogleFont("Antic Slab")

val  cormorantMediumFont= GoogleFont("Cormorant")

val AnticSlabFamily = FontFamily(
    Font(googleFont = AnticSlabFont, fontProvider = provider)
)
val CormorantMedium=FontFamily(Font(googleFont = cormorantMediumFont, fontProvider = provider))

val Typography = Typography(
titleMedium = TextStyle(
fontFamily = CormorantMedium, // Global application
fontWeight = FontWeight.Medium,
fontSize = 16.sp,
lineHeight = 24.sp,
letterSpacing = 0.15.sp
)
// Add other styles here...
)