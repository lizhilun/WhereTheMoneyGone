package com.lizl.wtmg.mvvm.adapter

import androidx.core.view.isVisible
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.lizl.wtmg.R
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import kotlinx.android.synthetic.main.item_bottom_polymerize_child.view.*
import kotlinx.android.synthetic.main.item_bottom_polymerize_group.view.*

class BottomListAdapter(bottomList: MutableList<PolymerizeModel>) : BaseDBMultiAdapter<PolymerizeModel, BaseViewHolder>(bottomList) {
    companion object {
        private const val ITEM_TYPE_GROUP = 1
        private const val ITEM_TYPE_CHILD = 2
    }

    private var onChildItemClickListener: ((PolymerizeChildModel) -> Unit)? = null

    override fun registerItemType(item: PolymerizeModel) = when (item) {
        is PolymerizeGroupModel -> ITEM_TYPE_GROUP
        is PolymerizeChildModel -> ITEM_TYPE_CHILD
        else -> ITEM_TYPE_CHILD
    }

    override fun registerItemLayout() = mutableListOf<Pair<Int, Int>>().apply {
        add(Pair(ITEM_TYPE_GROUP, R.layout.item_bottom_polymerize_group))
        add(Pair(ITEM_TYPE_CHILD, R.layout.item_bottom_polymerize_child))
    }

    override fun convert(helper: BaseViewHolder, item: PolymerizeModel) {
        with(helper.itemView) {
            when (item) {
                is PolymerizeGroupModel -> {
                    tv_group_name.text = item.name
                    tv_group_info.text = item.info

                    val childListAdapter = BottomListAdapter(item.childList as MutableList<PolymerizeModel>)
                    rv_child_list.adapter = childListAdapter

                    childListAdapter.setOnChildItemClickListener { onChildItemClickListener?.invoke(it) }
                }
                is PolymerizeChildModel -> {
                    iv_child_icon.isVisible = item.icon != null
                    item.icon?.let { iv_child_icon.setImageResource(it) }

                    tv_child_name.text = item.name

                    setOnClickListener { onChildItemClickListener?.invoke(item) }
                }
            }
        }
    }

    fun setOnChildItemClickListener(onChildItemClickListener: (PolymerizeChildModel) -> Unit) {
        this.onChildItemClickListener = onChildItemClickListener
    }
}