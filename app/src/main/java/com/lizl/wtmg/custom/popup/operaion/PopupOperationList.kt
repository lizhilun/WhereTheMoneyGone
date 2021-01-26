package com.lizl.wtmg.custom.popup.operaion

import android.content.Context
import com.lizl.wtmg.R
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.popup_operation_list.view.*

class PopupOperationList(context: Context, private var operationList: List<OperationModel>) : CenterPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_operation_list

    override fun onCreate()
    {
        rv_operation_list.adapter = OperationListAdapter(operationList).apply {
            setOnOperationItemClickListener { dismiss() }
        }
    }
}