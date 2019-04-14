package com.android.expensemanager.ui.transaction.view

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.android.expensemanager.MainNavigationActivity
import com.android.expensemanager.R
import com.android.expensemanager.StringConstants
import com.android.expensemanager.base.BaseFragment
import com.android.expensemanager.datasource.database.DatabaseAccessor
import com.android.expensemanager.datasource.database.ExpenseManagerDB
import com.android.expensemanager.ui.expense.data.ExpenseDataSource
import com.android.expensemanager.ui.expense.data.ExpenseRepo
import com.android.expensemanager.ui.expense.model.ExpenseDetails
import com.android.expensemanager.utils.PreferenceAccessor
import com.android.expensemanager.utils.ViewUtils
import kotlinx.android.synthetic.main.expense_list_item.view.*
import kotlinx.android.synthetic.main.transaction_list_fragment.*
import java.text.DecimalFormat


class TransactionListFragment : BaseFragment()
{
    private lateinit var vm             : TransctionViewModel
    private lateinit var mPrefAccessor  : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = createViewModel(TransctionViewModel::class.java)
        vm.setRepository(getRepo())
        mPrefAccessor = PreferenceAccessor.getPrefs(context!!.applicationContext, ViewUtils.SERVICE_PREFS)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(mView == null || savedInstanceState != null)
        {
            mView = inflater.inflate(R.layout.transaction_list_fragment, container, false)
        }
        else
        {
            mIsNewInflatedView = false
        }
        return mView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(mIsNewInflatedView)
        {
            setUpActionBar(toolbar, R.string.em_transaction_list)

            setUpObservers()

            if(savedInstanceState == null)
            {
                vm.getAllTransactionsList()
            }
        }
        else
        {
            vm.getAllTransactionsList()
        }
    }

    private fun setUpObservers()
    {
        vm.mTransactionListLD.observe(this, Observer {

            inflateTransactionList(it)
        })
    }

    private fun inflateTransactionList(transactionList: List<ExpenseDetails>?)
    {
        transaction_container.removeAllViews()

        for(transaction in transactionList!!)
        {
            transaction_container.addView(constructTransactionListItem(transaction))
        }

    }

    private fun constructTransactionListItem(transaction: ExpenseDetails): View?
    {
        val formatter = DecimalFormat("0000")

        val view = layoutInflater.inflate(R.layout.expense_list_item, null)



        if(transaction.categoryType.equals(StringConstants.expense))
        {
            view.exp_number.text = "Exp - "+formatter.format(transaction.expenseId)
        }
        else
        {
            view.exp_number.text = "Inc - "+formatter.format(transaction.expenseId)
            view.category_type.setBackgroundResource(R.drawable.rounded_corners_green)
        }
        view.category_type.text = transaction.categoryType
        view.category_name.text = transaction.categoryName
        view.exp_date.text = transaction.dateFormated
        view.expense_amount.text = transaction.amountFormated

        view.tag = transaction.expenseId

        view.setOnClickListener(onTransactionListItemClickListener)

        return view
    }

    private val onTransactionListItemClickListener = View.OnClickListener {

        var text = it.category_type.text
        val isExpense = text.contains(StringConstants.expense)

        (activity as MainNavigationActivity).createExpenseFragment(isExpense, it.tag.toString())
    }

    private fun getRepo() : ExpenseDataSource
    {
        return ExpenseRepo.getInstance(DatabaseAccessor(ExpenseManagerDB.getInstance(context!!.applicationContext)))
    }


    companion object
    {
        fun newInstance(): TransactionListFragment
        {
            return TransactionListFragment()
        }
    }
}
