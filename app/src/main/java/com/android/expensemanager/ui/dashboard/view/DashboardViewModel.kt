package com.android.expensemanager.ui.dashboard.view
import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.expensemanager.StringConstants
import com.android.expensemanager.base.CommonCallbacks
import com.android.expensemanager.ui.categories.data.CategoriesDataSource
import com.android.expensemanager.ui.expense.data.ExpenseDataSource
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.text.SimpleDateFormat
import java.util.*

class DashboardViewModel : ViewModel()
{

    lateinit var mDS : ExpenseDataSource

    val mDateLD  : MutableLiveData<String> = MutableLiveData()
    val currentMonthTotalIncomeLD  : MutableLiveData<Double> = MutableLiveData()
    val currentMonthTotalExpenseLD  : MutableLiveData<Double> = MutableLiveData()

    var currentMonthTotalIncome : Double  = 0.0
    var currentMonthTotalExpense : Double  = 0.0
    var mCurrentDate  = ""
    var month = 0

    init
    {
        val calendar = Calendar.getInstance()

        val hour    = calendar.get(Calendar.HOUR_OF_DAY)
        val day     = calendar.get(Calendar.DAY_OF_MONTH)
        month   = calendar.get(Calendar.MONTH)
        val year    = calendar.get(Calendar.YEAR)
        val date    = calendar.get(Calendar.DATE)

//        mDateLD.value = getCustomizedDate("",year,month,day)
        mCurrentDate = getCustomizedDate("",year,month,day)
        mDateLD.value = getCustomizedDate("EEE dd, MMM yyyy",year,month,day)
    }

    fun setRepository(dataSource: ExpenseDataSource)
    {
        mDS = dataSource
    }

    fun getCustomizedDate(format: String, year: Int, month: Int, day: Int): String
    {
        val sdf = SimpleDateFormat(if (TextUtils.isEmpty(format)) "MMMM yyyy" else format)
        val gc = GregorianCalendar.getInstance() as GregorianCalendar
        gc.set(year, month, day)

        return sdf.format(gc.time)
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

    fun start()
    {
        getCurrentMonthTotalExpense()
        getCurrentMonthTotalIncome()
    }

    private fun getCurrentMonthTotalIncome()
    {
        mDS.getCurrentMonthTotalTransaction(month , StringConstants.income , object : CommonCallbacks.StatusCallback
        {
            override fun onSuccess(message: String)
            {
                currentMonthTotalIncomeLD.value = message.toDouble()
            }

            override fun onError(errorMsg: String)
            {
            }

        })
    }

    private fun getCurrentMonthTotalExpense()
    {
        mDS.getCurrentMonthTotalTransaction(month , StringConstants.expense , object : CommonCallbacks.StatusCallback
        {
            override fun onSuccess(message: String)
            {
                currentMonthTotalExpenseLD.value = message.toDouble()
            }

            override fun onError(errorMsg: String) {
            }

        })
    }
}
