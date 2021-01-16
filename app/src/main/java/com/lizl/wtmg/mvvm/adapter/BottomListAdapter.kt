package com.lizl.wtmg.mvvm.adapter

import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.mvvm.model.BottomModel
import kotlinx.android.synthetic.main.item_bottom_list.view.*

class BottomListAdapter(bottomList: MutableList<BottomModel>) : BaseQuickAdapter<BottomModel, BaseViewHolder>(R.layout.item_bottom_list, bottomList)
{
    override fun convert(helper: BaseViewHolder, item: BottomModel)
    {
        with(helper.itemView) {
            tv_name.text = item.name
            iv_icon.isVisible = item.icon != null
            iv_icon.setImageResource(item.icon ?: R.color.transparent)
        }
    }
}