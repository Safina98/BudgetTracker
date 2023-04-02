package com.example.budgettracker2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.concurrent.Executors
/*
@Database(entities = [TabelKategori::class,TabelTransaksi::class], version = 2, exportSchema = false)
abstract class BudgetDatabase : RoomDatabase() {

    abstract val kategori_dao: KategoriDao
    abstract val transaksi_dao:TransaksiDao


    companion object{
        @Volatile
        private var INSTANCE : BudgetDatabase? = null

        fun getInstance(context: Context) : BudgetDatabase {
            synchronized(this){
                var instance = INSTANCE

                val rdc: Callback = object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        db.execSQL("INSERT INTO tabel_kategori (id,cath_name,tipe,color) VALUES( 0,'BEAUTY','PENGELUARAN','BLUE');")
                        db.execSQL("INSERT INTO tabel_kategori (id,cath_name,tipe,color) VALUES( 1,'FOOD','PENGELUARAN','PURPLE');")
                        db.execSQL("INSERT INTO tabel_kategori (id,cath_name,tipe,color) VALUES( 2,'TRANSPORTATION','PENGELUARAN','YELLOW');")
                        db.execSQL("INSERT INTO tabel_kategori (id,cath_name,tipe,color) VALUES( 3,'FASHION','PENGELUARAN','BLUE');")
                        db.execSQL("INSERT INTO tabel_kategori (id,cath_name,tipe,color) VALUES( 4,'SALARY','PEMASUKAN','GREEN');")

                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        //do something every time database is open
                    }
                }

                if (instance == null){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        BudgetDatabase::class.java,
                        "budget_database"
                    ).addCallback(rdc)
                        .fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
            db.clearAllTables()
            db.close()

// Delete the database file
            applicationContext.deleteDatabase("my_database")
        }

    }
}

 */