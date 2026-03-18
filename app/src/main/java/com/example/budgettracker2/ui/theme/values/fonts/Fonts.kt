package com.example.budgettracker2.ui.theme.values.fonts

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import com.example.budgettracker2.R


val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val MyFontFamily = FontFamily(
    Font(googleFont = GoogleFont("Raleway"), fontProvider = provider)
)