package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.module.property.PropertyManager
import com.lizl.wtmg.mvvm.model.PropertyCategoryGroupModel
import kotlinx.android.synthetic.main.item_property_category_group.view.*

class PropertyCategoryGroupAdapter : BaseQuickAdapter<PropertyCategoryGroupModel, BaseViewHolder>(R.layout.item_property_category_group)
{
    override fun convert(helper: BaseViewHolder, item: PropertyCategoryGroupModel)
    {
        with(helper.itemView) {
            tv_name.text = PropertyManager.getPropertyCategoryName(item.category)
            rv_property_list.adapter = PropertyListAdapter(item.propertyList)
        }
    }
}