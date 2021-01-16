package com.lizl.wtmg.mvvm.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.databinding.ItemExpenditureTypeBinding
import com.lizl.wtmg.mvvm.model.ExpenditureTypeModel

class ExpenditureTypeAdapter : BaseQuickAdapter<ExpenditureTypeModel, BaseViewHolder>(R.layout.item_expenditure_type)
{
    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemExpenditureTypeBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: ExpenditureTypeModel)
    {
        helper.getBinding<ItemExpenditureTypeBinding>()?.apply {
            expenditureTypeModel = item
            executePendingBindings()
        }
    }
}