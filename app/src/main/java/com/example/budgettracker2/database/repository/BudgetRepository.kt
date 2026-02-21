package com.example.budgettracker2.database.repository

import com.example.budgettracker2.database.CategoryDao
import com.example.budgettracker2.database.CategoryTable
import com.example.budgettracker2.database.KategoriModel
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
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
){

    fun getAllPocket(): Flow<List<TabunganModel>> =
        pocketDao.getPocketsWithSum()


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
    suspend fun insertKategori(
        namaKategori: String,
        tipeKategori: String,
        warnaKategori: String
    ): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO){
                val category= CategoryTable().apply {
                    category_name=namaKategori
                    category_type=tipeKategori
                    category_color=warnaKategori
                }
                categoryDao.insert(category)
            }


        }

    fun getAllCategory(): Flow<List<KategoriModel>> =
        categoryDao.getAllKategoriFlow()

}

