package com.lizl.wtmg.mvvm.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.databinding.ItemPolymerizeChildBinding

class PolymerizeChildAdapter(childList: MutableList<PolymerizeChildModel>) :
    BaseQuickAdapter<PolymerizeChildModel, BaseViewHolder>(R.layout.item_polymerize_child, childList)
{
    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemPolymerizeChildBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: PolymerizeChildModel)
    {
        helper.getBinding<ItemPolymerizeChildBinding>()?.apply {
            polymerizeChildModel = item
            executePendingBindings()
        }
    }
}