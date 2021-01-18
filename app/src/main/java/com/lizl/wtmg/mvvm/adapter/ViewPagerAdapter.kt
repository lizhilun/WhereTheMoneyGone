package com.lizl.wtmg.mvvm.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout.LayoutParams
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R

class ViewPagerAdapter(viewList: MutableList<View>) : BaseQuickAdapter<View, BaseViewHolder>(R.layout.item_view_container, viewList)
{
    override fun convert(helper: BaseViewHolder, item: View)
    {
        with(helper.itemView) {
            if (this !is ViewGroup) return
            removeAllViews()
            item.layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
            addView(item)
        }
    }
}