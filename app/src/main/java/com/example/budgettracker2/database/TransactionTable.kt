package com.example.budgettracker2.database

import androidx.room.*
import com.example.budgettracker2.DateTypeConverter
import java.util.*

@Entity(tableName = "transaction_table",
    foreignKeys = [ForeignKey(entity =CategoryTable::class,
        parentColumns = ["category_id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE),
   ForeignKey(entity =PocketTable::class,
        parentColumns = ["pocket_id"],
        childColumns = ["pocket_id"],
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE)]
    )
@TypeConverters(DateTypeConverter::class)
data class TransactionTable(
    @PrimaryKey(autoGenerate = true)
    var transaction_id:Int = 0,
    @ColumnInfo(name = "category_id")
    var category_id:Int? = null,
    @ColumnInfo(name = "pocket_id")
    var pocket_id:Int? = null, //newly added
    @ColumnInfo(name = "note")
    var note:String="",
    @ColumnInfo(name = "date")
    var date: Date = Date(),
    @ColumnInfo(name = "nominal")
    var nominal:Int=0,
    @ColumnInfo(name = "tipe")
    var tipe: String=""

){
    @Ignore
    constructor() : this(0, 0,null, "empty", Date(),0)
}



