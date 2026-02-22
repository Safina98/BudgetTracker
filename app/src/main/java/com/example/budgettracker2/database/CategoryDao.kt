package com.example.budgettracker2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.budgettracker2.database.model.NewKategoriModel
import com.example.budgettracker2.database.model.TabunganModel
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao{

    @Insert
    fun insert(category:CategoryTable)

    @Query("INSERT INTO category_table (category_name, category_type, category_color) "+
            "SELECT :category_name as category_name, :category_type as category_type, :category_color as category_color "+
            "WHERE NOT EXISTS ("+
            "SELECT 1 "+
            "FROM category_table "+
            "WHERE category_name = :category_name"+
    ")")
    fun insertIfNotExist(category_name:String,category_type:String,category_color:String)

    @Update
    fun update(category:CategoryTable)

    @Delete
    fun delete1(c:CategoryTable)

    @Query("DELETE FROM category_table WHERE category_id = :id")
    fun delete(id:Int)

    @Query("DELETE FROM transaction_table WHERE category_id = :id")
    fun deleteTransactions(id:Int)


    @Query("SELECT * FROM category_table WHERE category_id =:id")
    fun getCategory(id:Int):CategoryTable

    @Query("SELECT category_table.category_id as id_, " +
            "category_table.category_name as category_name_, " +
            "category_table.category_type as category_type_, " +
            "category_table.category_color as category_color_, " +
            "SUM(transaction_table.nominal) as sum FROM category_table " +
            "LEFT JOIN transaction_table" +
            " ON category_table.category_id = transaction_table.category_id" +
            " WHERE strftime('%Y', date) = strftime('%Y', 'now')" +
            " GROUP BY category_table.category_id ")
    fun getAllKategori(): LiveData<List<KategoriModel>>

    @Query("""
        SELECT category_table.*, SUM(transaction_table.nominal) AS categoryCashSum 
        FROM category_table
        LEFT JOIN transaction_table ON category_table.category_id = transaction_table.category_id
        GROUP BY category_table.category_id
    """)
    fun getAllKategoriFlow(): Flow<List<NewKategoriModel>>

    @Query("SELECT category_name FROM category_table WHERE category_type = :tipe")
    fun getKategoriNameD(tipe:String):List<String>

    @Query("SELECT category_id FROM category_table WHERE category_name = :name")
    fun getCategoryIdByName(name: String): Int

    @Query("SELECT * FROM category_table WHERE category_name = :name")
    fun getCategoryByName(name: String): CategoryTable

    @Transaction
    fun deleteCategoryWithTransaction(id:Int,boolean: Boolean){
        delete(id)
        if (boolean)deleteTransactions(id)
    }



}