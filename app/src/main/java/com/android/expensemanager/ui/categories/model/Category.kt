package com.android.expensemanager.ui.categories.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_list_table")
data class Category(

    @field:PrimaryKey
    @field:ColumnInfo(name = "category_name")
    val categoryName: String,

    @field:ColumnInfo(name = "category_type")
    val categoryType: String
)