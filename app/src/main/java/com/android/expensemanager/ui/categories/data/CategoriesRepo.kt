package com.android.expensemanager.ui.categories.data

import android.util.Log
import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.constants.APIConstants
import com.android.expensemanager.datasource.database.K
import com.android.expensemanager.datasource.database.LocalDataSource
import com.android.expensemanager.ui.categories.model.Category

class CategoriesRepo(private val mLocalDS  : LocalDataSource) : CategoriesDataSource
{
    private lateinit var mGetCategoriesListCB  : CategoriesDataSource.CategoriesListCB
    private lateinit var mStoreCategoriesListCB  : CommonCallbacks.StatusCallback
    private lateinit var mSaveCB            : CommonCallbacks.StatusCallback
    private lateinit var mDeleteCB           : CommonCallbacks.StatusCallback
    private lateinit var mCategoryDetailsCB  : CategoriesDataSource.CategoryDetailsCB


    override fun getCategoriesList(cb: CategoriesDataSource.CategoriesListCB)
    {
        mGetCategoriesListCB = cb

        mLocalDS.getEntityList(APIConstants.CATEGORIES_LIST,object : LocalDataSource.DataLoadedCallback
        {
            override fun onDataLoaded(data: K)
            {
                mGetCategoriesListCB.onLoaded(data as ArrayList<Category>)
            }

            override fun onLoadingError(errorCode: Int, errorMsg: String)
            {
                mGetCategoriesListCB.onError("Problem in Fetching Company Details. Please Contact Support")
            }

        })
    }

    override fun storeCategoriesList(categoryList: ArrayList<Category>, cb: CommonCallbacks.StatusCallback)
    {
        mStoreCategoriesListCB = cb

        mLocalDS.saveEntityList(APIConstants.CATEGORIES_LIST, categoryList , object : LocalDataSource.DataStoredCallback
        {
            override fun onDataSaved()
            {
                Log.d("Category", "Category List Stored")
            }
            override fun onSaveError()
            {

            }
        })
    }

    override fun storeNewCategory(newCategory: Category, cb: CommonCallbacks.StatusCallback)
    {
        mSaveCB = cb

        mLocalDS.saveEntityDetails(APIConstants.ADD_CATEGORY, newCategory , object : LocalDataSource.DataStoredCallback
        {
            override fun onDataSaved()
            {
                mSaveCB.onSuccess("New category was added successfully.")
            }

            override fun onSaveError()
            {

            }


        })
    }

    override fun deleteCategory(mCategoryId: String, cb: CommonCallbacks.StatusCallback)
    {
        mDeleteCB = cb

        mLocalDS.deleteEntityDetails(APIConstants.EDIT_CATEGORY, mCategoryId, object :LocalDataSource.DataStoredCallback
        {
            override fun onDataSaved()
            {
                mDeleteCB.onSuccess("Category was deleted successfully")
            }

            override fun onSaveError()
            {

            }
        })



    }

    override fun getCategoryDetails(mCategoryId: String, cb: CategoriesDataSource.CategoryDetailsCB)
    {
        mCategoryDetailsCB = cb

        mLocalDS.getEntityDetails(APIConstants.EDIT_CATEGORY, mCategoryId , object : LocalDataSource.DataLoadedCallback
        {
            override fun onDataLoaded(data: K)
            {
                mCategoryDetailsCB.onLoaded(data as Category)
            }

            override fun onLoadingError(errorCode: Int, errorMsg: String)
            {
                mCategoryDetailsCB.onError("error")
            }

        })


    }



    companion object {

        private var INSTANCE: CategoriesDataSource? = null

        @JvmStatic fun getInstance(localDataSource: LocalDataSource) = INSTANCE ?: synchronized(CategoriesDataSource::class.java)
            {
                if (INSTANCE == null)
                {
                    INSTANCE = CategoriesRepo(localDataSource)
                }

                return INSTANCE!!
            }


        /**
         * Used to force [getInstance] to create a new instance
         * next time it's called.
         */
        @JvmStatic fun destroyInstance() {
            INSTANCE = null
        }
    }
}


