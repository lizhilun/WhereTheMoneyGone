package com.lizl.wtmg.mvvm.adapter

import androidx.databinding.DataBindingUtil
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.setOnItemLongClickListener
import com.lizl.wtmg.custom.other.CustomDiffUtil
import com.lizl.wtmg.databinding.ItemPolymerizeGroupBinding
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel

class PolymerizeGroupAdapter : BaseQuickAdapter<PolymerizeGroupModel, BaseViewHolder>(R.layout.item_polymerize_group)
{

    companion object
    {
        private const val PAYLOAD_CHILD_LIST = "PAYLOAD_CHILD_LIST"
        private const val PAYLOAD_INFO = "PAYLOAD_INFO"
    }

    init
    {
        setDiffCallback(CustomDiffUtil({ oldItem, newItem -> oldItem.name == newItem.name }, { oldItem, newItem -> oldItem == newItem }, { oldItem, newItem ->
            mutableListOf<String>().apply {
                if (oldItem.info != newItem.info || oldItem.name != newItem.name) add(PAYLOAD_INFO)
                if (oldItem.childList != newItem.childList) add(PAYLOAD_CHILD_LIST)
            }
        }))
    }

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

            executePendingBindings()
        }
    }

    override fun convert(helper: BaseViewHolder, item: PolymerizeGroupModel, payloads: List<Any>)
    {
        helper.getBinding<ItemPolymerizeGroupBinding>()?.apply {
            payloads.forEach { payloadList ->
                if (payloadList !is MutableList<*>)
                {
                    return@forEach
                }
                payloadList.forEach { payload ->
                    when (payload)
                    {
                        PAYLOAD_CHILD_LIST ->
                        {
                            if (rvChildList.adapter is PolymerizeChildAdapter)
                            {
                                (rvChildList.adapter as PolymerizeChildAdapter).setDiffNewData(item.childList)
                            }
                        }
                        PAYLOAD_INFO -> polymerizeGroupModel = item
                    }
                }
            }

            executePendingBindings()
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