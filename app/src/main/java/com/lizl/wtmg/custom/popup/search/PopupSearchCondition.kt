package com.lizl.wtmg.custom.popup.search

import android.content.Context
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import com.blankj.utilcode.util.ScreenUtils
import com.lizl.wtmg.R
import com.lxj.xpopup.core.AttachPopupView
import kotlinx.android.synthetic.main.popup_search_condition.view.*

class PopupSearchCondition(context: Context, private val callback: (Int, Int) -> Unit) : AttachPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_search_condition

    override fun onCreate()
    {
        popupInfo?.let {
            it.hasShadowBg = false
        }

        val onTextChanged = {
            val minAmount = et_min_amount.text.toString().toIntOrNull()
            val maxAmount = et_max_amount.text.toString().toIntOrNull()
            tv_confirm.isVisible = minAmount != null && maxAmount != null && maxAmount > minAmount
        }

        et_min_amount.addTextChangedListener { onTextChanged.invoke() }
        et_max_amount.addTextChangedListener { onTextChanged.invoke() }

        tv_confirm.setOnClickListener {
            val minAmount = et_min_amount.text.toString().toIntOrNull() ?: return@setOnClickListener
            val maxAmount = et_max_amount.text.toString().toIntOrNull() ?: return@setOnClickListener
            callback.invoke(minAmount, maxAmount)
            dismiss()
        }
    }

    override fun getPopupWidth(): Int = ScreenUtils.getScreenWidth()
}