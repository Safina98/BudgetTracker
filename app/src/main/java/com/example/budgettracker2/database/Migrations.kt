package com.example.budgettracker2.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object Migrations {
    val MIGRATION_2_3 = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL(
                """
            CREATE TABLE IF NOT EXISTS pocket_table (
                pocket_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                pocket_name TEXT NOT NULL,
                saldo INTEGER NOT NULL
            )
            """
            )
            database.execSQL(
                """
            CREATE TABLE transaction_table_new (
                transaction_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                category_id INTEGER,
                pocket_id INTEGER,
                note TEXT NOT NULL,
                date TEXT NOT NULL,
                nominal INTEGER NOT NULL,
                FOREIGN KEY(category_id) REFERENCES category_table(category_id) 
                    ON DELETE CASCADE ON UPDATE CASCADE,
                FOREIGN KEY(pocket_id) REFERENCES pocket_table(pocket_id) 
                    ON DELETE CASCADE ON UPDATE CASCADE
            )
            """
            )

                // Step 2: Copy data from the old table to the new table
            database.execSQL(
                """
            INSERT INTO transaction_table_new (transaction_id, category_id, note, date, nominal)
            SELECT transaction_id, category_id, note, date, nominal
            FROM transaction_table
            """
            )

            // Step 3: Drop the old table
            database.execSQL("DROP TABLE transaction_table")

            // Step 4: Rename the new table to the old table's name
            database.execSQL("ALTER TABLE transaction_table_new RENAME TO transaction_table")
        }
    }
}