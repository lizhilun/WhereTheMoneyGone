package com.lizl.wtmg.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.core.view.isVisible
import com.lizl.wtmg.R
import kotlinx.android.synthetic.main.layout_custom_title_bar.view.*
import skin.support.widget.SkinCompatFrameLayout

class CustomTitleBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr)
{
    private var onBackBtnClickListener: (() -> Unit)? = null
    private var onActionClickListener: (() -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_custom_title_bar, null).apply {

            addView(this)

            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar)
            iv_back.isVisible = typeArray.getBoolean(R.styleable.CustomTitleBar_backBtnVisible, true)
            tv_title.text = typeArray.getString(R.styleable.CustomTitleBar_titleText)
            tv_action.text = typeArray.getString(R.styleable.CustomTitleBar_actionText)
            typeArray.recycle()

            iv_back.setOnClickListener { onBackBtnClickListener?.invoke() }
            tv_action.setOnClickListener { onActionClickListener?.invoke() }
        }
    }

    fun setOnBackBtnClickListener(onBackBtnClickListener: () -> Unit)
    {
        this.onBackBtnClickListener = onBackBtnClickListener
    }

    fun setOnActionClickListener(onActionClickListener: () -> Unit)
    {
        this.onActionClickListener = onActionClickListener
    }
}