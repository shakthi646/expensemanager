package com.android.expensemanager

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import com.android.expensemanager.ui.dashboard.view.DashboardFragment
import com.android.expensemanager.ui.categories.view.CategoriesListFragment
import com.android.expensemanager.constants.IntConstants
import com.android.expensemanager.ui.categories.view.CreateCategoryFragment
import com.android.expensemanager.ui.expense.view.create_expense.CreateExpenseFragment
import com.android.expensemanager.ui.homeViewPager.HomeViewPagerFragment
import com.android.expensemanager.ui.transaction.view.TransactionListFragment
import kotlinx.android.synthetic.main.dashboard_fragment.*

class MainNavigationActivity : AppCompatActivity() {

    var mCurrentFragment = IntConstants.sDashboard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_navigation_activity)

//        openHomeViewPager()
        openDashboard()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean
    {
        if (item.itemId == android.R.id.home)
        {
            if(mCurrentFragment == IntConstants.sDashboard)
            {
                this.finish()
            }

            super.onBackPressed()
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed()
    {
//        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
//            drawer_layout.closeDrawer(GravityCompat.START)
//        }
//        else
//        {
//            super.onBackPressed()
//        }
        super.onBackPressed()
    }

    fun openHomeViewPager()
    {

        val fragmentPopped = supportFragmentManager.popBackStackImmediate(StringConstants.fragHomeViewPager, 0)

        if (!fragmentPopped && supportFragmentManager.findFragmentByTag(StringConstants.fragHomeViewPager) == null)
        {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.list_container, HomeViewPagerFragment.newInstance(), StringConstants.fragHomeViewPager)
                .addToBackStack(StringConstants.fragHomeViewPager)
                .commit()
        }

        mCurrentFragment = IntConstants.sHome
    }

    fun openDashboard()
    {

        val fragmentPopped = supportFragmentManager.popBackStackImmediate(StringConstants.fragDashboard, 0)

        if (!fragmentPopped && supportFragmentManager.findFragmentByTag(StringConstants.fragDashboard) == null)
        {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.list_container, DashboardFragment.newInstance(), StringConstants.fragDashboard)
                .addToBackStack(StringConstants.fragDashboard)
                .commit()
        }

        mCurrentFragment = IntConstants.sDashboard
    }

    fun createExpenseFragment(isExpense : Boolean , expenseId : String = "")
    {
        val fragmentPopped = supportFragmentManager.popBackStackImmediate(StringConstants.fragCreateExpense, 0)

        if (!fragmentPopped && supportFragmentManager.findFragmentByTag(StringConstants.fragCreateExpense) == null)
        {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations( R.anim.slide_in_right, 0, 0, 0)
                .replace(
                    R.id.list_container, CreateExpenseFragment.newInstance(isExpense, expenseId), StringConstants.fragCreateExpense)
                .addToBackStack(StringConstants.fragCreateExpense)
                .commit()
        }

        mCurrentFragment = IntConstants.sCategoriesList
    }

    fun openTransactionListFragment()
    {
        val fragmentPopped = supportFragmentManager.popBackStackImmediate(StringConstants.fragTransactionList, 0)

        if (!fragmentPopped && supportFragmentManager.findFragmentByTag(StringConstants.fragTransactionList) == null)
        {
            supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.list_container, TransactionListFragment.newInstance(), StringConstants.fragTransactionList)
                .addToBackStack(StringConstants.fragTransactionList)
                .commit()
        }

        mCurrentFragment = IntConstants.sTransactionList
    }

    fun openCategoriesListFragment(isFromCreation : Boolean = false)
    {

        val fragmentPopped = supportFragmentManager.popBackStackImmediate(StringConstants.fragCategoriesList, 0)

        if (!fragmentPopped && supportFragmentManager.findFragmentByTag(StringConstants.fragCategoriesList) == null)
        {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations( R.anim.slide_in_right, 0, 0, 0)
                .replace(
                    R.id.list_container, CategoriesListFragment.newInstance(isFromCreation), StringConstants.fragCategoriesList)
                .addToBackStack(StringConstants.fragCategoriesList)
                .commit()
        }

        mCurrentFragment = IntConstants.sCategoriesList
    }

    fun openCreateCategoryFragment(categoryId : String = "")
    {
        val fragmentPopped = supportFragmentManager.popBackStackImmediate(StringConstants.fragCreateCategory, 0)

        if (!fragmentPopped && supportFragmentManager.findFragmentByTag(StringConstants.fragCreateCategory) == null)
        {
            supportFragmentManager
                .beginTransaction()
                .setCustomAnimations( R.anim.slide_in_right, 0, 0, 0)
                .replace(
                    R.id.list_container, CreateCategoryFragment.newInstance(categoryId), StringConstants.fragCreateCategory)
                .addToBackStack(StringConstants.fragCreateCategory)
                .commit()
        }

        mCurrentFragment = IntConstants.sCreateCategory
    }
}
