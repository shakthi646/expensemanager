package com.android.expensemanager.base

interface CommonCallbacks
{

    interface StatusCallback : ErrorCallback
    {
        fun onSuccess(message : String)
    }

    interface ErrorCallback
    {
        fun onError(errorMsg : String)
    }
}