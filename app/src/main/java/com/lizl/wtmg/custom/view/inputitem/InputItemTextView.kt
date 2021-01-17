package com.lizl.wtmg.custom.view.inputitem

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.lizl.wtmg.R
import kotlinx.android.synthetic.main.layout_input_item_text_view.view.*
import skin.support.widget.SkinCompatFrameLayout

class InputItemTextView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_input_item_text_view, null).apply {

            addView(this)

            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.InputItemTextView)

            tv_des.text = typeArray.getString(R.styleable.InputItemTextView_tvDecText)

            typeArray.recycle()
        }
    }

    fun setText(text: String)
    {
        tv_text.text = text
    }
}