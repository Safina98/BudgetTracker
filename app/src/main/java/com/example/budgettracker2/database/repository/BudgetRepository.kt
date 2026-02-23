package com.example.budgettracker2.database.repository

import android.util.Log
import com.example.budgettracker2.database.CategoryDao
import com.example.budgettracker2.database.CategoryTable
import com.example.budgettracker2.database.KategoriModel
import com.example.budgettracker2.database.PocketDao
import com.example.budgettracker2.database.PocketTable
import com.example.budgettracker2.database.TransactionDao
import com.example.budgettracker2.database.TransactionTable
import com.example.budgettracker2.database.TransaksiModel
import com.example.budgettracker2.database.model.NewKategoriModel
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
    fun getAllPocketName(): Flow<List<String>> =
        pocketDao.getAllPocketNameFlow()
    suspend fun getPocketIdByName(name:String): Int? {
        return withContext(Dispatchers.IO){
            pocketDao.getIdByPocketName(name)
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
    suspend fun updatePocketWithInitialBalance(
        pocketId:Int,
        name: String,
        saldo: Int
    ): Result<Unit> =
        runCatching {
            val pocket = PocketTable().apply {
                pocket_id = pocketId
                pocketName = name
                this.saldo = saldo
            }

            pocketDao.updatePocketWithInitialBallace(pocket)
        }

    suspend fun deleteTabungan(
        kategoriId:Int, deleteTransaction: Boolean
    ): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO){
                pocketDao.deletePocketWithTransaction(kategoriId, deleteTransaction)
            }
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
    suspend fun updateKategori(
        kategoriId:Int,
        namaKategori: String,
        tipeKategori: String,
        warnaKategori: String
    ): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO){
                val category= CategoryTable().apply {
                    category_id=kategoriId
                    category_name=namaKategori
                    category_type=tipeKategori
                    category_color=warnaKategori
                }
                categoryDao.update(category)
            }
        }
    suspend fun deleteKategori(
        kategoriId:Int,deleteTransaction: Boolean
    ): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO){
                categoryDao.deleteCategoryWithTransaction(kategoriId, deleteTransaction)
            }
        }
    fun getAllCategory(): Flow<List<NewKategoriModel>> =
        categoryDao.getAllKategoriFlow()
    suspend fun getCategoryIdByName(name:String):Int{
        return withContext(Dispatchers.IO){
            categoryDao.getCategoryIdByName(name)
        }

    }

    fun getCategoryNamebyTipe(tipe:String): Flow<List<String>> =
        categoryDao.getKategoriNameByTipe(tipe)


    suspend fun upsertTransaction(
        transactionId: Int?,
        transactionTable: TransactionTable,
        kategoriName: String,
        tabunganName: String
    ): Result<Unit> { // Return Result to be used by the ViewModel
        return runCatching {
            withContext(Dispatchers.IO) {
                // Fetch IDs based on names
                val pocketId = getPocketIdByName(tabunganName)
                val categoryId = getCategoryIdByName(kategoriName)

                // Assign foreign keys to the table object
                transactionTable.pocket_id = pocketId
                transactionTable.category_id = categoryId

                if (transactionId == null || transactionId == 0) {
                    transactionDao.insert(transactionTable)
                } else {
                    transactionTable.transaction_id = transactionId
                    transactionDao.update(transactionTable)
                }
            }
        }
    }
    fun getAllTransaction(): Flow<List<TransaksiModel>> =
        transactionDao.getFilteredDataFlow(null,null,null,null)

}

