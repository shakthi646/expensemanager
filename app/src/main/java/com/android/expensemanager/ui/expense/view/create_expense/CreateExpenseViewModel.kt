package com.android.expensemanager.ui.expense.view.create_expense

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.ui.categories.model.Category
import com.android.expensemanager.ui.expense.data.ExpenseDataSource
import com.android.expensemanager.ui.expense.model.ExpenseDetails
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class CreateExpenseViewModel : ViewModel()
{
    lateinit var mDS : ExpenseDataSource

    var mExpenseDetailsEditPage : MutableLiveData<ExpenseDetails> = MutableLiveData()
    val mSuccessMessage   : MutableLiveData<String> = MutableLiveData()
    val mErrorMessage   : MutableLiveData<String> = MutableLiveData()

    lateinit var mExpenseCategory : Category

    lateinit var mExpenseDetails : ExpenseDetails


    var day   : Int = 0
    var month : Int = 0
    var year  : Int = 0

    var mExpenseId : String = ""
    var mIsAdd = true
    var mIsExpense = true

    init
    {

    }

    fun setRepository(repo: ExpenseDataSource)
    {
        mDS = repo
    }

    fun start()
    {
        initilize()

        getExpenseEditPage()
    }

    private fun getExpenseEditPage()
    {

        mDS.getExpenseEditPage(mExpenseId , object : ExpenseDataSource.ExpenseEditPageCB
        {
            override fun onLoaded(expenseEditPageCB: ExpenseDetails?)
            {
                if(expenseEditPageCB != null)
                {
                    mExpenseDetails = expenseEditPageCB
                }
                mExpenseDetailsEditPage.value = expenseEditPageCB
            }

            override fun onError(errorMsg: String) {
                mErrorMessage.value = errorMsg
            }

        })
    }

    private fun initilize()
    {
        if(TextUtils.isEmpty(mExpenseId))
        {
            val calendar = Calendar.getInstance()
            day = calendar.get(Calendar.DAY_OF_MONTH)
            month = calendar.get(Calendar.MONTH)
            year = calendar.get(Calendar.YEAR)
        }
        else
        {
            mIsAdd = false
        }
    }

    fun getCustomizedDate(dateFormat: String, year: Int, month: Int, day: Int): String
    {
        val sdf = SimpleDateFormat(dateFormat)
        val gc = GregorianCalendar.getInstance() as GregorianCalendar
        gc.set(year, month, day)

        return sdf.format(gc.time)
    }

    fun saveExpenseDetails(amount: String)
    {
        mExpenseDetails.amount = amount.toInt()
        val amount_formatted = getIndianRupeesFromatted(mExpenseDetails.amount)
        mExpenseDetails.amountFormated = amount_formatted


        mDS.saveExpenseDetails(mExpenseId, mExpenseDetails, object : CommonCallbacks.StatusCallback
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

    fun getIndianRupeesFromatted(amount : Int?) : String
    {
        val locale=Locale("en", "IN")
        val decimalFormat= DecimalFormat.getCurrencyInstance(locale) as DecimalFormat
        val dfs= DecimalFormatSymbols.getInstance(locale)
        dfs.setCurrencySymbol("\u20B9")
        decimalFormat.setDecimalFormatSymbols(dfs)

        return decimalFormat.format(amount)

    }

    fun deleteSelectedTransaction()
    {
        mDS.deleteTransaction(mExpenseId , object : CommonCallbacks.StatusCallback
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
