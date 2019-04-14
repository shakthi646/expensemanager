package com.android.expensemanager.ui.expense.data

import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.base.ErrorCallback
import com.android.expensemanager.ui.expense.model.ExpenseDetails

interface ExpenseDataSource
{
    fun getTransactionsList(cb : TransactionsListCB)
    fun getExpenseEditPage(expenseId : String = "",cb : ExpenseEditPageCB?)
    fun saveExpenseDetails(mExpenseId: String, mExpenseDetails: ExpenseDetails, statusCallback: CommonCallbacks.StatusCallback)
    fun deleteTransaction(mExpenseId : String, cb: CommonCallbacks.StatusCallback)

    fun getCurrentMonthTotalTransaction(monthId : Int, transactionType : String, cb: CommonCallbacks.StatusCallback)



    interface TransactionsListCB : CommonCallbacks.ErrorCallback
    {
        fun onLoaded(transactionList : List<ExpenseDetails>)
    }

    interface ExpenseEditPageCB : ErrorCallback
    {
        fun onLoaded(expenseEditPageCB: ExpenseDetails?)
    }
}

