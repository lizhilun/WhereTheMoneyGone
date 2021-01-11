package com.lizl.wtmg.custom.function

import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.blankj.utilcode.util.VibrateUtils
import com.chad.library.adapter.base.BaseQuickAdapter

fun ViewPager2.registerOnPageChangeCallback(onPageSelectedListener: (position: Int) -> Unit)
{
    this.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback()
    {
        override fun onPageSelected(position: Int)
        {
            onPageSelectedListener.invoke(position)
        }
    })
}

fun <T> BaseQuickAdapter<T, *>.setOnItemClickListener(listener: (model: T) -> Unit)
{
    setOnItemClickListener { _, _, position ->
        val model = data.getOrNull(position)
        if (model != null)
        {
            listener.invoke(model)
        }
    }
}

fun <T> BaseQuickAdapter<T, *>.setOnItemLongClickListener(listener: (model: T) -> Unit)
{
    setOnItemLongClickListener { _, _, position ->
        val model = data.getOrNull(position)
        if (model != null)
        {
            listener.invoke(model)
        }
        true
    }
}

fun View.setOnClickListener(vibrate: Boolean, listener: (View) -> Unit)
{
    setOnClickListener {
        VibrateUtils.vibrate(30)
        listener.invoke(it)
    }
}