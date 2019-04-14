package com.android.expensemanager.ui.categories.data

import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.ui.categories.model.Category

interface CategoriesDataSource
{
    fun getCategoriesList(cb : CategoriesListCB)

    fun storeCategoriesList(categoryList: ArrayList<Category>, cb: CommonCallbacks.StatusCallback)

    fun storeNewCategory(newCategory: Category, cb: CommonCallbacks.StatusCallback)

    fun deleteCategory(mCategoryId : String, cb: CommonCallbacks.StatusCallback)

    fun getCategoryDetails(mCategoryId : String, cb: CategoryDetailsCB)


    interface CategoriesListCB : CommonCallbacks.ErrorCallback
    {
        fun onLoaded(list : List<Category>)
    }

    interface CategoryDetailsCB : CommonCallbacks.ErrorCallback
    {
        fun onLoaded(mCategoryDetails : Category)
    }
}
