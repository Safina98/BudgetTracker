package com.example.budgettracker2.database

import android.util.Log
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.budgettracker2.database.model.TabunganModel
import kotlinx.coroutines.flow.Flow

@Dao
interface PocketDao {
    @Insert
    fun insertPocket(pocketTable: PocketTable): Long

    @Update
    fun updatePocket(pocket: PocketTable)
    @Query("DELETE FROM pocket_table WHERE pocket_id = :id")
    fun deletePocket(id:Int)


    @Insert
    fun insertTransaction(transactionTable: TransactionTable)

    @Query("DELETE FROM transaction_table WHERE pocket_id = :id")
    fun deleteTransaction(id:Int)
    @Query("""
    UPDATE transaction_table
    SET nominal = :newNominal
    WHERE transaction_id = (
        SELECT transaction_id
        FROM transaction_table
        WHERE pocket_id = :pocketId
        ORDER BY date ASC
        LIMIT 1
    )
""")
    suspend fun updateFirstTransactionNominal(
        pocketId: Int,
        newNominal: Int
    )

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

    @Transaction
    suspend fun updatePocketWithInitialBallace(pocket: PocketTable){
        updatePocket(pocket)
        updateFirstTransactionNominal(pocket.pocket_id,pocket.saldo)
    }
    @Transaction
    suspend fun deletePocketWithTransaction(id: Int,boolean: Boolean){
        deletePocket(id)
        if (boolean) deleteTransaction(id)
    }

}