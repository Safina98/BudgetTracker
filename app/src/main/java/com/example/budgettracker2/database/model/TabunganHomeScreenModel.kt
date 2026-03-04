package com.example.budgettracker2.database.model

import androidx.room.Embedded
import com.example.budgettracker2.database.table.PocketTable

data class TabunganHomeScreenModel(
    @Embedded
    val pocketTable: PocketTable,
    val thisYearIncome: Int,
    val thisYearOutcome: Int
    )