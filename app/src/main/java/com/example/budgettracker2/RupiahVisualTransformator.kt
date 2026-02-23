package com.example.budgettracker2

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import java.text.NumberFormat
import java.util.*

class RupiahVisualTransformation : VisualTransformation {
    private val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID")).apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val inputText = text.text

        // If empty, we show "Rp " as a placeholder/prefix visually
        if (inputText.isEmpty()) {
            return TransformedText(
                AnnotatedString("Rp "),
                object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = 3 // Move cursor after "Rp "
                    override fun transformedToOriginal(offset: Int): Int = 0
                }
            )
        }

        val formatted = formatter.format(inputText.toLongOrNull() ?: 0)

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = formatted.length
            override fun transformedToOriginal(offset: Int): Int = inputText.length
        }

        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}