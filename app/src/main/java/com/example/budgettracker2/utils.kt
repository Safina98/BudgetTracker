package com.example.budgettracker2

import com.example.budgettracker2.ui.widgetstyles.cream
import com.example.budgettracker2.ui.widgetstyles.dustyRose
import com.example.budgettracker2.ui.widgetstyles.lavenderPurple
import com.example.budgettracker2.ui.widgetstyles.mutedSageGreen
import com.example.budgettracker2.ui.widgetstyles.olive
import java.text.NumberFormat
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.time.Instant

object TIPETRANSAKSI {
    const val keluar= "PENGELUARAN"
    const val masuk = "PEMASUKAN"
    const val TRANSFER = "TRANSFER"

}

val tipeList = listOf("PEMASUKAN", "PENGELUARAN", "TRANSFER")
val warnaList = listOf("Dusty Rose","Muted Sage Green","Lavender Purple","Cream","Olive")
val warnaListOld = listOf("Pink","Yellow","Blue","Green","Purple")
val pocketColors = mapOf(
    "Dusty Rose" to dustyRose,
    "Muted Sage Green" to mutedSageGreen,
    "Lavender Purple" to lavenderPurple,
    "Cream" to cream,
    "Olive" to olive
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



