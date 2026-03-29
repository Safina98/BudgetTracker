package com.example.budgettracker2.database.model

import com.example.budgettracker2.database.table.CategoryTable
import com.example.budgettracker2.database.table.PocketTable
import com.example.budgettracker2.database.table.TransactionTable

data class BackupData(
    val transactions: List<TransactionTable>,
    val categories: List<CategoryTable>,
    val pockets: List<PocketTable>
)