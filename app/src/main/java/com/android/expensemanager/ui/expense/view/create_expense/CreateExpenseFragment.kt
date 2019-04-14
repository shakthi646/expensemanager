package com.android.expensemanager.ui.expense.view.create_expense

import android.app.DatePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.android.expensemanager.MainNavigationActivity
import com.android.expensemanager.R
import com.android.expensemanager.StringConstants
import com.android.expensemanager.databinding.CreateExpenseFragmentBinding
import com.android.expensemanager.base.BaseFragment
import com.android.expensemanager.datasource.database.DatabaseAccessor
import com.android.expensemanager.datasource.database.ExpenseManagerDB
import com.android.expensemanager.ui.categories.model.Category
import com.android.expensemanager.ui.categories.view.set
import com.android.expensemanager.ui.expense.data.ExpenseDataSource
import com.android.expensemanager.ui.expense.data.ExpenseRepo
import com.android.expensemanager.ui.expense.model.ExpenseDetails
import com.android.expensemanager.utils.PreferenceAccessor
import com.android.expensemanager.utils.ViewUtils
import kotlinx.android.synthetic.main.create_expense_fragment.*

class CreateExpenseFragment : BaseFragment()
{
    private lateinit var mBinding       : CreateExpenseFragmentBinding
    private lateinit var vm             : CreateExpenseViewModel
    private lateinit var mPrefAccessor  : SharedPreferences

    private var currentSelectedCategoryName = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = createViewModel(CreateExpenseViewModel::class.java)
        vm.setRepository(getRepo())
        mPrefAccessor = PreferenceAccessor.getPrefs(context!!.applicationContext, ViewUtils.SERVICE_PREFS)

        setHasOptionsMenu(true)
    }

    private fun getRepo(): ExpenseDataSource
    {
        return ExpenseRepo.getInstance(DatabaseAccessor(ExpenseManagerDB.getInstance(context!!.applicationContext)))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(mView == null || savedInstanceState != null)
        {
            mView = inflater.inflate(R.layout.create_expense_fragment, container, false)

            mBinding       = CreateExpenseFragmentBinding.bind(mView!!)
            mBinding.model = vm
            mBinding.setLifecycleOwner(this)
        }
        else
        {
            mIsNewInflatedView = false
        }
        return mBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(mIsNewInflatedView)
        {
            mBinding.model!!.mIsExpense = arguments!![StringConstants.isExpense]!! as Boolean
            mBinding.model!!.mExpenseId = arguments!![StringConstants.expenseId]!! as String

            if(mBinding.model!!.mIsExpense)
            {
                setUpActionBar(toolbar , R.string.em_create_expense)
            }
            else
            {
                setUpActionBar(toolbar , R.string.em_add_income)
                save_button.text = StringConstants.save_income
            }


            setListeners()
            setUpUpservers()

            if(savedInstanceState == null)
            {
                mBinding.model!!.start()
            }
        }
        else
        {
            currentSelectedCategoryName = mPrefAccessor.getString(StringConstants.selectedCategoryName,"")
            mBinding.model!!.mExpenseDetails.categoryName = currentSelectedCategoryName
            mBinding.model!!.mExpenseDetailsEditPage.value =  mBinding.model!!.mExpenseDetails
            currentSelectedCategoryName = ""
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
    {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()

        if (!TextUtils.isEmpty(mBinding.model!!.mExpenseId))
        {
            menu!!.add(Menu.NONE, 1, Menu.NONE, "delete").setIcon(R.drawable.ic_delete_icon).setShowAsAction(
                MenuItem.SHOW_AS_ACTION_IF_ROOM)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when(item!!.itemId)
        {
            1 ->
            {
                mBinding.model!!.deleteSelectedTransaction()
            }
        }

        return super.onOptionsItemSelected(item)
    }


    private fun setListeners()
    {
        date_value.setOnClickListener{

            val mDatePickerDialog = DatePickerDialog(context!!, dateSetListener, mBinding.model!!.year, mBinding.model!!.month, mBinding.model!!.day)
            mDatePickerDialog.show()
        }

        category_name.setOnClickListener(onCategoryClickListener)
        save_button.setOnClickListener(saveClickListener)

    }

    private val onCategoryClickListener = View.OnClickListener{

        (activity as MainNavigationActivity).openCategoriesListFragment(true)
    }

    private val saveClickListener =  View.OnClickListener {

        if(TextUtils.isEmpty(mBinding.model!!.mExpenseDetails.categoryName))
        {
            Toast.makeText(context, "Please select the Category", Toast.LENGTH_SHORT).show()
        }
        else if(TextUtils.isEmpty(amount_value.text.toString()))
        {
            Toast.makeText(context, "Please Enter the Amount", Toast.LENGTH_SHORT).show()
            amount_value.error = "Please Enter the Amount"
        }
        else
        {
            mBinding.model!!.saveExpenseDetails(amount_value.text.toString())
        }
    }

    private fun setUpUpservers()
    {
        mBinding.model!!.mExpenseDetailsEditPage.observe(this, Observer {

            if(it != null)
            {
                updateDisplay(it)
            }
            else
            {
                mBinding.model!!.mExpenseDetails = ExpenseDetails()
                setExpenseDate()
            }
        })

        mBinding.model!!.mSuccessMessage.observe(this, Observer {

            if(!TextUtils.isEmpty(it))
            {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                activity!!.onBackPressed()
            }
        })


    }

    private fun updateDisplay(it: ExpenseDetails?) {


        if(it!!.amount != null)
        {
            amount_value.setText(it.amount.toString())
        }

        date_value.text = it.dateFormated
    }


    private val dateSetListener = DatePickerDialog.OnDateSetListener { _, calYear, monthOfYear, dayOfMonth ->

        mBinding.model!!.day   = dayOfMonth
        mBinding.model!!.month = monthOfYear
        mBinding.model!!.year  = calYear

        setExpenseDate()
    }

    private fun setExpenseDate()
    {
        mBinding.model!!.run {

            // setting formatted date
            val dateFormat                              = "dd, MMM yyyy"
            val formattedDate                           = getCustomizedDate(dateFormat, year, month, day)

            mExpenseDetails.dateFormated = formattedDate
            mExpenseDetails.monthId      = month

            date_value.text                             = formattedDate

            if(mBinding.model!!.mIsExpense)
            {
                mExpenseDetails.categoryType = StringConstants.expense
            }
            else
            {
                mExpenseDetails.categoryType = StringConstants.income
            }
        }
    }

    override fun onResume() {
        super.onResume()
    }


    companion object {

        fun newInstance(isExpense : Boolean, expenseId : String?) : CreateExpenseFragment
        {
            val bundle = Bundle().apply {
                putString(StringConstants.expenseId, expenseId)
                putBoolean(StringConstants.isExpense, isExpense)
            }

            return newInstance(bundle)
        }

        private fun newInstance(arguments: Bundle?): CreateExpenseFragment
        {
            val fragment = CreateExpenseFragment()

            if (arguments != null)
            {
                fragment.arguments = arguments
            }

            return fragment
        }

    }
}
