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

