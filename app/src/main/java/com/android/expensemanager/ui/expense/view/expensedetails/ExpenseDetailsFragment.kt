package com.android.expensemanager.ui.expense.view.expensedetails

import android.os.Bundle
import com.android.expensemanager.StringConstants
import com.android.expensemanager.base.BaseFragment

class ExpenseDetailsFragment : BaseFragment()
{


    companion object {

        fun newInstance(expenseId : String?) : ExpenseDetailsFragment
        {
            val bundle = Bundle().apply { putString(StringConstants.expenseId, expenseId) }

            return newInstance(bundle)
        }

        private fun newInstance(arguments: Bundle?): ExpenseDetailsFragment
        {
            val fragment = ExpenseDetailsFragment()

            if (arguments != null)
            {
                fragment.arguments = arguments
            }

            return fragment
        }
    }
}
