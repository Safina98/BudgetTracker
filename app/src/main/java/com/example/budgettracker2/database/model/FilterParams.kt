package com.example.budgettracker2.database.model

import java.util.Date

data class FilterParams(
    val tipe: String,
    val pocketId: Int?,
    val categoryId: Int?,
    val startDate: Date?,
    val endDate: Date?,
    val searchQuery: String?,
    val monthOnly: Int? // new — filter by month index regardless of year
)