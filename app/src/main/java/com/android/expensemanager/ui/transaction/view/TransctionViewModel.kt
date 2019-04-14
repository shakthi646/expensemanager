package com.android.expensemanager.ui.transaction.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.expensemanager.ui.expense.data.ExpenseDataSource
import com.android.expensemanager.ui.expense.model.ExpenseDetails

class TransctionViewModel : ViewModel()
{
    lateinit var mDS : ExpenseDataSource

    var mTransactionListLD : MutableLiveData<List<ExpenseDetails>> = MutableLiveData()


    fun setRepository(repo: ExpenseDataSource)
    {
        mDS = repo
    }

    fun getAllTransactionsList()
    {
        mDS.getTransactionsList(object : ExpenseDataSource.TransactionsListCB
        {
            override fun onLoaded(transactionList: List<ExpenseDetails>)
            {
                mTransactionListLD.value = transactionList
            }

            override fun onError(errorMsg: String)
            {
            }

        })
    }

}
