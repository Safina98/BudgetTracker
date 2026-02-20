package com.example.budgettracker2

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.*
import java.text.NumberFormat
import java.util.*

class RupiahVisualTransformation : VisualTransformation {

    private val formatter = NumberFormat
        .getCurrencyInstance(Locale("id", "ID"))
        .apply {
            maximumFractionDigits = 0
            minimumFractionDigits = 0
        }

    override fun filter(text: AnnotatedString): TransformedText {

        val digits = text.text

        if (digits.isEmpty()) {
            return TransformedText(AnnotatedString(""), OffsetMapping.Identity)
        }

        val formatted = formatter.format(digits.toLong())

        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                return formatted.length
            }

            override fun transformedToOriginal(offset: Int): Int {
                return digits.length
            }
        }

        return TransformedText(
            AnnotatedString(formatted),
            offsetMapping
        )
    }
}