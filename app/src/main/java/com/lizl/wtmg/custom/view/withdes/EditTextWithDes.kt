package com.lizl.wtmg.custom.view.withdes

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setHintTextSize
import kotlinx.android.synthetic.main.layout_editext_with_des.view.*
import skin.support.widget.SkinCompatFrameLayout

class EditTextWithDes(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : SkinCompatFrameLayout(context, attrs, defStyleAttr)
{
    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    init
    {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?)
    {
        LayoutInflater.from(context).inflate(R.layout.layout_editext_with_des, null).apply {

            addView(this)

            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.InputItemEditView)

            tv_des.text = typeArray.getString(R.styleable.InputItemEditView_decText)
            et_edit.hint = typeArray.getString(R.styleable.InputItemEditView_hint)
            et_edit.setHintTextSize(typeArray.getDimension(R.styleable.InputItemEditView_hintTextSize,
                    context.resources.getDimension(R.dimen.global_text_size_smaller)))

            typeArray.recycle()
        }
    }

    fun setEditText(text: String)
    {
        et_edit.setText(text)
    }

    fun getEditText(): String
    {
        return et_edit.text.toString()
    }
}