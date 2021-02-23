package com.lizl.wtmg.custom.view.tracesrecord

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import com.blankj.utilcode.util.SizeUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.module.skin.view.SkinImageView
import skin.support.constraint.SkinCompatConstraintLayout
import skin.support.widget.SkinCompatEditText

class AccountInputView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatConstraintLayout(context, attrs, defStyleAttr)
{
    private val TAG = "AccountInputView"

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var ivAccount = LayoutInflater.from(context).inflate(R.layout.layout_imageview, null) as SkinImageView
    private var etAccount = LayoutInflater.from(context).inflate(R.layout.layout_edittext, null) as SkinCompatEditText
    private var coverView = View(context)

    private var onLayoutClickListener: (() -> Unit)? = null

    private var accountType = ""

    init
    {
        initView(attrs)
    }


    companion object
    {
        const val TEXT_GRAVITY_START = 1
        const val TEXT_GRAVITY_END = 2
    }

    private fun initView(attrs: AttributeSet?)
    {
        ivAccount.id = View.generateViewId()
        ivAccount.imageTintList = null
        addView(ivAccount)

        etAccount.id = View.generateViewId()
        etAccount.maxEms = 8
        etAccount.maxLines = 1
        etAccount.isSingleLine = true
        etAccount.background = null
        addView(etAccount)

        coverView.id = View.generateViewId()
        addView(coverView)

        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.AccountInputView)

        val constraintSet = ConstraintSet()

        constraintSet.constrainWidth(ivAccount.id, LayoutParams.WRAP_CONTENT)
        constraintSet.constrainHeight(ivAccount.id, LayoutParams.WRAP_CONTENT)

        constraintSet.constrainWidth(etAccount.id, LayoutParams.MATCH_CONSTRAINT)
        constraintSet.constrainHeight(etAccount.id, LayoutParams.MATCH_PARENT)

        constraintSet.constrainWidth(coverView.id, LayoutParams.MATCH_PARENT)
        constraintSet.constrainHeight(coverView.id, LayoutParams.MATCH_PARENT)

        when (typeArray.getInt(R.styleable.AccountInputView_textGravity, TEXT_GRAVITY_START))
        {
            TEXT_GRAVITY_START ->
            {
                constraintSet.connect(ivAccount.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)
                constraintSet.connect(ivAccount.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(ivAccount.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

                constraintSet.connect(etAccount.id, ConstraintSet.START, ivAccount.id, ConstraintSet.END, SizeUtils.dp2px(5F))
                constraintSet.connect(etAccount.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)

                etAccount.gravity = Gravity.START or Gravity.CENTER_VERTICAL
            }
            TEXT_GRAVITY_END ->
            {
                constraintSet.connect(ivAccount.id, ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END)
                constraintSet.connect(ivAccount.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
                constraintSet.connect(ivAccount.id, ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM)

                constraintSet.connect(etAccount.id, ConstraintSet.END, ivAccount.id, ConstraintSet.START, SizeUtils.dp2px(5F))
                constraintSet.connect(etAccount.id, ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START)

                etAccount.gravity = Gravity.END or Gravity.CENTER_VERTICAL
            }
        }

        constraintSet.applyTo(this)

        typeArray.recycle()

        coverView.setOnClickListener { onLayoutClickListener?.invoke() }
    }

    fun setInputEnable(enable: Boolean)
    {
        ivAccount.isVisible = false
        etAccount.setText("")
        etAccount.isEnabled = enable
        coverView.isVisible = !enable
        accountType = ""
    }

    fun setHint(hint: String)
    {
        etAccount.hint = hint
    }

    fun setAccountType(accountType: String)
    {
        this.accountType = accountType
        ivAccount.isVisible = true
        ivAccount.setImageResource(accountType.getIcon())
        etAccount.setText(accountType.translate())
    }

    fun setAccountName(accountName: String)
    {
        this.accountType = ""
        etAccount.setText(accountName)
    }

    fun getHint() = etAccount.hint.toString()

    fun getInputAccountType() = if (accountType.isBlank()) etAccount.text.toString() else accountType

    fun setOnLayoutClickListener(onLayoutClickListener: (() -> Unit)? = null)
    {
        this.onLayoutClickListener = onLayoutClickListener
    }
}