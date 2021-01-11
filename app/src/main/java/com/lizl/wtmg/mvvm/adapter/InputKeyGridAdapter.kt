package com.lizl.wtmg.mvvm.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import kotlinx.android.synthetic.main.item_input_key.view.*

class InputKeyGridAdapter(keyList: MutableList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_input_key, keyList)
{
    override fun convert(helper: BaseViewHolder, item: String)
    {
        with(helper.itemView) {
            tv_name.text = item
        }
    }
}