package com.example.budgettracker2.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CategoryDao{

    @Insert
    fun insert(category:CategoryTable)
    //@Insert
    //fun insertInit(category:CategoryTable)
    @Update
    fun update(category:CategoryTable)

     @Query("SELECT category_id as id_,category_name as category_name_,category_type as category_type_,category_color as category_color_ FROM category_table")
     fun getAllKategori(): LiveData<List<KategoriModel>>
    @Query("SELECT * FROM category_table")
    fun getAllKategoriCoba():LiveData<List<CategoryTable>>

    @Query("SELECT category_name FROM category_table WHERE category_type = :tipe")
    fun getKategoriName(tipe:String):LiveData<List<String>>
    @Query("SELECT category_name FROM category_table")
    fun getAllKategoriName():LiveData<List<String>>

}