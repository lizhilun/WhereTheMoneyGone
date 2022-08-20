package com.lizl.wtmg.custom.view.selection

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.update
import com.lizl.wtmg.module.skin.view.SkinRecyclerView

class SingleSelectionView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinRecyclerView(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private val singleSelectionAdapter = SingleSelectionAdapter()

    private var onSelectionChangedListener: ((SingleSelectionModel) -> Unit)? = null

    init {
        adapter = singleSelectionAdapter
        layoutManager = GridLayoutManager(context, 5)
        overScrollMode = View.OVER_SCROLL_NEVER

        singleSelectionAdapter.setOnItemClickListener(true) { model ->
            if (model.isSelected) return@setOnItemClickListener
            singleSelectionAdapter.data.forEach {
                if (it.isSelected) {
                    it.isSelected = false
                    singleSelectionAdapter.update(it)
                }
            }
            model.isSelected = true
            singleSelectionAdapter.update(model)
            onSelectionChangedListener?.invoke(model)
        }
    }

    fun setData(selectionList: MutableList<SingleSelectionModel>) {
        singleSelectionAdapter.replaceData(selectionList)
    }

    fun setOnSelectionChangedListener(onSelectionChangedListener: (SingleSelectionModel) -> Unit) {
        this.onSelectionChangedListener = onSelectionChangedListener
    }
}