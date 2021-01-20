package com.lizl.wtmg.util

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.SizeUtils
import com.lizl.wtmg.custom.view.withdes.TextViewWithDes

object DataBindUtil
{
    @JvmStatic
    @BindingAdapter("app:adapter")
    fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?)
    {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("app:image")
    fun bindAdapter(imageView: ImageView, imageRes: Int?)
    {
        imageRes ?: return
        imageView.setImageResource(imageRes)
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

    @JvmStatic
    @BindingAdapter("app:overScrollMode")
    fun bindOverScrollModel(viewPager: ViewPager2, mode: Int)
    {
        val child = viewPager.getChildAt(0)
        if (child is RecyclerView)
        {
            child.overScrollMode = mode
        }
    }

    @JvmStatic
    @BindingAdapter("app:isSelected")
    fun bindSelected(view: View, isSelected: Boolean)
    {
        view.isSelected = isSelected
    }

    @JvmStatic
    @BindingAdapter("app:text")
    fun bindText(textView: TextViewWithDes, text: Any?)
    {
        textView.setMainText(text?.toString() ?: "")
    }
}