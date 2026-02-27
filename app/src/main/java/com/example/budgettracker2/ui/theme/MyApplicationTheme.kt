package com.example.budgettracker2.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography, // This is your custom Typography object
        content = content
    )
}