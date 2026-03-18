package com.example.budgettracker2.ui.widgetstyles

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

// Arrow shape (pointed)
class ArrowShape : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val path = Path().apply {
            val width = size.width
            val height = size.height
            val arrowWidth = width * 0.2f // Arrow point width

            // Start from top-left
            moveTo(0f, 0f)
            // Top line to arrow point
            lineTo(width - arrowWidth, 0f)
            // Arrow top point
            lineTo(width, height / 2)
            // Arrow bottom point
            lineTo(width - arrowWidth, height)
            // Bottom line
            lineTo(0f, height)
            // Close back to start
            close()
        }
        return Outline.Generic(path)
    }
}
@Composable
fun ArrowButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    backgroundColor: Color = Color(0xFF6750A4),
    isCircle: Boolean = false
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        shape = ArrowShape(),
                elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 24.dp,
        pressedElevation = 8.dp,
        hoveredElevation = 6.dp
    ),
    ) {}
}

// Usage examples:
