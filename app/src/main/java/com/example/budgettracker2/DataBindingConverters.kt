package com.example.budgettracker2

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.BindingConversion
import androidx.databinding.InverseBindingAdapter
import androidx.room.TypeConverter
import com.example.budgettracker2.viewModels.MainViewModel
import com.google.android.material.textfield.TextInputEditText
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
@BindingAdapter("rupiahFormat", "viewModel", requireAll = true)
fun bindRupiahFormat(textInputEditText: TextInputEditText, amount: String?, viewModel: MainViewModel) {
    val cleanString = amount?.replace("[^\\d]".toRegex(), "")
    val formattedText = formatToRupiah(cleanString ?: "")
    textInputEditText.setText(formattedText)
    textInputEditText.setSelection(formattedText.length)

    // Update the ViewModel LiveData with the cleaned numeric value
    viewModel.updateJumlah(cleanString)
}
private fun formatToRupiah(input: String): String {
    val parsed = input.toLongOrNull() ?: 0
    val formatter = NumberFormat.getCurrencyInstance(Locale("id", "ID"))
    return formatter.format(parsed).replace("IDR", "Rp")
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

