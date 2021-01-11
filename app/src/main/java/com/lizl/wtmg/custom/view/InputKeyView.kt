package com.lizl.wtmg.custom.view

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.VibrateUtils
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.mvvm.adapter.InputKeyGridAdapter

class InputKeyView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var onKeyClickListener: ((String) -> Unit)? = null

    init
    {
        layoutManager = GridLayoutManager(context, 3)
        adapter = InputKeyGridAdapter(getInputKeyList()).apply {
            setOnItemClickListener { key: String ->
                VibrateUtils.vibrate(30)
                onKeyClickListener?.invoke(key)
            }
        }
        overScrollMode = OVER_SCROLL_NEVER
    }

    private fun getInputKeyList(): MutableList<String>
    {
        return listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "0", "+").toMutableList()
    }

    fun setOnKeyClickListener(onKeyClickListener: (String) -> Unit)
    {
        this.onKeyClickListener = onKeyClickListener
    }
}