package com.android.expensemanager.ui.expense.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.expensemanager.ui.expense.model.ExpenseDetails

@Dao
interface ExpenseDao
{
    @Query("SELECT * FROM expense_details_table")
    fun getAllTransactionsList() : List<ExpenseDetails>

    @Query("SELECT * FROM expense_details_table WHERE expense_id = :expenseIdString")
    fun getExpenseDetails(expenseIdString : String) : ExpenseDetails

    @Query("DELETE FROM expense_details_table WHERE category_name = :expenseIdString")
    fun deleteExpenseDetails(expenseIdString: String): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNewExpense(mExpenseDetails : ExpenseDetails)

    @Query("SELECT SUM(amount) FROM expense_details_table WHERE month_id = :monthId AND category_type = :transactionType")
    fun getCurrentMonthTransactionAmount(monthId : Int, transactionType : String) : Int
}
