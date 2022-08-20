package com.lizl.wtmg.mvvm.adapter

import androidx.core.view.isVisible
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import kotlinx.android.synthetic.main.item_number_key.view.*

class NumberKeyAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_number_key) {
    override fun convert(helper: BaseViewHolder, item: String) {
        with(helper.itemView) {
            tv_name.text = item
            tv_name.isVisible = item.isNotBlank()
        }
    }
}