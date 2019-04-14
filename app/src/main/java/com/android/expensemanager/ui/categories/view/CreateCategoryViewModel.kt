package com.android.expensemanager.ui.categories.view

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.expensemanager.ui.categories.data.CategoriesDataSource
import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.ui.categories.model.Category

class CreateCategoryViewModel : ViewModel()
{
    lateinit var mDS : CategoriesDataSource

    val mCategoryDetailsLD : MutableLiveData<Category> = MutableLiveData()
    val mLoading        : MutableLiveData<Boolean> = MutableLiveData()
    val mSuccessMessage   : MutableLiveData<String> = MutableLiveData()
    val mErrorMessage   : MutableLiveData<String> = MutableLiveData()

    lateinit var mCategory : Category

    var mCategoryId : String = ""


    fun start()
    {
        if(!TextUtils.isEmpty(mCategoryId))
        {
            getCategoryDetails(mCategoryId)
        }
    }

    private fun getCategoryDetails(mCategoryId: String)
    {
        mDS.getCategoryDetails(mCategoryId, cb = object : CategoriesDataSource.CategoryDetailsCB
        {
            override fun onLoaded(mCategoryDetails: Category)
            {
                mCategoryDetailsLD.value = mCategoryDetails
            }

            override fun onError(errorMsg: String)
            {
                mErrorMessage.value = errorMsg
            }

        })
    }


    fun setRepository(repo: CategoriesDataSource) {

        mDS = repo
    }

    fun saveCategory(categoryName: String, categoryType: String)
    {
        mCategory = Category( categoryName, categoryType)

        mDS.storeNewCategory(mCategory, cb = object : CommonCallbacks.StatusCallback
        {
            override fun onSuccess(message: String)
            {
                mSuccessMessage.value = message
            }

            override fun onError(errorMsg: String)
            {
               mErrorMessage.value = errorMsg
            }

        })
    }

    fun deleteCategory()
    {
        mDS.deleteCategory(mCategoryId , object : CommonCallbacks.StatusCallback
        {
            override fun onSuccess(message: String)
            {
                mSuccessMessage.value = message
            }

            override fun onError(errorMsg: String)
            {
                mErrorMessage.value = errorMsg
            }

        })
    }


}

