package com.lizl.wtmg.util

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.widget.EditText
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils

object DataBindUtil
{
    @JvmStatic
    @BindingAdapter("app:adapter")
    fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?)
    {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("app:hintTextSize")
    fun bindHintTextSize(editText: EditText, textSize: Float)
    {
        val ss = SpannableString(editText.hint)
        val ass = AbsoluteSizeSpan(SizeUtils.px2dp(textSize), true)
        ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        editText.hint = SpannableString(ss)
    }
}