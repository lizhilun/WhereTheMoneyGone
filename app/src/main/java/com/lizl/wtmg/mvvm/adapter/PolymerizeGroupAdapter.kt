package com.lizl.wtmg.mvvm.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.setOnItemLongClickListener
import com.lizl.wtmg.databinding.ItemPolymerizeGroupBinding
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel

class PolymerizeGroupAdapter : BaseQuickAdapter<PolymerizeGroupModel, BaseViewHolder>(R.layout.item_polymerize_group)
{

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemPolymerizeGroupBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: PolymerizeGroupModel)
    {
        helper.getBinding<ItemPolymerizeGroupBinding>()?.apply {
            polymerizeGroupModel = item

            rvChildList.adapter = PolymerizeChildAdapter(item.childList).apply {

                setOnItemClickListener { propertyModel -> }

                setOnItemLongClickListener { propertyModel -> }
            }
        }
    }
}