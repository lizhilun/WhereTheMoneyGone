package com.lizl.wtmg.custom.popup

import android.content.Context
import com.lizl.wtmg.R
import com.lizl.wtmg.mvvm.adapter.BottomListAdapter
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.popup_bottom_list.view.*

class PopupBottomList(context: Context, private val bottomList: MutableList<PolymerizeModel>, private val onSelectListener: (PolymerizeChildModel) -> Unit) :
    BottomPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_bottom_list

    override fun onCreate()
    {
        popupInfo?.let {
            it.hasShadowBg = true
        }

        rv_list.adapter = BottomListAdapter(bottomList).apply {
            setOnChildItemClickListener { polymerizeChildModel ->
                dismiss()
                onSelectListener.invoke(polymerizeChildModel)
            }
        }
    }
}