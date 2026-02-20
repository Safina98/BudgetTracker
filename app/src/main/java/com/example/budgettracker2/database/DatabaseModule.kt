package com.example.budgettracker2.database

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): BudgetDB {
        return BudgetDB.getInstance(context)
    }

    @Provides
    fun providePocketDao(db: BudgetDB): PocketDao {
        return db.pocket_dao
    }

}