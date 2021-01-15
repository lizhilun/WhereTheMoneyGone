package com.lizl.wtmg.custom.popup

import android.content.Context
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.mvvm.adapter.BottomListAdapter
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.popup_bottom_list.view.*

class PopupBottomList(context: Context, private val bottomList: MutableList<BottomModel>, private val onSelectListener: (BottomModel) -> Unit) :
    BottomPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_bottom_list

    override fun onCreate()
    {
        popupInfo?.let {
            it.hasShadowBg = true
            it.hasBlurBg = true
        }

        rv_list.adapter = BottomListAdapter(bottomList).apply {
            setOnItemClickListener { bottomModel ->
                dismiss()
                onSelectListener.invoke(bottomModel)
            }
        }
    }
}