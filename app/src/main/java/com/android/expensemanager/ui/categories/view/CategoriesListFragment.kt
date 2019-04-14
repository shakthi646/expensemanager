package com.android.expensemanager.ui.categories.view

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.android.expensemanager.MainNavigationActivity
import com.android.expensemanager.R
import com.android.expensemanager.StringConstants
import com.android.expensemanager.ui.categories.data.CategoriesDataSource
import com.android.expensemanager.base.BaseFragment
import com.android.expensemanager.ui.categories.data.CategoriesRepo
import com.android.expensemanager.databinding.CategoriesListFragmentBinding
import com.android.expensemanager.datasource.database.DatabaseAccessor
import com.android.expensemanager.datasource.database.ExpenseManagerDB
import com.android.expensemanager.ui.categories.model.Category
import com.android.expensemanager.ui.expense.view.create_expense.CreateExpenseFragment
import com.android.expensemanager.utils.PreferenceAccessor
import com.android.expensemanager.utils.PreferenceAccessor.edit
import com.android.expensemanager.utils.ViewUtils
import kotlinx.android.synthetic.main.categories_list_fragment.*
import kotlinx.android.synthetic.main.categories_list_item.view.*

class CategoriesListFragment : BaseFragment()
{

    private lateinit var mBinding       : CategoriesListFragmentBinding
    private lateinit var vm             : CategoriesListViewModel
    private lateinit var mPrefAccessor  : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = createViewModel(CategoriesListViewModel::class.java)
        vm.setRepository(getRepo())
        mPrefAccessor = PreferenceAccessor.getPrefs(context!!.applicationContext, ViewUtils.SERVICE_PREFS)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(mView == null || savedInstanceState != null)
        {
            mView = inflater.inflate(R.layout.categories_list_fragment, container, false)

            mBinding       = CategoriesListFragmentBinding.bind(mView!!)
            mBinding.model = vm
            mBinding.setLifecycleOwner(this)
        }
        else
        {
            mIsNewInflatedView = false
        }
        return mBinding.root

    }

    @SuppressLint("RestrictedApi")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if(mIsNewInflatedView)
        {
            mBinding.model!!.mIsFromCreation = (arguments!![StringConstants.isFromCreation] as Boolean?)!!

            setUpActionBar(toolbar, R.string.em_categories_list)

            setUpObservers()

            add_category_fab.setOnClickListener(onAddClickListener)

            if(savedInstanceState == null)
            {
                mBinding.model!!.start()
            }

            if(mBinding.model!!.mIsFromCreation)
            {
                add_category_fab.visibility = View.GONE
            }
        }
        else
        {
            mBinding.model!!.start()
        }


    }

    private fun setUpObservers()
    {
        mBinding.model!!.mCategoryListLD.observe(this, Observer {

            if(it.size <= 0)
            {
                mBinding.model!!.getInitialCategoryList(resources.getStringArray(R.array.category_array))
            }
            inflateCategoryItems(it)
        })

        mBinding.model!!.mCategoryDetailsLD.observe(this, Observer {

            mPrefAccessor[StringConstants.selectedCategoryName] = it.categoryName
            mPrefAccessor[StringConstants.selectedCategoryType] = it.categoryType

            activity!!.onBackPressed()
        })
    }

    private fun inflateCategoryItems(categoryList: ArrayList<Category>?)
    {
        categories_container.removeAllViews()

        for(category in categoryList!!)
        {
            categories_container.addView(constructCategoryListItem(category))
        }
    }

    private fun constructCategoryListItem(category: Category) : View
    {
        val view = layoutInflater.inflate(R.layout.categories_list_item, null)

        view.category_name.text = category.categoryName

        view.tag = category.categoryName

        view.setOnClickListener(onCategoryListItemClickListener)

        return view
    }

    private val onCategoryListItemClickListener = View.OnClickListener { listItem ->

        if(mBinding.model!!.mIsFromCreation)
        {
            getSelectedCategory(listItem!!.tag!!.toString())
        }
        else // for Category edit section
        {
            (activity as MainNavigationActivity).openCreateCategoryFragment(listItem!!.tag!!.toString())
        }
    }

    private fun getSelectedCategory(categoryName: String)
    {
        mBinding.model!!.getSelectedCategory(categoryName)
    }

    private fun getRepo() : CategoriesDataSource
    {
        return CategoriesRepo.getInstance(DatabaseAccessor(ExpenseManagerDB.getInstance(context!!.applicationContext))
        )
    }

    private val onAddClickListener = View.OnClickListener {

        (activity as MainNavigationActivity).openCreateCategoryFragment()
    }


    companion object
    {
        fun newInstance(isFromCreation : Boolean): CategoriesListFragment
        {
            val bundle = Bundle().apply { putBoolean(StringConstants.isFromCreation, isFromCreation) }
            return newInstance(bundle)
        }

        private fun newInstance(arguments: Bundle?): CategoriesListFragment
        {
            val fragment = CategoriesListFragment()

            if (arguments != null)
            {
                fragment.arguments = arguments
            }

            return fragment
        }
    }

}

operator fun SharedPreferences.set(key: String, value: Any?)
{
    when (value)
    {
        is String? -> edit({ it.putString(key, value) })
        is Int -> edit({ it.putInt(key, value) })
        is Boolean -> edit({ it.putBoolean(key, value) })
        is Float -> edit({ it.putFloat(key, value) })
        is Long -> edit({ it.putLong(key, value) })
        else -> throw UnsupportedOperationException("Not yet implemented")
    }
}
