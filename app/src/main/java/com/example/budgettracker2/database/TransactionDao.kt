package com.example.budgettracker2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao{
    @Insert
    fun insert(TransactionTable: TransactionTable)
    @Update
    fun update(TransactionTable: TransactionTable)

    @Query("DELETE FROM transaction_table WHERE transaction_id =:trans_id")
    fun delete(trans_id:Int)

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactionTableCoba(): LiveData<List<TransactionTable>>
    // // SELECT :brand_name_ as brand_name, (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1) as cath_code FROM brand_table WHERE NOT EXISTS(SELECT brand_name,cath_code FROM brand_table WHERE brand_name =:brand_name_ AND cath_code = (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1)) LIMIT 1 ")
    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, strftime('%Y-%m-%d', transaction_table.date) as date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id")
    fun getAllTransactionsWithCategoryName(): LiveData<List<TransaksiModel>>

    @Query("SELECT ifnull(SUM(nominal),0) from transaction_table")
    fun getBuget():LiveData<Int>

    @Query("SELECT ifnull(SUM(nominal),0)  from transaction_table WHERE nominal < 0")
    fun getBugetTM():LiveData<Int>

    @Query("SELECT SUM(nominal) FROM transaction_table t JOIN category_table c ON t.category_id = c.category_id WHERE c.category_type = :categoryType")
    fun getSumByCategoryType(categoryType: String): LiveData<Int>


}