package com.example.budgettracker2.database.model

import androidx.room.Embedded
import com.example.budgettracker2.database.PocketTable

data class TabunganModel(
    @Embedded
    val pocketTable: PocketTable,
    val currentBallance: Int,

    )