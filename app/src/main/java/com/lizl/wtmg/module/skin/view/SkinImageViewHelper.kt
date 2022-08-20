package com.lizl.wtmg.module.skin.view

import android.content.res.TypedArray
import android.util.AttributeSet
import com.lizl.wtmg.R
import skin.support.content.res.SkinCompatResources
import skin.support.content.res.SkinCompatVectorResources
import skin.support.widget.SkinCompatHelper

class SkinImageViewHelper(private val view: SkinImageView) {
    private var mSrcResId = SkinCompatHelper.INVALID_ID
    private var mTintResId = SkinCompatHelper.INVALID_ID
    private var mSrcCompatResId = SkinCompatHelper.INVALID_ID

    fun loadFromAttributes(attrs: AttributeSet?, defStyleAttr: Int) {
        var a: TypedArray? = null
        try {
            a = view.context.obtainStyledAttributes(attrs, R.styleable.SkinImageView, defStyleAttr, 0)
            mSrcResId = a.getResourceId(R.styleable.SkinImageView_android_src, SkinCompatHelper.INVALID_ID)
            mTintResId = a.getResourceId(R.styleable.SkinImageView_android_tint, SkinCompatHelper.INVALID_ID)
            mSrcCompatResId = a.getResourceId(R.styleable.SkinImageView_srcCompat, SkinCompatHelper.INVALID_ID)
        } finally {
            a?.recycle()
        }
        applySkin()
    }

    fun setImageResource(resId: Int) {
        mSrcResId = resId
        applySkin()
    }

    fun applySkin() {
        mSrcCompatResId = SkinCompatHelper.checkResourceId(mSrcCompatResId)
        if (mSrcCompatResId != SkinCompatHelper.INVALID_ID) {
            val drawable = SkinCompatVectorResources.getDrawableCompat(view.context, mSrcCompatResId)
            if (drawable != null) {
                view.setImageDrawable(drawable)
            }
        } else {
            mSrcResId = SkinCompatHelper.checkResourceId(mSrcResId)
            if (mSrcResId == SkinCompatHelper.INVALID_ID) {
                return
            }
            val drawable = SkinCompatVectorResources.getDrawableCompat(view.context, mSrcResId)
            if (drawable != null) {
                view.setImageDrawable(drawable)
            }
        }

        applyTintResource()
    }

    private fun applyTintResource() {
        mTintResId = SkinCompatHelper.checkResourceId(mTintResId)
        if (mTintResId != SkinCompatHelper.INVALID_ID) {
            try {
                val color = SkinCompatResources.getColorStateList(view.context, mTintResId)
                view.imageTintList = color
            } catch (e: Exception) {
            }
        }
    }
}