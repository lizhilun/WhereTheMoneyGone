package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.other.CustomDiffUtil
import com.lizl.wtmg.databinding.ItemPolymerizeChildBinding
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel

class PolymerizeChildAdapter(childList: MutableList<PolymerizeChildModel>) : BaseDBAdapter<PolymerizeChildModel, BaseViewHolder, ItemPolymerizeChildBinding>(R.layout.item_polymerize_child,
                                                                                                                                                             childList)
{
    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem == newItem }, { oldItem, newItem -> oldItem == newItem }))
    }

    override fun bindViewHolder(dataBinding: ItemPolymerizeChildBinding, item: PolymerizeChildModel)
    {
        with(dataBinding) {
            polymerizeChildModel = item
        }
    }
}