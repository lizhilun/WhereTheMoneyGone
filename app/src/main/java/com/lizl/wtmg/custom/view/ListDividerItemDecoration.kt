package com.lizl.wtmg.custom.view

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class ListDividerItemDecoration(private val dividerSize: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        outRect.bottom = dividerSize
    }
}