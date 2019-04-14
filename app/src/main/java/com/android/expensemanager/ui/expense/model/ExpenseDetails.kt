package com.android.expensemanager.ui.expense.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expense_details_table")
class ExpenseDetails
{
    @field:PrimaryKey(autoGenerate = true)
    @field:ColumnInfo(name = "expense_id")
    var expenseId : Int? = null

    @field:ColumnInfo(name = "date_formated")
    var dateFormated : String? = null

    @field:ColumnInfo(name = "category_name")
    var categoryName : String? = null

    @field:ColumnInfo(name = "category_type")
    var categoryType : String? = null

    @field:ColumnInfo(name = "amount")
    var amount : Int? = null

    @field:ColumnInfo(name = "amount_formated")
    var amountFormated : String? = null

    @field:ColumnInfo(name = "month_id")
    var monthId : Int? = 0
}