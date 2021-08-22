package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.BaseDelegateMultiAdapter
import com.chad.library.adapter.base.delegate.BaseMultiTypeDelegate
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class BaseDBMultiAdapter<T, VH : BaseViewHolder>(data: MutableList<T>? = null) : BaseDelegateMultiAdapter<T, VH>(data)
{
    init
    {
        setMultiTypeDelegate(object : BaseMultiTypeDelegate<T>()
                             {
                                 override fun getItemType(data: List<T>, position: Int): Int
                                 {
                                     return registerItemType(data[position])
                                 }
                             })

        getMultiTypeDelegate()?.let {
            registerItemLayout().forEach { pair ->
                it.addItemType(pair.first, pair.second)
            }
        }
    }

    abstract fun registerItemType(item: T): Int

    abstract fun registerItemLayout(): List<Pair<Int, Int>>
}