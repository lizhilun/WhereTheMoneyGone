package com.lizl.wtmg.util

import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.SizeUtils
import com.lizl.wtmg.custom.function.setHintTextSize
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.view.QuantityStatisticsView
import com.lizl.wtmg.custom.view.withdes.TextViewWithDes
import com.lizl.wtmg.mvvm.model.statistics.QuantityModel

object DataBindUtil {
    @JvmStatic
    @BindingAdapter("app:adapter")
    fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?) {
        recyclerView.adapter = adapter
    }

    @JvmStatic
    @BindingAdapter("app:image")
    fun bingImage(imageView: ImageView, imageRes: Int?) {
        imageRes ?: return
        imageView.setImageResource(imageRes)
    }

    @JvmStatic
    @BindingAdapter("app:hintTextSize")
    fun bindHintTextSize(editText: EditText, textSize: Float) {
        val ss = SpannableString(editText.hint)
        val ass = AbsoluteSizeSpan(SizeUtils.px2dp(textSize), true)
        ss.setSpan(ass, 0, ss.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        editText.hint = SpannableString(ss)
    }

    @JvmStatic
    @BindingAdapter("app:overScrollMode")
    fun bindOverScrollModel(viewPager: ViewPager2, mode: Int) {
        val child = viewPager.getChildAt(0)
        if (child is RecyclerView) {
            child.overScrollMode = mode
        }
    }

    @JvmStatic
    @BindingAdapter("app:isSelected")
    fun bindSelected(view: View, isSelected: Boolean) {
        view.isSelected = isSelected
    }

    @JvmStatic
    @BindingAdapter("app:text")
    fun bindText(textView: TextViewWithDes, text: Any?) {
        textView.setMainText(text?.toString() ?: "")
    }

    @JvmStatic
    @BindingAdapter("app:amount")
    fun bindAmount(textView: TextView, amount: Double) {
        textView.text = amount.toAmountStr()
    }

    @JvmStatic
    @BindingAdapter("app:amount")
    fun bindAmount(textView: TextViewWithDes, amount: Double) {
        textView.setMainText(amount.toAmountStr())
    }

    @JvmStatic
    @BindingAdapter("app:statistics")
    fun bindStatistics(view: QuantityStatisticsView, statistics: ArrayList<QuantityModel>?) {
        view.setStatisticsData(statistics.orEmpty().toMutableList())
    }
}