package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.db.model.PropertyModel
import com.lizl.wtmg.module.property.PropertyManager
import kotlinx.android.synthetic.main.item_property.view.*

class PropertyListAdapter(propertyList: MutableList<PropertyModel>) : BaseQuickAdapter<PropertyModel, BaseViewHolder>(R.layout.item_property, propertyList)
{
    override fun convert(helper: BaseViewHolder, item: PropertyModel)
    {
        with(helper.itemView) {
            tv_name.text = item.name
            tv_amount.text = item.amount.toString()
            iv_icon.setImageResource(PropertyManager.getPropertyIcon(item.type))
        }
    }
}