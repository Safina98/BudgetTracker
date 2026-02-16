package com.example.budgettracker2.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.budgettracker2.tipe


@Entity(tableName = "category_table")

data class  CategoryTable (

    @PrimaryKey(autoGenerate = true)
    var category_id: Int = 0,
    @ColumnInfo(name = "category_name")
    var category_name: String = "empty",
    @ColumnInfo(name = "category_type")
    var category_type: String = "empty",
    @ColumnInfo(name = "category_color")
    var category_color: String = "empty"
){
    @Ignore
    constructor() : this(0, "empty", "empty", "BLUE")
}


