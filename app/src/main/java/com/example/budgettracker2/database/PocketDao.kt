package com.example.budgettracker2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PocketDao {
    @Insert
    fun insert(pocketTable: PocketTable)

    @Query("SELECT pocket_name FROM pocket_table")
    fun getAllPocketName():List<String>

    @Query("SELECT pocket_id FROM pocket_table WHERE pocket_name =:name")
    fun getIdByPocketName(name:String):Int

    @Query("SELECT * FROM pocket_table WHERE pocket_id =:id")
    fun getPocketById(id:Int):PocketTable?

    @Query("SELECT * FROM pocket_table")
    fun getAllPocket(): Flow<List<PocketTable>>



}