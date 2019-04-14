package com.android.expensemanager.ui.expense.data

import com.android.expensemanager.StringConstants
import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.common.AppExecutors
import com.android.expensemanager.constants.APIConstants
import com.android.expensemanager.datasource.database.K
import com.android.expensemanager.datasource.database.LocalDataSource
import com.android.expensemanager.ui.expense.model.ExpenseDetails

class ExpenseRepo (private val mLocalDS  : LocalDataSource) : ExpenseDataSource
{
    private lateinit var mSaveCB            : CommonCallbacks.StatusCallback
    private lateinit var mDeleteCB           : CommonCallbacks.StatusCallback
    private lateinit var mExpenseEditCB           : ExpenseDataSource.ExpenseEditPageCB
    private lateinit var mTransactionListCB           : ExpenseDataSource.TransactionsListCB
    private lateinit var mIncomeTotalCB : CommonCallbacks.StatusCallback
    private lateinit var mExpenseTotalCB : CommonCallbacks.StatusCallback


    override fun getCurrentMonthTotalTransaction(monthId: Int, transactionType: String, cb: CommonCallbacks.StatusCallback)
    {
        if(transactionType.equals(StringConstants.income))
        {
            mIncomeTotalCB = cb
        }
        else
        {
            mExpenseTotalCB = cb
        }

        mLocalDS.getEntityAmountDetails(monthId, transactionType, object : LocalDataSource.DataLoadedCallback
        {
            override fun onDataLoaded(data: K)
            {
                if(transactionType.equals(StringConstants.income))
                {
                    mIncomeTotalCB.onSuccess(data.toString())
                }
                else
                {
                    mExpenseTotalCB.onSuccess(data.toString())
                }
            }

            override fun onLoadingError(errorCode: Int, errorMsg: String)
            {

            }

        })

    }

    override fun deleteTransaction(mExpenseId: String, cb: CommonCallbacks.StatusCallback)
    {
        mDeleteCB = cb

        mLocalDS.deleteEntityDetails(APIConstants.EDIT_EXPENSE, mExpenseId, object :LocalDataSource.DataStoredCallback
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

    override fun getTransactionsList(cb: ExpenseDataSource.TransactionsListCB)
    {
        mTransactionListCB = cb

        mLocalDS.getEntityList(APIConstants.TRANSACTION_LIST,object : LocalDataSource.DataLoadedCallback
        {
            override fun onDataLoaded(data: K)
            {
                mTransactionListCB.onLoaded(data as ArrayList<ExpenseDetails>)
            }

            override fun onLoadingError(errorCode: Int, errorMsg: String)
            {
                mTransactionListCB.onError("Problem in Fetching Company Details. Please Contact Support")
            }

        })

    }

    override fun getExpenseEditPage(expenseId: String, cb: ExpenseDataSource.ExpenseEditPageCB?)
    {
        mExpenseEditCB = cb!!

        mLocalDS.getEntityDetails(APIConstants.EDIT_EXPENSE, expenseId , object : LocalDataSource.DataLoadedCallback
        {
            override fun onDataLoaded(data: K)
            {
                if(data == null)
                {
                    mExpenseEditCB.onLoaded(null)
                }
                else
                {
                    mExpenseEditCB.onLoaded(data as ExpenseDetails)
                }

            }

            override fun onLoadingError(errorCode: Int, errorMsg: String)
            {
                mExpenseEditCB.onError("error")
            }

        })
    }

    override fun saveExpenseDetails(mExpenseId: String, mExpenseDetails: ExpenseDetails, cb: CommonCallbacks.StatusCallback)
    {
        mSaveCB = cb

        mLocalDS.saveEntityDetails(APIConstants.EDIT_EXPENSE , mExpenseDetails, object : LocalDataSource.DataStoredCallback {
            override fun onDataSaved() {
                mSaveCB.onSuccess("data Stored Successfully")
            }

            override fun onSaveError() {
                mSaveCB.onSuccess("data Stored Successfully")
            }
        })

    }

    companion object {

        private var INSTANCE: ExpenseDataSource? = null

        @JvmStatic fun getInstance(localDataSource: LocalDataSource) = INSTANCE ?: synchronized(ExpenseDataSource::class.java)
        {
            if (INSTANCE == null)
            {
                INSTANCE = ExpenseRepo(localDataSource)
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
