package com.example.budgettracker2.database.table

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Ignore
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.budgettracker2.DateTypeConverter
import com.example.budgettracker2.database.table.PocketTable
import java.util.Date

@Entity(tableName = "transaction_table",
    foreignKeys = [ForeignKey(
        entity = CategoryTable::class,
        parentColumns = ["category_id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.Companion.CASCADE,
        onUpdate = ForeignKey.Companion.CASCADE
    ),
        ForeignKey(
            entity = PocketTable::class,
            parentColumns = ["pocket_id"],
            childColumns = ["pocket_id"],
            onDelete = ForeignKey.Companion.CASCADE,
            onUpdate = ForeignKey.Companion.CASCADE
        )]
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