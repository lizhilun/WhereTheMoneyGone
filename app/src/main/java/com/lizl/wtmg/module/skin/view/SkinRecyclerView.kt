package com.lizl.wtmg.module.skin.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import skin.support.widget.SkinCompatBackgroundHelper
import skin.support.widget.SkinCompatSupportable

class SkinRecyclerView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : RecyclerView(context, attrs, defStyleAttr), SkinCompatSupportable
{
    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private var mBackgroundTintHelper: SkinCompatBackgroundHelper? = null

    init
    {
        mBackgroundTintHelper = SkinCompatBackgroundHelper(this).apply { loadFromAttributes(attrs, defStyleAttr) }
    }

    override fun setBackgroundResource(@DrawableRes
                                       resId: Int)
    {
        super.setBackgroundResource(resId)
        mBackgroundTintHelper?.onSetBackgroundResource(resId)
    }

    override fun applySkin()
    {
        mBackgroundTintHelper?.applySkin()
    }
}