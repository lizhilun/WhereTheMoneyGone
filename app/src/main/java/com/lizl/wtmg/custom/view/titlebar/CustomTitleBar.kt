package com.lizl.wtmg.custom.view.titlebar

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.view.StatusBarPlaceholder
import kotlinx.android.synthetic.main.layout_custom_title_bar.view.*

class CustomTitleBar(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : LinearLayout(context, attrs, defStyleAttr)
{
    private var onBackBtnClickListener: (() -> Unit)? = null

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        orientation = VERTICAL
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        addView(StatusBarPlaceholder(context))

        LayoutInflater.from(context).inflate(R.layout.layout_custom_title_bar, null).apply {

            addView(this)

            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar)
            iv_back.isVisible = typeArray.getBoolean(R.styleable.CustomTitleBar_backBtnVisible, true)
            tv_title.text = typeArray.getString(R.styleable.CustomTitleBar_titleText)
            iv_back.setImageResource(typeArray.getResourceId(R.styleable.CustomTitleBar_backBtnSrc, R.drawable.ic_baseline_arrow_back_24))
            typeArray.recycle()

            iv_back.setOnClickListener { onBackBtnClickListener?.invoke() }
        }
    }

    fun setTitle(text: String)
    {
        tv_title.text = text
    }

    fun setActionList(btnList: List<TitleBarBtnBean.BaseBtnBean>)
    {
        rv_action_list.adapter = TitleBarBtnListAdapter(btnList)
    }

    fun setOnBackBtnClickListener(onBackBtnClickListener: () -> Unit)
    {
        this.onBackBtnClickListener = onBackBtnClickListener
    }
}