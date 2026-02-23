package com.example.budgettracker2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao{
    @Insert
    fun insert(TransactionTable: TransactionTable)
    @Update
    fun update(TransactionTable: TransactionTable)
    @Delete
    fun delete2(t:TransactionTable)

    @Query("SELECT * FROM transaction_table WHERE transaction_id =:id ")
    fun getTransById(id:Int):TransactionTable

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactionTableCoba(): Flow<List<TransactionTable>>

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactionTable(): List<TransactionTable>
    // // SELECT :brand_name_ as brand_name, (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1) as cath_code FROM brand_table WHERE NOT EXISTS(SELECT brand_name,cath_code FROM brand_table WHERE brand_name =:brand_name_ AND cath_code = (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1)) LIMIT 1 ")


    @Query("SELECT t.transaction_id AS id, c.category_name AS category_name_model_, t.note AS ket, t.date, t.nominal FROM transaction_table t " +
            "INNER JOIN category_table c ON t.category_id = c.category_id WHERE" +
            " (:type IS NULL OR c.category_type = :type OR :type = 'ALL') " +
            "AND (:categoryId IS NULL OR t.category_id = :categoryId) " +
            "AND (:startDate IS NULL OR t.date >= :startDate) " +
            "AND (:endDate IS NULL OR  t.date <= :endDate) ORDER BY t.date DESC")
    fun getFilteredData3(type: String?, categoryId: Int?, startDate: String?, endDate: String?): List<TransaksiModel>
    @Query("SELECT t.transaction_id AS id, c.category_name AS category_name_model_, t.note AS ket, t.date, t.nominal FROM transaction_table t " +
            "INNER JOIN category_table c ON t.category_id = c.category_id WHERE" +
            " (:type IS NULL OR c.category_type = :type OR :type = 'ALL') " +
            "AND (:categoryId IS NULL OR t.category_id = :categoryId) " +
            "AND (:startDate IS NULL OR t.date >= :startDate) " +
            "AND (:endDate IS NULL OR  t.date <= :endDate) ORDER BY t.date DESC")
    fun getFilteredDataFlow(type: String?, categoryId: Int?, startDate: String?, endDate: String?): Flow<List<TransaksiModel>>

    @Query("SELECT SUM(t.nominal)  FROM transaction_table t " +
            "INNER JOIN category_table c ON t.category_id = c.category_id WHERE" +
            " (:type IS NULL OR c.category_type = :type OR :type = 'ALL') " +
            "AND (:categoryId IS NULL OR t.category_id = :categoryId) " +
            "AND (:startDate IS NULL OR t.date >= :startDate) " +
            "AND (:endDate IS NULL OR  t.date <= :endDate) ORDER BY t.date DESC")
    fun getFilteredDataSum(type: String?, categoryId: Int?, startDate: String?, endDate: String?):Int

    @Query("SELECT ifnull(SUM(nominal),0) from transaction_table")
    fun getBuget():LiveData<Int>

    @Query("SELECT ifnull(SUM(nominal),0)  from transaction_table WHERE nominal < 0")
    fun getBugetTM():LiveData<Int>

    @Query("SELECT SUM(nominal) FROM transaction_table t JOIN category_table c ON t.category_id = c.category_id WHERE c.category_type = :categoryType AND strftime('%Y', date) = strftime('%Y', 'now')")
    fun getSumByCategoryType(categoryType: String): LiveData<Int>

    @Query("SELECT SUM(nominal) FROM transaction_table WHERE strftime('%Y', date) = strftime('%Y', 'now') AND pocket_id=1")
    fun getSumTM(): LiveData<Int>
    @Query("SELECT SUM(nominal) FROM transaction_table WHERE  pocket_id=2")
    fun getSumLp(): LiveData<Int>

}