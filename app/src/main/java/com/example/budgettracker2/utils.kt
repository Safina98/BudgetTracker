package com.example.budgettracker2

import com.example.budgettracker2.ui.widgetstyles.creamBrushLight
import com.example.budgettracker2.ui.widgetstyles.dustyRoseBrushLight
import com.example.budgettracker2.ui.widgetstyles.lavenderPurpleBrushLight
import com.example.budgettracker2.ui.widgetstyles.mutedSageGreenBrushLight
import com.example.budgettracker2.ui.widgetstyles.oliveBrushLight
import java.text.NumberFormat
import java.util.Locale

object TIPETRANSAKSI {
    const val keluar= "PENGELUARAN"
    const val masuk = "PEMASUKAN"
    const val TRANSFER = "TRANSFER"
}
val tipeList = listOf("PEMASUKAN", "PENGELUARAN", "TRANSFER")
val warnaList = listOf("Dusty Rose","Muted Sage Green","Lavender Purple","Cream","Olive","New Color")
val warnaListOld = listOf("Pink","Yellow","Blue","Green","Purple")
val pocketColors = mapOf(
    "Dusty Rose" to dustyRoseBrushLight,
    "Muted Sage Green" to mutedSageGreenBrushLight,
    "Lavender Purple" to lavenderPurpleBrushLight,
    "Cream" to creamBrushLight,
    "Olive" to oliveBrushLight
)
object WARNA {
    const val PINK = "cream"
    const val YELLOW = 1
    const val BLUE = 2
    const val GREEN = 2
    const val PURPLE = 2
}
val rupiahFormatter = NumberFormat
    .getCurrencyInstance(Locale("id", "ID"))
    .apply {
        maximumFractionDigits = 0
        minimumFractionDigits = 0
    }
