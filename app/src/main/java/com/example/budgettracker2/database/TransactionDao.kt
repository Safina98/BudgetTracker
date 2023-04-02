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

    @Query("SELECT * FROM transaction_table")
    fun getAllTransactionTableCoba(): LiveData<List<TransactionTable>>
    // // SELECT :brand_name_ as brand_name, (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1) as cath_code FROM brand_table WHERE NOT EXISTS(SELECT brand_name,cath_code FROM brand_table WHERE brand_name =:brand_name_ AND cath_code = (SELECT category_id FROM category_table WHERE category_name = :caht_name_ limit 1)) LIMIT 1 ")
    @Query("SELECT transaction_table.transaction_id as id, category_table.category_name as category_name_model_, transaction_table.note as ket, transaction_table.date, transaction_table.nominal FROM transaction_table JOIN category_table ON transaction_table.category_id = category_table.category_id")
    fun getAllTransactionsWithCategoryName(): LiveData<List<TransaksiModel>>


}