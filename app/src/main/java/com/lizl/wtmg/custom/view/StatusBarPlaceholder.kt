package com.lizl.wtmg.custom.view

import android.content.Context
import android.util.AttributeSet
import com.blankj.utilcode.util.BarUtils
import skin.support.widget.SkinCompatFrameLayout
import skin.support.widget.SkinCompatView

class StatusBarPlaceholder(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init {
        SkinCompatView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, BarUtils.getStatusBarHeight())
            addView(this)
        }
    }
}