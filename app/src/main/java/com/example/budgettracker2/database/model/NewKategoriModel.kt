package com.example.budgettracker2.database.model

import androidx.room.Embedded
import com.example.budgettracker2.database.table.CategoryTable

data class NewKategoriModel(
    @Embedded
    val categoryTable: CategoryTable,
    val categoryCashSum:Int,
)