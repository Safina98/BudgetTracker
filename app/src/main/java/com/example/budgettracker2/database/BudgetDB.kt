package com.example.budgettracker2.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.budgettracker2.database.Migrations.MIGRATION_2_3
import com.example.budgettracker2.database.dao.CategoryDao
import com.example.budgettracker2.database.dao.PocketDao
import com.example.budgettracker2.database.dao.TransactionDao
import com.example.budgettracker2.database.table.CategoryTable
import com.example.budgettracker2.database.table.PocketTable
import com.example.budgettracker2.database.table.TransactionTable
import java.util.concurrent.Executors

@Database(entities = [CategoryTable::class, TransactionTable::class, PocketTable::class], version = 1, exportSchema = false)
abstract class BudgetDB : RoomDatabase() {

    abstract val category_dao: CategoryDao
    abstract val transaction_dao: TransactionDao
    abstract val pocket_dao: PocketDao
    companion object{
        @Volatile
        private var INSTANCE : BudgetDB? = null
        fun destroyInstance() {
            INSTANCE = null
        }
        fun getInstance(context: Context) : BudgetDB {
            synchronized(this){
                var instance = INSTANCE

                val rdc: Callback = object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        db.execSQL("INSERT INTO category_table (category_id,category_name,category_type,category_color) VALUES( 1,'BEAUTY','PENGELUARAN','BLUE');")
                        db.execSQL("INSERT INTO category_table (category_id,category_name,category_type,category_color) VALUES( 2,'FOOD','PENGELUARAN','PURPLE');")
                        db.execSQL("INSERT INTO category_table (category_id,category_name,category_type,category_color) VALUES( 3,'TRANSPORTATION','PENGELUARAN','YELLOW');")
                        db.execSQL("INSERT INTO category_table (category_id,category_name,category_type,category_color) VALUES( 4,'FASHION','PENGELUARAN','BLUE');")
                        db.execSQL("INSERT INTO category_table (category_id,category_name,category_type,category_color) VALUES( 5,'SALARY','PEMASUKAN','GREEN');")
                        db.execSQL("INSERT INTO category_table (category_id,category_name,category_type,category_color) VALUES( 6,'INITIAL SAVING','PEMASUKAN','GREEN');")
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        //do something every time database is open
                    }
                }
                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BudgetDB::class.java,
                        "budget_db"
                    ).addCallback(rdc)
                       // .fallbackToDestructiveMigration()
                    .build()
                    INSTANCE = instance
                }
                return instance
            }

        }

    }
}

