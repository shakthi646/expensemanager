package com.android.expensemanager.ui.homeViewPager

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.android.expensemanager.R
import com.android.expensemanager.base.BaseFragment
import com.android.expensemanager.ui.categories.view.CategoriesListFragment
import com.android.expensemanager.ui.dashboard.view.DashboardFragment
import com.android.expensemanager.ui.transaction.view.TransactionListFragment
import com.android.expensemanager.utils.PreferenceAccessor
import com.android.expensemanager.utils.ViewUtils
import kotlinx.android.synthetic.main.home_view_pager_fragment.*

class HomeViewPagerFragment : BaseFragment()
{
    private lateinit var mPrefAccessor  : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mPrefAccessor = PreferenceAccessor.getPrefs(context!!.applicationContext, ViewUtils.SERVICE_PREFS)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(mView == null || savedInstanceState != null)
        {
            mView = inflater.inflate(R.layout.home_view_pager_fragment, container, false)
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
            expense_view_pager.adapter = null
            expense_view_pager.adapter = ExpenseFragmentAdapter(childFragmentManager)
            expense_tab_layout.setupWithViewPager(expense_view_pager)
            expense_view_pager.offscreenPageLimit = 2
        }
        else
        {

        }
    }

    private inner class ExpenseFragmentAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)
    {
        override fun getItem(position: Int): Fragment
        {
            return when (position)
            {
                0 -> DashboardFragment.newInstance()
                1 -> TransactionListFragment.newInstance()
                2 -> CategoriesListFragment.newInstance(false)

                else -> TransactionListFragment.newInstance()
            }
        }

        override fun getCount(): Int
        {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence?
        {
            return when (position)
            {
                0 ->  "Dashbboard"
                1 ->  "Transaction"
                2 ->  "Categories"

                else -> "Dashbboard"
            }
        }
    }

    companion object
    {
        fun newInstance( ): HomeViewPagerFragment
        {
            return HomeViewPagerFragment()
        }
    }
}
