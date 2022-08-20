package com.lizl.wtmg.custom.popup

import android.content.Context
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatRadioButton
import com.lizl.wtmg.R
import com.lxj.xpopup.core.CenterPopupView
import kotlinx.android.synthetic.main.popup_radio_group.view.*
import skin.support.widget.SkinCompatRadioButton

class PopupRadioGroup(context: Context, private val title: String, private val radioList: List<String>, private val checkedRadio: String,
                      private val onSelectFinishListener: (String) -> Unit) : CenterPopupView(context) {

    override fun getImplLayoutId() = R.layout.popup_radio_group

    override fun onCreate() {
        tv_title.text = title

        val padding = context.resources.getDimensionPixelOffset(R.dimen.global_content_padding_content) / 2

        radioList.forEach {
            val radioButton = SkinCompatRadioButton(context).apply {
                setPadding(padding, padding, padding, padding)
                setTextAppearance(R.style.GlobalTextStyle)
                setButtonDrawable(R.drawable.ic_check_button)
                id = View.generateViewId()
                text = it
            }
            rv_radio_group.addView(radioButton, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            if (it == checkedRadio) {
                rv_radio_group.check(radioButton.id)
            }
        }

        tv_cancel.setOnClickListener { dismiss() }

        tv_confirm.setOnClickListener {
            val checkedRadio = rv_radio_group.findViewById<AppCompatRadioButton>(rv_radio_group.checkedRadioButtonId)
                    ?: return@setOnClickListener
            if (this.checkedRadio != checkedRadio.text.toString()) {
                onSelectFinishListener.invoke(checkedRadio.text.toString())
            }
            dismiss()
        }
    }
}