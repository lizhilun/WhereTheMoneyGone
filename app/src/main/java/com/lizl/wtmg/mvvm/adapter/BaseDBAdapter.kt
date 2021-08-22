package com.lizl.wtmg.mvvm.adapter

import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

abstract class BaseDBAdapter<T, VH : BaseViewHolder, DB : ViewDataBinding>(@LayoutRes private val layoutResId: Int, data: MutableList<T>? = null) :
        BaseQuickAdapter<T, VH>(layoutResId, data)
{
    override fun onItemViewHolderCreated(viewHolder: VH, viewType: Int)
    {
        DataBindingUtil.bind<DB>(viewHolder.itemView)
    }

    override fun convert(helper: VH, item: T)
    {
        DataBindingUtil.getBinding<DB>(helper.itemView)?.apply {
            bindViewHolder(this, item)
        }
    }

    override fun convert(helper: VH, item: T, payloads: List<Any>)
    {
        DataBindingUtil.getBinding<DB>(helper.itemView)?.apply {
            bindViewHolder(this, item, payloads)
        }
    }

    abstract fun bindViewHolder(dataBinding: DB, item: T)

    open fun bindViewHolder(dataBinding: DB, item: T, payloads: List<Any>)
    {

    }
}