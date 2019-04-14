package com.android.expensemanager.ui.categories.view

import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.view.*
import android.widget.Toast
import androidx.lifecycle.Observer
import com.android.expensemanager.R
import com.android.expensemanager.StringConstants
import com.android.expensemanager.ui.categories.data.CategoriesDataSource
import com.android.expensemanager.base.BaseFragment
import com.android.expensemanager.ui.categories.data.CategoriesRepo
import com.android.expensemanager.databinding.CreateCategoryFragmentBinding
import com.android.expensemanager.datasource.database.DatabaseAccessor
import com.android.expensemanager.datasource.database.ExpenseManagerDB
import kotlinx.android.synthetic.main.create_category_fragment.*

class CreateCategoryFragment : BaseFragment()
{
    private lateinit var mBinding       : CreateCategoryFragmentBinding
    private lateinit var vm             : CreateCategoryViewModel
    private lateinit var mPrefAccessor  : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        vm = createViewModel(CreateCategoryViewModel::class.java)
        vm.setRepository(getRepo())

        setHasOptionsMenu(true)
    }

    private fun getRepo() : CategoriesDataSource
    {
        return CategoriesRepo.getInstance(DatabaseAccessor(ExpenseManagerDB.getInstance(context!!.applicationContext)))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        if(mView == null || savedInstanceState != null)
        {
            mView = inflater.inflate(R.layout.create_category_fragment, container, false)

            mBinding       = CreateCategoryFragmentBinding.bind(mView!!)
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
            mBinding.model!!.mCategoryId = arguments!![StringConstants.categoryId]!!.toString()

            setUpActionBar(toolbar, R.string.em_create_category)

            mBinding.model!!.mCategoryDetailsLD.observe(this, Observer {

                expense_button.isChecked = it.categoryType.equals(StringConstants.expense)
                income_button.isChecked = it.categoryType.equals(StringConstants.income)
            })

            mBinding.model!!.mSuccessMessage.observe(this, Observer {

                if(!TextUtils.isEmpty(it))
                {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                    activity!!.onBackPressed()
                }
            })

            save_button.setOnClickListener(onSaveClickListener)

            if(savedInstanceState == null)
            {
                mBinding.model!!.start()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?)
    {
        super.onCreateOptionsMenu(menu, inflater)
        menu?.clear()

        if (!TextUtils.isEmpty(mBinding.model!!.mCategoryId))
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
                mBinding.model!!.deleteCategory()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private val onSaveClickListener = View.OnClickListener {

        var categoryType = ""

        if(expense_button.isChecked)
        {
            categoryType = StringConstants.expense
        }
        else
        {
            categoryType = StringConstants.income
        }

        if(!TextUtils.isEmpty(category_name_value.text.trim()))
        {
            vm.saveCategory(category_name_value.text.trim().toString(), categoryType)
        }
        else
        {
            Toast.makeText(context, "Please enter the category name", Toast.LENGTH_SHORT).show()
            category_name_value.error = "Please enter the category name"
        }

    }

    companion object
    {
        fun newInstance(categoryId : String?): CreateCategoryFragment
        {
            val bundle = Bundle().apply { putString(StringConstants.categoryId, categoryId) }

            return newInstance(bundle)
        }

        private fun newInstance(arguments: Bundle?): CreateCategoryFragment
        {
            val fragment = CreateCategoryFragment()

            if (arguments != null)
            {
                fragment.arguments = arguments
            }

            return fragment
        }
    }
}
