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

fun <T> BaseQuickAdapter<T, *>.setOnItemClickListener(vibrate: Boolean = false, listener: (model: T) -> Unit)
{
    setOnItemClickListener { _, _, position ->
        if (vibrate) VibrateUtils.vibrate(30)
        data.getOrNull(position)?.let { listener.invoke(it) }
    }
}

fun <T> BaseQuickAdapter<T, *>.setOnItemLongClickListener(listener: (model: T) -> Unit)
{
    setOnItemLongClickListener { _, _, position ->
        data.getOrNull(position)?.let { listener.invoke(it) }
        true
    }
}

fun View.setOnClickListener(vibrate: Boolean, listener: (View) -> Unit)
{
    setOnClickListener {
        if (vibrate) VibrateUtils.vibrate(30)
        listener.invoke(it)
    }
}