package com.lizl.wtmg.mvvm.adapter

import androidx.core.view.isGone
import com.blankj.utilcode.util.ColorUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.module.skin.util.SkinUtil
import com.lizl.wtmg.mvvm.model.InputKeyModel
import kotlinx.android.synthetic.main.item_input_key.view.*

class InputKeyGridAdapter(keyList: MutableList<InputKeyModel>) : BaseQuickAdapter<InputKeyModel, BaseViewHolder>(R.layout.item_input_key, keyList)
{
    override fun convert(helper: BaseViewHolder, item: InputKeyModel)
    {
        with(helper.itemView) {

            tv_name.text = item.keyName
            tv_name.isGone = item.keyName.isNullOrBlank() || item.keyIconResId != null

            iv_key.isGone = item.keyIconResId != null
            item.keyIconResId?.let { iv_key.setImageResource(it) }

            if (item.keyBgResId != null)
            {
                setBackgroundResource(item.keyBgResId!!)
                tv_name.setTextColor(ColorUtils.getColor(R.color.white))
            }
            else
            {
                setBackgroundResource(R.color.transparent)
                tv_name.setTextColor(SkinUtil.getColor(context, R.color.colorTextColor))
            }
        }
    }
}