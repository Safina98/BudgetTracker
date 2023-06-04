package com.example.budgettracker2

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.room.TypeConverter
import java.text.NumberFormat
import java.util.*
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("rupiahFormat")
fun TextView.setRupiahFormat(nominal: Int) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    val formattedNominal = formatter.format(Math.abs(nominal))
    text = formattedNominal
}
@BindingAdapter("rupiahFormatN")
fun TextView.setRupiahFormatN(nominal: Int) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("in", "ID"))
    val formattedNominal = formatter.format(nominal)
    text = formattedNominal
}

class DateTypeConverter {
    companion object {
        private const val dateFormat = "yyyy-MM-dd"
        private val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): String? {
            return date?.let { formatter.format(it) }
        }

        @TypeConverter
        @JvmStatic
        fun toDate(dateString: String?): Date? {
            return dateString?.let { formatter.parse(it) }
        }
    }
}

