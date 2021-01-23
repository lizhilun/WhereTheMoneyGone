package com.lizl.wtmg.custom.view.withdes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import com.lizl.wtmg.R
import com.lizl.wtmg.module.skin.view.SkinImageView
import skin.support.constraint.SkinCompatConstraintLayout
import skin.support.widget.SkinCompatTextView

class TextViewWithDes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatConstraintLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    companion object
    {
        const val VIEW_TYPE_TOP_BOTTOM = 1
        const val VIEW_TYPE_START_END = 2
    }

    private val tvDec = LayoutInflater.from(context).inflate(R.layout.layout_textview, null) as SkinCompatTextView
    private val tvMain = LayoutInflater.from(context).inflate(R.layout.layout_textview, null) as SkinCompatTextView
    private val ivRight = LayoutInflater.from(context).inflate(R.layout.layout_imageview, null) as SkinImageView

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        tvDec.id = View.generateViewId()
        addView(tvDec)

        tvMain.id = View.generateViewId()
        addView(tvMain)

        ivRight.setImageResource(R.drawable.ic_baseline_keyboard_arrow_right_24)
        ivRight.id = View.generateViewId()

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithDes)

        tvDec.text = typeArray.getString(R.styleable.TextViewWithDes_decTextStr)
        tvMain.setTextAppearance(typeArray.getResourceId(R.styleable.TextViewWithDes_decTextAppearance, R.style.GlobalSecondaryTextStyle))
        tvMain.setTextAppearance(typeArray.getResourceId(R.styleable.TextViewWithDes_mainTextAppearance, R.style.GlobalTextStyle))

        if (typeArray.getBoolean(R.styleable.TextViewWithDes_rightArrowVisible, false))
        {
            addView(ivRight)
        }

        val constraintSet = ConstraintSet()

        constraintSet.constrainWidth(tvDec.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(tvDec.id, LayoutParams.WRAP_CONTENT)

        constraintSet.constrainWidth(tvMain.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(tvMain.id, LayoutParams.WRAP_CONTENT)

        constraintSet.constrainWidth(ivRight.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(ivRight.id, LayoutParams.WRAP_CONTENT)

        when (typeArray.getInt(R.styleable.TextViewWithDes_viewType, VIEW_TYPE_TOP_BOTTOM))
        {
            VIEW_TYPE_START_END  ->
            {
                constraintSet.connect(tvDec.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(tvDec.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                constraintSet.connect(tvDec.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

                constraintSet.connect(tvMain.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(tvMain.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                constraintSet.connect(tvMain.id, ConstraintSet.END, ivRight.id, ConstraintSet.START)

                constraintSet.connect(ivRight.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(ivRight.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                constraintSet.connect(ivRight.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
            }
            VIEW_TYPE_TOP_BOTTOM ->
            {
                constraintSet.connect(tvDec.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(tvDec.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(tvDec.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                constraintSet.connect(tvDec.id, ConstraintSet.BOTTOM, tvMain.id, ConstraintSet.TOP)
                constraintSet.setVerticalChainStyle(tvDec.id, ConstraintSet.CHAIN_PACKED)

                constraintSet.connect(tvMain.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)
                constraintSet.connect(tvMain.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                constraintSet.connect(tvMain.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(tvMain.id, ConstraintSet.TOP, tvMain.id, ConstraintSet.BOTTOM)
            }
        }

        constraintSet.applyTo(this)

        typeArray.recycle()
    }

    fun setDecText(text: String)
    {
        tvDec.text = text
    }

    fun setMainText(text: String)
    {
        tvMain.text = text
    }
}