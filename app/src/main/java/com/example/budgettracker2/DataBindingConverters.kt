package com.example.budgettracker2

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.NumberFormat
import java.util.*

@BindingAdapter("rupiahFormat")
fun TextView.setRupiahFormat(nominal: Int) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    val formattedNominal = formatter.format(nominal)
    text = formattedNominal
}
