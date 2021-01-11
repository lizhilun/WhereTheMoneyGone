package com.lizl.wtmg.custom.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ColorUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.mvvm.adapter.InputKeyGridAdapter
import com.lizl.wtmg.mvvm.model.InputKeyModel

class InputKeyView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        layoutManager = GridLayoutManager(context, 4)
        adapter = InputKeyGridAdapter(getInputKeyList())
        overScrollMode = OVER_SCROLL_NEVER
    }

    private fun getInputKeyList(): MutableList<InputKeyModel>
    {
        return mutableListOf<InputKeyModel>().apply {
            add(InputKeyModel("1"))
            add(InputKeyModel("2"))
            add(InputKeyModel("3"))
            add(InputKeyModel("D", R.drawable.ic_baseline_backspace_24))
            add(InputKeyModel("4"))
            add(InputKeyModel("5"))
            add(InputKeyModel("6"))
            add(InputKeyModel("-"))
            add(InputKeyModel("7"))
            add(InputKeyModel("8"))
            add(InputKeyModel("9"))
            add(InputKeyModel("+"))
            add(InputKeyModel(context.getString(R.string.record_once_more)))
            add(InputKeyModel("0"))
            add(InputKeyModel("."))
            add(InputKeyModel(context.getString(R.string.save), keyBgResId = ColorUtils.getColor(R.color.colorPrimary)))
        }
    }
}