package com.android.expensemanager.datasource.database

import androidx.lifecycle.LiveData
import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.ui.categories.model.Category
import org.jetbrains.annotations.NotNull

typealias K = Any?

interface LocalDataSource
{
    interface DataLoadedCallback
    {
        fun onDataLoaded(data: K)
        fun onLoadingError(errorCode : Int, errorMsg : String)
    }

    interface DataStoredCallback
    {
        fun onDataSaved()
        fun onSaveError()
    }


    fun getEntityList(@NotNull entityConstant : Int, callback : DataLoadedCallback)
    fun getEntityDetails(@NotNull entityConstant : Int, entityId : String, callback : DataLoadedCallback)

    fun saveEntityDetails(@NotNull entityConstant: Int, data: Any, callback: DataStoredCallback)
    fun saveEntityList(@NotNull entityConstant: Int, data: K, callback: DataStoredCallback)

    fun deleteEntityDetails(@NotNull entityConstant: Int, entityID: String , callback: DataStoredCallback ?= null)


    fun getEntityAmountDetails(entityId : Int, transactionType : String, callback : DataLoadedCallback)

}
