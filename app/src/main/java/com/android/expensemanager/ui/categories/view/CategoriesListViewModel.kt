package com.android.expensemanager.ui.categories.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.expensemanager.ui.categories.data.CategoriesDataSource
import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.ui.categories.model.Category

class CategoriesListViewModel : ViewModel()
{
    lateinit var mDS : CategoriesDataSource

    val mCategoryListLD : MutableLiveData<ArrayList<Category>> = MutableLiveData()
    val mCategoryDetailsLD : MutableLiveData<Category> = MutableLiveData()
    val mLoading        : MutableLiveData<Boolean> = MutableLiveData()
    val mErrorMessage   : MutableLiveData<String> = MutableLiveData()

    val categoryList : ArrayList<Category> = ArrayList()

    var mIsFromCreation : Boolean = false

    lateinit var stringCategoryArray : Array<String>

    fun setRepository(repo: CategoriesDataSource) {

        mDS = repo
    }

    fun start()
    {
        getCategoriesList()
    }

    private fun getCategoriesList()
    {
        mDS.getCategoriesList(object : CategoriesDataSource.CategoriesListCB
        {
            override fun onLoaded(list : List<Category>)
            {
                mLoading.value = false

                mCategoryListLD.value = list as ArrayList<Category>
            }

            override fun onError(errorMsg: String)
            {
                mLoading.value = false
                mErrorMessage.value = errorMsg
            }
        })
    }

    fun getInitialCategoryList(stringCategoryArray: Array<String>)
    {
        this.stringCategoryArray = stringCategoryArray

        for(name in stringCategoryArray)
        {
            categoryList.add(Category( name, "Expense" ))
        }
        categoryList.add(Category("Salary", "Income"))

        storeCategoriesList(categoryList)

        mCategoryListLD.value = categoryList
    }

    private fun storeCategoriesList(categoryList: ArrayList<Category>)
    {
        mDS.storeCategoriesList(categoryList ,  object : CommonCallbacks.StatusCallback
        {
            override fun onSuccess(message: String)
            {

            }

            override fun onError(errorMsg: String)
            {

            }

        })
    }

    fun getSelectedCategory(categoryName: String)
    {
        mDS.getCategoryDetails(categoryName, cb = object : CategoriesDataSource.CategoryDetailsCB
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

}
