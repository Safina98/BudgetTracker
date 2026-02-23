package com.example.budgettracker2

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
val warnaList = listOf("Pink","Yellow","Blue","Green","Purple")

object WARNA {
    const val PINK = 0
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

