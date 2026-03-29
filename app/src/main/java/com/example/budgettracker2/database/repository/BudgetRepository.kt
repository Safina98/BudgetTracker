package com.example.budgettracker2.database.repository

import android.util.Log
import com.example.budgettracker2.database.dao.CategoryDao
import com.example.budgettracker2.database.table.CategoryTable
import com.example.budgettracker2.database.dao.PocketDao
import com.example.budgettracker2.database.table.PocketTable
import com.example.budgettracker2.database.dao.TransactionDao
import com.example.budgettracker2.database.table.TransactionTable
import com.example.budgettracker2.database.TransaksiModel
import com.example.budgettracker2.database.model.BackupData
import com.example.budgettracker2.database.model.NewKategoriModel
import com.example.budgettracker2.database.model.TabunganHomeScreenModel
import com.example.budgettracker2.database.model.TabunganModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import com.google.gson.JsonPrimitive
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.lang.reflect.Type
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class BudgetRepository @Inject constructor(
    private val pocketDao: PocketDao,
    private val transactionDao: TransactionDao,
    private val categoryDao: CategoryDao
){
    // Custom Gson instance that handles Date serialization
// Room stores Date as String ("yyyy-MM-dd"), Gson needs to know how to convert it
    private val gson = GsonBuilder()
        .registerTypeAdapter(Date::class.java, object : JsonSerializer<Date>, JsonDeserializer<Date> {
            private val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            // Convert Date object → JSON string e.g. "2024-03-27"
            override fun serialize(src: Date?, typeOfSrc: Type?, context: JsonSerializationContext?) =
                JsonPrimitive(src?.let { format.format(it) })

            // Convert JSON string "2024-03-27" → Date object
            override fun deserialize(json: JsonElement?, typeOfT: Type?, context: JsonDeserializationContext?) =
                json?.asString?.let { format.parse(it) }
        })
        .create()

    // Reads all data from Room and converts it to a JSON string
// Called by BackupViewModel before uploading to Drive
    suspend fun exportToJson(): String = withContext(Dispatchers.IO) {
        val backup = BackupData(
            categories = categoryDao.getAllCategories(),
            pockets = pocketDao.getAllPockets(),
            transactions = transactionDao.getAllTransactionTable()
        )
        gson.toJson(backup) // converts BackupData → JSON string
    }

    // Parses JSON string and restores all data into Room
// Called by BackupViewModel after downloading from Drive
    suspend fun importFromJson(json: String) = withContext(Dispatchers.IO) {
        val backup = gson.fromJson(json, BackupData::class.java)


        backup.pockets.forEach {
            Log.i("backupGson","${it}")
        }
       backup.categories.forEach {
           Log.i("backupGson","${it}")
       }
        backup.transactions.forEach {
          //  Log.i("backupGson","${it}")
        }
        // Run everything in one transaction so all operations share the same connection
        // This ensures PRAGMA foreign_keys = OFF applies to the same connection as inserts
        transactionDao.runInTransaction {
            //transactionDao.disableForeignKeys()

            transactionDao.deleteAll()
            categoryDao.deleteAll()
            pocketDao.deleteAll()

            categoryDao.insertAll(backup.categories)
            pocketDao.insertAll(backup.pockets)
            transactionDao.insertAll(backup.transactions)


            //transactionDao.enableForeignKeys()
        }
    }

    // Helper to run a block inside a Room transaction


    fun getAllPocket(): Flow<List<TabunganModel>> =
        pocketDao.getPocketsWithSum()

    fun getPocketNameListFilter(): Flow<List<String>> {
        return pocketDao.getAllPocketNameFlow().map { list ->
            listOf("Semua") + list
        }
    }
    fun getAllPocketName(): Flow<List<String>> =
        pocketDao.getAllPocketNameFlow()
    suspend fun getPocketIdByName(name:String): Int? {
        return withContext(Dispatchers.IO){
            pocketDao.getIdByPocketName(name)
        }
    }
    fun getThisYearPocketSum(): Flow<List<TabunganHomeScreenModel>> =
        pocketDao.getPocketsThisYearWithSum()


    suspend fun insertPocketWithInitialBalance(
        name: String,
        color:String,
        index:Int,
        mainPocket: Boolean,
        saldo: Int
    ): Result<Unit> =
        runCatching {
            val pocket = PocketTable().apply {
                pocketName = name
                this.saldo = saldo
                this.mainPocket =mainPocket
                this.index=index
                this.color=color
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
        color:String,
        index:Int,
        mainPocket: Boolean,
        saldo: Int
    ): Result<Unit> =
        runCatching {
            val pocket = PocketTable().apply {
                pocket_id=pocketId
                pocketName = name
                this.saldo = saldo
                this.mainPocket =mainPocket
                this.index=index
                this.color=color
            }
            Log.i("PocketColor","repo update: ${pocket.color}")

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

    fun getThisYearCategorySum(): Flow<List<NewKategoriModel>> =
        categoryDao.getAllKategoriThisYearFlow()


    suspend fun getCategoryIdByName(name:String):Int{
        return withContext(Dispatchers.IO){
            categoryDao.getCategoryIdByName(name)
        }
    }
    fun getCategoryNameListFilter(tipe:String): Flow<List<String>> {
        return categoryDao.getKategoriNameByTipe(tipe).map { list ->
            listOf("Semua") + list
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
    suspend fun getTransactionById(id: Int): Result<TransaksiModel?> {
        return runCatching {
            withContext(Dispatchers.IO) {
                transactionDao.getSelectedTransaction(id)
            }
        }
    }
    suspend fun deleteTransaction(
        transactionId:Int
    ): Result<Unit> =
        runCatching {
            withContext(Dispatchers.IO){
                transactionDao.delete(transactionId)
            }
        }

    fun getAllTransaction(): Flow<List<TransaksiModel>> =
        transactionDao.getFilteredDataFlow(null,null,null,null)

    fun getFilteredTransactions(
        tipe: String?,
        pocketId: Int?,
        categoryId: Int?,
        startDate:Date?,
        endDate:Date?,
        searchQuery:String?,
        monthOnly:Int?
    ): Flow<List<TransaksiModel>> =
        transactionDao.getFilteredTransactions(tipe, pocketId, categoryId, startDate, endDate,searchQuery,monthOnly)


// In BudgetRepository


}

