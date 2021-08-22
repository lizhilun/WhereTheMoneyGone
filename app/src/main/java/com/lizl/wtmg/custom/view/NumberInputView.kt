package com.lizl.wtmg.custom.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.backspace
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.mvvm.adapter.InputKeyGridAdapter
import kotlinx.android.synthetic.main.layout_number_input.view.*
import kotlinx.coroutines.GlobalScope
import skin.support.widget.SkinCompatFrameLayout

class NumberInputView(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    private var onTextChangedListener: ((String) -> Unit)? = null

    private var onMainActionClickListener: (() -> Unit)? = null
    private var onSubActionClickListener: (() -> Unit)? = null

    private var inputTemp = StringBuilder()

    init
    {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_number_input, null).apply {
            addView(this)

            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.NumberInputView)
            tv_main_action.text = typeArray.getString(R.styleable.NumberInputView_mainActionText)
            tv_sub_action.text = typeArray.getString(R.styleable.NumberInputView_subActionText)
            typeArray.recycle()

            rv_number_list.adapter = InputKeyGridAdapter(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "-", "0", ".").toMutableList()).apply {
                setOnItemClickListener(true) { key: String ->
                    when (key)
                    {
                        "-"  ->
                        {
                            if (inputTemp.isNotEmpty())
                            {
                                return@setOnItemClickListener
                            }
                            inputTemp.append(key)
                        }
                        "."  ->
                        {
                            if (inputTemp.contains(key))
                            {
                                return@setOnItemClickListener
                            }
                            if (inputTemp.endsWith("-"))
                            {
                                inputTemp.append(0)
                            }
                            inputTemp.append(key)
                        }
                        else ->
                        {
                            if (inputTemp.contains(".") && inputTemp.indexOf(".") == inputTemp.length - 3)
                            {
                                return@setOnItemClickListener
                            }
                            inputTemp.append(key)
                        }
                    }
                    onTextChangedListener?.invoke(inputTemp.toString())
                }
            }

            iv_backspace.setOnClickListener(true) {
                inputTemp.backspace()
                onTextChangedListener?.invoke(inputTemp.toString())
            }

            tv_main_action.setOnClickListener(true) { onMainActionClickListener?.invoke() }

            tv_sub_action.setOnClickListener(true) { onSubActionClickListener?.invoke() }
        }
    }

    fun getInputNumber(): Double
    {
        if (inputTemp.isEmpty() || (inputTemp.contains("-") && inputTemp.length == 1))
        {
            return 0.0
        }
        return inputTemp.toString().toDouble()
    }

    fun setInputNumber(input: Double)
    {
        inputTemp.clear()
        inputTemp.append(input)
        Utils.runOnUiThread { onTextChangedListener?.invoke(inputTemp.toString()) }
    }

    fun clearInput()
    {
        inputTemp.clear()
        Utils.runOnUiThread { onTextChangedListener?.invoke("") }
    }

    fun setOnTextChangedListener(onTextChangedListener: (String) -> Unit)
    {
        this.onTextChangedListener = onTextChangedListener
    }

    fun setOnMainActionClickListener(onMainActionClickListener: () -> Unit)
    {
        this.onMainActionClickListener = onMainActionClickListener
    }

    fun setOnSubActionClickListener(onSubActionClickListener: () -> Unit)
    {
        this.onSubActionClickListener = onSubActionClickListener
    }
}