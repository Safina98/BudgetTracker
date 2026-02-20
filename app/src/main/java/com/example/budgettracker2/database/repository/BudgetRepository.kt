package com.example.budgettracker2.database.repository

import com.example.budgettracker2.database.PocketDao
import com.example.budgettracker2.database.PocketTable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val pocketDao: PocketDao
){

    suspend fun insertPocket(pocket: PocketTable){
        withContext(Dispatchers.IO){
            pocketDao.insert(pocket)
        }
    }

    fun getAllPocket(): Flow<List<PocketTable>> =
        pocketDao.getAllPocket()

}
