package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.db.model.PropertyModel
import kotlinx.android.synthetic.main.item_property.view.*

class PropertyListAdapter(propertyList: MutableList<PropertyModel>) : BaseQuickAdapter<PropertyModel, BaseViewHolder>(R.layout.item_property, propertyList)
{
    override fun convert(helper: BaseViewHolder, item: PropertyModel)
    {
        with(helper.itemView) {
            tv_name.text = item.name
            tv_amount.text = item.amount.toString()

            iv_icon.setImageResource(when (item.type)
            {
                AppConstant.PROPERTY_TYPE_CASH      -> R.drawable.ic_baseline_cash_24
                AppConstant.PROPERTY_TYPE_WE_CHAT   -> R.drawable.ic_baseline_we_chat_24
                AppConstant.PROPERTY_TYPE_ALI_PAY   -> R.drawable.ic_baseline_ali_pay_24
                AppConstant.PROPERTY_TYPE_BACK_CARD -> R.drawable.ic_baseline_bank_card_24
                else                                -> R.drawable.ic_baseline_cash_24
            })
        }
    }
}