package com.example.budgettracker2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.example.budgettracker2.database.model.TabunganModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PocketDao {
    @Insert
    fun insertPocket(pocketTable: PocketTable): Long

    @Insert
    fun insertTransaction(transactionTable: TransactionTable)

    @Query("SELECT pocket_name FROM pocket_table")
    fun getAllPocketName():List<String>

    @Query("SELECT pocket_id FROM pocket_table WHERE pocket_name =:name")
    fun getIdByPocketName(name:String):Int

    @Query("SELECT * FROM pocket_table WHERE pocket_id =:id")
    fun getPocketById(id:Int):PocketTable?

    @Query("SELECT * FROM pocket_table")
    fun getAllPocket(): Flow<List<PocketTable>>

    @Query("""
        SELECT pocket_table.*, SUM(transaction_table.nominal) AS currentBallance 
        FROM pocket_table 
        LEFT JOIN transaction_table ON pocket_table.pocket_id = transaction_table.pocket_id 
        GROUP BY pocket_table.pocket_id
    """)
    fun getPocketsWithSum(): Flow<List<TabunganModel>>

    @Transaction
    suspend fun insertPocketWithInitialBalance(
        pocket: PocketTable,
        transaction: TransactionTable
    ) {
        val id = insertPocket(pocket)
        transaction.pocket_id = id.toInt()
        insertTransaction(transaction)
    }

}