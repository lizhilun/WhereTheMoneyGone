package com.lizl.wtmg.mvvm.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.setOnItemLongClickListener
import com.lizl.wtmg.databinding.ItemPolymerizeGroupBinding
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel

class PolymerizeGroupAdapter : BaseQuickAdapter<PolymerizeGroupModel, BaseViewHolder>(R.layout.item_polymerize_group)
{

    private var onChildItemClickListener: ((PolymerizeChildModel) -> Unit)? = null

    private var onChildItemLongClickListener: ((PolymerizeChildModel) -> Unit)? = null

    override fun onItemViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int)
    {
        DataBindingUtil.bind<ItemPolymerizeGroupBinding>(viewHolder.itemView)
    }

    override fun convert(helper: BaseViewHolder, item: PolymerizeGroupModel)
    {
        helper.getBinding<ItemPolymerizeGroupBinding>()?.apply {
            polymerizeGroupModel = item

            rvChildList.adapter = PolymerizeChildAdapter(item.childList).apply {

                setOnItemClickListener { childModel -> onChildItemClickListener?.invoke(childModel) }

                setOnItemLongClickListener { childModel -> onChildItemLongClickListener?.invoke(childModel) }
            }
        }
    }

    fun setOnChildItemClickListener(onChildItemClickListener: (PolymerizeChildModel) -> Unit)
    {
        this.onChildItemClickListener = onChildItemClickListener
    }

    fun setOnChildItemLongClickListener(onChildItemLongClickListener: (PolymerizeChildModel) -> Unit)
    {
        this.onChildItemLongClickListener = onChildItemLongClickListener
    }
}