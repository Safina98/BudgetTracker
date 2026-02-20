package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PrimaryButtonStyle(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // Define the colors for your gradation
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        // 1. Make the button background transparent
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
        // 2. Remove default padding so the gradient fills the whole button
        contentPadding = PaddingValues(),
        shape = RoundedCornerShape(12.dp)

    ) {
        // 3. Use a Box to draw the gradient
        Box(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(56.dp)
                .background(
                    brush = darkGreen,
                    shape = RoundedCornerShape(4.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }
    }
}