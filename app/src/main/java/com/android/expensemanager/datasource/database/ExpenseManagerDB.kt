package com.android.expensemanager.datasource.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.android.expensemanager.StringConstants
import com.android.expensemanager.ui.categories.data.CategoriesListDao
import com.android.expensemanager.ui.categories.model.Category
import com.android.expensemanager.ui.expense.data.ExpenseDao
import com.android.expensemanager.ui.expense.model.ExpenseDetails

@Database(entities = [Category::class, ExpenseDetails::class], version = 4, exportSchema = false)
abstract class ExpenseManagerDB : RoomDatabase()
{
    /** DATABASE TABLES*/
    abstract fun categoriesListDao()  : CategoriesListDao
    abstract fun expenseDao()  : ExpenseDao

    companion object
    {
        private var INSTANCE: ExpenseManagerDB? = null

        fun getInstance(context: Context): ExpenseManagerDB
        {
            synchronized(Any())
            {
                if (INSTANCE == null)
                {
                    INSTANCE = Room
                        .databaseBuilder(context.applicationContext, ExpenseManagerDB::class.java, StringConstants.DB_NAME)
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }

            return INSTANCE as ExpenseManagerDB
        }
    }
}
