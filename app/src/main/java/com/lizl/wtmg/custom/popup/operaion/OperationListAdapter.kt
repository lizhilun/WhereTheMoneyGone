package com.lizl.wtmg.custom.popup.operaion

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import kotlinx.android.synthetic.main.item_operation.view.*

class OperationListAdapter(operationList: List<OperationModel>) :
    BaseQuickAdapter<OperationModel, BaseViewHolder>(R.layout.item_operation, operationList.toMutableList())
{
    private var onOperationItemClickListener: (() -> Unit)? = null

    override fun convert(helper: BaseViewHolder, item: OperationModel)
    {
        with(helper.itemView) {
            tv_operation_name.text = item.operationName
            setOnClickListener {
                onOperationItemClickListener?.invoke()
                item.operationItemCallBack.invoke()
            }
        }
    }

    fun setOnOperationItemClickListener(onOperationItemClickListener: () -> Unit)
    {
        this.onOperationItemClickListener = onOperationItemClickListener
    }
}