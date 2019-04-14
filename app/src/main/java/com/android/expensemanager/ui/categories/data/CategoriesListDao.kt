package com.android.expensemanager.ui.categories.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.expensemanager.ui.categories.model.Category

@Dao
interface CategoriesListDao
{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewCategory(newCategory : Category)

    @Query("SELECT * FROM category_list_table WHERE category_type = :categoryTpye")
    fun getCategoryList(categoryTpye : String) : Category

    @Query("SELECT * FROM category_list_table WHERE category_name = :mcategoryId")
    fun getCategoryDetails(mcategoryId : String) : Category

    @Query("DELETE FROM category_list_table WHERE category_name = :mcategoryId")
    fun deleteCategory(mcategoryId: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun storeCategoryList(list: List<Category>)

    @Query("SELECT * FROM category_list_table")
    fun getAllCategoryList() : List<Category>
}
