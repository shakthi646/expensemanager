package com.android.expensemanager.base

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.android.expensemanager.MainNavigationActivity

abstract class BaseFragment : Fragment()
{
    protected lateinit var mActionBar : ActionBar
    protected var mView : View?      = null
    protected var mIsNewInflatedView = true


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return super.onCreateView(inflater, container, savedInstanceState)

    }

    protected fun <T : ViewModel> createViewModel(modelClass: Class<T>) : T
    {
        return ViewModelProvider.AndroidViewModelFactory.getInstance(activity!!.application).create(modelClass)
    }


    protected fun setUpActionBar(toolbar : Toolbar, title : Int ?= null, titleString : String ?= null)
    {
        (activity as MainNavigationActivity).setSupportActionBar(toolbar)


        mActionBar = (activity as MainNavigationActivity).supportActionBar!!

        mActionBar.setDisplayHomeAsUpEnabled(true)
        toolbar.contentInsetStartWithNavigation = 1

        if(title != null)
        {
            mActionBar.title = resources.getString(title)
        }
        else if (!TextUtils.isEmpty(titleString))
        {
            mActionBar.title = titleString
        }
        else
        {
            mActionBar.setDisplayShowTitleEnabled(false)
        }

        activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        setHasOptionsMenu(true)
    }

}