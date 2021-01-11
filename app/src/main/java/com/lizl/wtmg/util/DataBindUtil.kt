package com.lizl.wtmg.util

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView

object DataBindUtil
{
    @JvmStatic
    @BindingAdapter("app:adapter")
    fun bindAdapter(recyclerView: RecyclerView, adapter: RecyclerView.Adapter<*>?)
    {
        recyclerView.adapter = adapter
    }
}