package com.android.expensemanager.views

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.android.expensemanager.utils.ViewUtils


class RubikMediumTextView(context: Context, attrs: AttributeSet) : AppCompatTextView(context, attrs)
{
    override fun setTypeface(tf: Typeface?)
    {
        super.setTypeface(ViewUtils.getRubikMediumTypeface(context))
    }
}