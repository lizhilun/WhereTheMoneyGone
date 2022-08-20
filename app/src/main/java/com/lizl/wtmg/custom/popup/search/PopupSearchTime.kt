package com.lizl.wtmg.custom.popup.search

import android.content.Context
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ScreenUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.mvvm.model.DateModel
import com.lxj.xpopup.core.AttachPopupView
import kotlinx.android.synthetic.main.popup_search_time.view.*

class PopupSearchTime(context: Context, private val callback: (Int, Long, Long) -> Unit) : AttachPopupView(context) {
    override fun getImplLayoutId() = R.layout.popup_search_time

    companion object {
        const val TIME_TYPE_CURRENT_MONTH = 1
        const val TIME_TYPE_LAST_MONTH = 2
        const val TIME_TYPE_CURRENT_YEAR = 3
        const val TIME_TYPE_LAST_YEAR = 4
        const val TIME_TYPE_ALL = 5
        const val TIME_TYPE_CUSTOM = 6
    }

    private var startTime = 0L
    private var endTime = 0L

    override fun onCreate() {
        popupInfo?.let {
            it.hasShadowBg = false
        }

        tv_current_month.setOnClickListener {
            val now = DateModel()
            val startTime = DateModel.dayStart(now.getYear(), now.getMonth()).getTimeInMills()
            val endTime = DateModel.monthEnd(now.getYear(), now.getMonth()).getTimeInMills()
            callback.invoke(TIME_TYPE_CURRENT_MONTH, startTime, endTime)
            dismiss()
        }

        tv_last_month.setOnClickListener {
            val lastMonth = DateModel().minusMonth(1)
            val startTime = DateModel.monthStart(lastMonth.getYear(), lastMonth.getMonth()).getTimeInMills()
            val endTime = DateModel.monthEnd(lastMonth.getYear(), lastMonth.getMonth()).getTimeInMills()
            callback.invoke(TIME_TYPE_LAST_MONTH, startTime, endTime)
            dismiss()
        }

        tv_current_year.setOnClickListener {
            val now = DateModel()
            val startTime = DateModel.yearStart(now.getYear()).getTimeInMills()
            val endTime = DateModel.yearEnd(now.getYear()).getTimeInMills()
            callback.invoke(TIME_TYPE_CURRENT_YEAR, startTime, endTime)
            dismiss()
        }

        tv_last_year.setOnClickListener {
            val now = DateModel()
            val startTime = DateModel.yearStart(now.getYear() - 1).getTimeInMills()
            val endTime = DateModel.yearEnd(now.getYear() - 1).getTimeInMills()
            callback.invoke(TIME_TYPE_LAST_YEAR, startTime, endTime)
            dismiss()
        }

        tv_all_time.setOnClickListener {
            val startTime = 0L
            val endTime = Long.MAX_VALUE
            callback.invoke(TIME_TYPE_ALL, startTime, endTime)
            dismiss()
        }

        tv_start_time.setOnClickListener {
            val now = DateModel()
            PopupUtil.showDatePickerDialog(now.getYear(), now.getMonth(), now.getDay()) { year, month, day ->
                startTime = DateModel.dayStart(year, month, day).getTimeInMills()
                tv_start_time.text = "%d-%02d-%02d".format(year, month, day)
                tv_confirm.isVisible = startTime in 1 until endTime
            }
        }

        tv_end_time.setOnClickListener {
            val now = DateModel()
            PopupUtil.showDatePickerDialog(now.getYear(), now.getMonth(), now.getDay()) { year, month, day ->
                endTime = DateModel.dayEnd(year, month, day).getTimeInMills()
                tv_end_time.text = "%d-%02d-%02d".format(year, month, day)
                tv_confirm.isVisible = startTime in 1 until endTime
            }
        }

        tv_confirm.setOnClickListener {
            callback.invoke(TIME_TYPE_CUSTOM, startTime, endTime)
            dismiss()
        }
    }

    override fun getPopupWidth(): Int = ScreenUtils.getScreenWidth()
}