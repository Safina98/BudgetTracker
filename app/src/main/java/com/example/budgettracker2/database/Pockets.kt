package com.example.budgettracker2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey


@Entity(tableName = "pocket_table")
data class PocketTable (
    @PrimaryKey(autoGenerate = true)
    var pocket_id:Int=0,
    @ColumnInfo(name = "pocket_name")
    var pocketName:String="",
    @ColumnInfo(name = "saldo")
    var saldo:Int=0
)
