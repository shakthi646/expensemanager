package com.android.expensemanager.datasource.database

import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.LiveData
import com.android.expensemanager.common.AppExecutors
import com.android.expensemanager.constants.APIConstants
import com.android.expensemanager.ui.categories.model.Category
import com.android.expensemanager.ui.expense.model.ExpenseDetails

class DatabaseAccessor(private val mDB : ExpenseManagerDB) : LocalDataSource
{
    override fun getEntityAmountDetails(entityId: Int, transactionType: String, callback: LocalDataSource.DataLoadedCallback)
    {
        appExecutors.diskIO.execute{

            val detail = mDB.expenseDao().getCurrentMonthTransactionAmount(entityId, transactionType)

            appExecutors.mainThread.execute{

                callback.onDataLoaded(detail)
            }
        }
    }

    private val appExecutors : AppExecutors = AppExecutors()

    override fun getEntityList(entityConstant: Int, callback: LocalDataSource.DataLoadedCallback)
    {
        when(entityConstant)
        {
            APIConstants.CATEGORIES_LIST ->
            {
                appExecutors.diskIO.execute{

                    val categoriesList = mDB.categoriesListDao().getAllCategoryList()

                    appExecutors.mainThread.execute{

                        callback.onDataLoaded(categoriesList)
                    }

                }
            }
            APIConstants.TRANSACTION_LIST ->
            {
                appExecutors.diskIO.execute{

                    val transactionList = mDB.expenseDao().getAllTransactionsList()

                    appExecutors.mainThread.execute{

                        callback.onDataLoaded(transactionList)
                    }

                }
            }
        }
    }

    override fun saveEntityList(entityConstant: Int, data: K, callback: LocalDataSource.DataStoredCallback)
    {

        appExecutors.diskIO.execute{

            when(entityConstant)
            {
                APIConstants.CATEGORIES_LIST ->
                {
                    mDB.categoriesListDao().storeCategoryList(data as List<Category>)
                    appExecutors.mainThread.execute{
                        callback.onDataSaved()
                    }
                }
            }
        }
    }


    override fun getEntityDetails(entityConstant: Int, entityId: String, callback: LocalDataSource.DataLoadedCallback)
    {
        when(entityConstant)
        {

            APIConstants.CATEGORIES_LIST ->
            {
                callback.onDataLoaded(mDB.categoriesListDao().getAllCategoryList())
            }
            APIConstants.EDIT_CATEGORY ->
            {
                appExecutors.diskIO.execute{

                    val detail = mDB.categoriesListDao().getCategoryDetails(entityId)

                    appExecutors.mainThread.execute{

                        callback.onDataLoaded(detail)
                    }
                }
            }
            APIConstants.EDIT_EXPENSE ->
            {
                appExecutors.diskIO.execute{

                    val detail = mDB.expenseDao().getExpenseDetails(entityId)

                    appExecutors.mainThread.execute{

                        callback.onDataLoaded(detail)
                    }
                }
            }

        }
    }

    override fun saveEntityDetails(entityConstant: Int, data: Any, callback: LocalDataSource.DataStoredCallback)
    {
        appExecutors.diskIO.execute{

            when(entityConstant)
            {
                APIConstants.ADD_CATEGORY ->
                {
                    mDB.categoriesListDao().insertNewCategory(data as Category)
                    appExecutors.mainThread.execute{
                        callback.onDataSaved()
                    }
                }
                APIConstants.EDIT_EXPENSE ->
                {
                    mDB.expenseDao().insertNewExpense(data as ExpenseDetails)
                    appExecutors.mainThread.execute{
                        callback.onDataSaved()
                    }
                }
            }
        }
    }

    override fun deleteEntityDetails(entityConstant: Int, entityID: String, callback: LocalDataSource.DataStoredCallback?)
    {
        appExecutors.diskIO.execute{

            when(entityConstant)
            {
                APIConstants.EDIT_CATEGORY ->
                {
                    mDB.categoriesListDao().deleteCategory(entityID)

                    if(callback!=null) {
                        appExecutors.mainThread.execute {
                            callback.onDataSaved()
                        }
                    }
                }
                APIConstants.EDIT_EXPENSE ->
                {
                    mDB.expenseDao().deleteExpenseDetails(entityID)

                    if(callback!=null) {
                        appExecutors.mainThread.execute {
                            callback.onDataSaved()
                        }
                    }
                }

            }

        }
    }

    companion object {

        private var INSTANCE: DatabaseAccessor? = null

        @JvmStatic fun getInstance(context : Context) : LocalDataSource
        {
            if (INSTANCE == null)
            {
                INSTANCE = DatabaseAccessor(ExpenseManagerDB.getInstance(context))
            }

            return INSTANCE !!
        }

        @JvmStatic fun clearInstance()
        {
            INSTANCE = null
        }
    }
}

