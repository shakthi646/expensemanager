package com.android.expensemanager.ui.dashboard.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.lifecycle.Observer
import com.android.expensemanager.MainNavigationActivity
import com.android.expensemanager.base.BaseFragment
import com.android.expensemanager.R
import com.android.expensemanager.databinding.DashboardFragmentBinding
import com.android.expensemanager.datasource.database.DatabaseAccessor
import com.android.expensemanager.datasource.database.ExpenseManagerDB
import com.android.expensemanager.ui.expense.data.ExpenseDataSource
import com.android.expensemanager.ui.expense.data.ExpenseRepo
import com.android.expensemanager.utils.PreferenceAccessor
import com.android.expensemanager.utils.ViewUtils
import kotlinx.android.synthetic.main.dashboard_fragment.*

class DashboardFragment : BaseFragment(), View.OnClickListener{

    private lateinit var mBinding       : DashboardFragmentBinding
    private lateinit var vm             : DashboardViewModel
    private lateinit var mPrefAccessor  : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = createViewModel(DashboardViewModel::class.java)
        vm.setRepository(getRepo())
        mPrefAccessor = PreferenceAccessor.getPrefs(context!!.applicationContext, ViewUtils.SERVICE_PREFS)

        setHasOptionsMenu(true)
    }

    private fun getRepo() : ExpenseDataSource
    {
        return ExpenseRepo.getInstance(DatabaseAccessor(ExpenseManagerDB.getInstance(context!!.applicationContext)))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(mView == null || savedInstanceState != null)
        {
            mView = inflater.inflate(R.layout.dashboard_fragment, container, false)

            mBinding       = DashboardFragmentBinding.bind(mView!!)
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
//            setUpNavigationDrawer()

            setUpClickListeners()

            setUpUpservers()
        }

        mBinding.model!!.start()

    }

//    private fun setUpNavigationDrawer()
//    {
//        val toggle = ActionBarDrawerToggle(activity, drawer_layout, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
//
//        drawer_layout.addDrawerListener(toggle)
//        toggle.syncState()
//
//        nav_view.setNavigationItemSelectedListener(this)
//    }

    private fun setUpUpservers()
    {
       mBinding.model!!.mDateLD.observe(this, Observer {

//           dashboard_date_field.text = it
       })

       mBinding.model!!.currentMonthTotalIncomeLD.observe(this, Observer {

           mBinding.model!!.currentMonthTotalIncome = it
           initilizeGraph()
       })

        mBinding.model!!.currentMonthTotalExpenseLD.observe(this, Observer {
            mBinding.model!!.currentMonthTotalExpense = it
            initilizeGraph()
        })
    }

    private fun initilizeGraph()
    {
        var totalBalance = mBinding.model!!.currentMonthTotalIncome - mBinding.model!!.currentMonthTotalExpense

        total_balance_value.text = vm.getIndianRupeesFromatted(totalBalance.toInt())
        income_value.text        = vm.getIndianRupeesFromatted(mBinding.model!!.currentMonthTotalIncome.toInt())
        expense_value.text       = vm.getIndianRupeesFromatted(mBinding.model!!.currentMonthTotalExpense.toInt())
        month_year_value.text    = vm.mCurrentDate


        val totalWidth : Int = indicator_bar_expense.width
        var totalAmount = mBinding.model!!.currentMonthTotalIncome + mBinding.model!!.currentMonthTotalExpense

        if(mBinding.model!!.currentMonthTotalIncome > 0)
        {
            indicator_bar_income.visibility = View.VISIBLE

            var tempLength : Double = totalWidth.div(totalAmount) as Double

            val percentage = mBinding.model!!.currentMonthTotalIncome.div(totalAmount)
            val resultWidth = tempLength.times(mBinding.model!!.currentMonthTotalIncome)

            indicator_bar_income.layoutParams.width = resultWidth.toInt()

            indicator_bar_income.requestLayout()
        }
        else
        {
            indicator_bar_income.visibility = View.GONE
        }
    }

    private fun setUpClickListeners()
    {
        add_income_button.setOnClickListener(this)
        add_expense_button.setOnClickListener(this)
        transactions_button.setOnClickListener(this)
        categories_button.setOnClickListener(this)
    }

    override fun onClick(view: View?)
    {

        when(view)
        {
            add_income_button      -> (activity as MainNavigationActivity).createExpenseFragment(false)
            add_expense_button     -> (activity as MainNavigationActivity).createExpenseFragment(true)
            transactions_button    -> (activity as MainNavigationActivity).openTransactionListFragment()
            categories_button      -> (activity as MainNavigationActivity).openCategoriesListFragment()
        }
    }


    companion object
    {
        fun newInstance(): DashboardFragment
        {
            return newInstance(null)
        }

        private fun newInstance(arguments: Bundle?): DashboardFragment
        {
            val fragment = DashboardFragment()

            if (arguments != null)
            {
                fragment.arguments = arguments
            }

            return fragment
        }
    }

}
