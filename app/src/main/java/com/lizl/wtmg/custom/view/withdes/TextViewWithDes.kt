package com.lizl.wtmg.custom.view.withdes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.lizl.wtmg.R
import kotlinx.android.synthetic.main.layout_text_view_with_dec.view.*
import skin.support.constraint.SkinCompatConstraintLayout

class TextViewWithDes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatConstraintLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_text_view_with_dec, null).apply {

            addView(this)

            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithDes)

            tv_dec.text = typeArray.getString(R.styleable.TextViewWithDes_decTextStr)
            tv_main.setTextAppearance(typeArray.getResourceId(R.styleable.TextViewWithDes_decTextAppearance, R.style.GlobalSecondaryTextStyle))
            tv_main.setTextAppearance(typeArray.getResourceId(R.styleable.TextViewWithDes_mainTextAppearance, R.style.GlobalTextStyle))

            typeArray.recycle()
        }
    }

    fun setDecText(text: String)
    {
        tv_dec?.text = text
    }

    fun setMainText(text: String)
    {
        tv_main?.text = text
    }
}