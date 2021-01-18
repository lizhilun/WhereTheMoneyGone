package com.lizl.wtmg.custom.view.selection

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.ItemSingleSelectionBinding

class SingleSelectionAdapter : BaseQuickAdapter<SingleSelectionModel, BaseViewHolder>(R.layout.item_single_selection)
{
    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemSingleSelectionBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: SingleSelectionModel)
    {
        helper.getBinding<ItemSingleSelectionBinding>()?.apply {
            singleSelectionModel = item
            executePendingBindings()
        }
    }
}