package com.lizl.wtmg.module.skin.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatImageView
import skin.support.widget.SkinCompatBackgroundHelper
import skin.support.widget.SkinCompatSupportable

class SkinImageView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : AppCompatImageView(context, attrs, defStyleAttr), SkinCompatSupportable
{
    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private var mBackgroundTintHelper: SkinCompatBackgroundHelper? = null
    private var mSkinImageViewHelper: SkinImageViewHelper? = null

    init
    {
        mBackgroundTintHelper = SkinCompatBackgroundHelper(this).apply { loadFromAttributes(attrs, defStyleAttr) }
        mSkinImageViewHelper = SkinImageViewHelper(this).apply { loadFromAttributes(attrs, defStyleAttr) }
    }

    override fun setBackgroundResource(@DrawableRes
                                       resId: Int)
    {
        super.setBackgroundResource(resId)
        mBackgroundTintHelper?.onSetBackgroundResource(resId)
    }

    override fun setImageResource(@DrawableRes
                                  resId: Int)
    {
        mSkinImageViewHelper?.setImageResource(resId)
    }

    override fun applySkin()
    {
        mBackgroundTintHelper?.applySkin()
        mSkinImageViewHelper?.applySkin()
    }
}