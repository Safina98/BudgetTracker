package com.example.budgettracker2.database.repository

import com.example.budgettracker2.database.PocketDao
import com.example.budgettracker2.database.PocketTable
import com.example.budgettracker2.database.TransactionDao
import com.example.budgettracker2.database.TransactionTable
import com.example.budgettracker2.database.model.TabunganModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.util.Date
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val pocketDao: PocketDao,
    private val transactionDao: TransactionDao
){

    suspend fun insertPocket(pocket: PocketTable): Long{
        return withContext(Dispatchers.IO){
            pocketDao.insertPocket(pocket)
        }
    }

    fun getAllPocket(): Flow<List<TabunganModel>> =
        pocketDao.getPocketsWithSum()


    suspend fun insertTransaction(transaction: TransactionTable){
        withContext(Dispatchers.IO){
            transactionDao.insert(transaction)
        }
    }
    suspend fun insertPocketWithInitialBalance(
        name: String,
        saldo: Int
    ): Result<Unit> =
        runCatching {
            val pocket = PocketTable().apply {
                pocketName = name
                this.saldo = saldo
            }
            val transaction = TransactionTable().apply {
                nominal = saldo
                note = "Saldo Awal $name"
                date = Date()
                tipe = "PEMASUKAN"
                category_id = 5
            }

            pocketDao.insertPocketWithInitialBalance(
                pocket,
                transaction
            )
        }


}
