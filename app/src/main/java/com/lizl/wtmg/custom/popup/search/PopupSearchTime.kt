package com.lizl.wtmg.custom.popup.search

import android.content.Context
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ScreenUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.mvvm.model.DateModel
import com.lizl.wtmg.util.DateUtil
import com.lxj.xpopup.core.AttachPopupView
import kotlinx.android.synthetic.main.popup_search_time.view.*

class PopupSearchTime(context: Context, private val callback: (Int, Long, Long) -> Unit) : AttachPopupView(context)
{
    override fun getImplLayoutId() = R.layout.popup_search_time

    companion object
    {
        const val TIME_TYPE_CURRENT_MONTH = 1
        const val TIME_TYPE_LAST_MONTH = 2
        const val TIME_TYPE_CURRENT_YEAR = 3
        const val TIME_TYPE_LAST_YEAR = 4
        const val TIME_TYPE_ALL = 5
        const val TIME_TYPE_CUSTOM = 6
    }

    private var startTime = 0L
    private var endTime = 0L

    override fun onCreate()
    {
        popupInfo?.let {
            it.hasShadowBg = false
        }

        tv_current_month.setOnClickListener {
            val now = DateModel()
            val startTime = DateModel(now.getYear(), now.getMonth()).getTimeInMills()
            val endTime = DateModel(now.getYear(), now.getMonth(), DateUtil.getDayCountInMonth(now.getYear(), now.getMonth()), 23, 59, 59).getTimeInMills()
            callback.invoke(TIME_TYPE_CURRENT_MONTH, startTime, endTime)
            dismiss()
        }

        tv_last_month.setOnClickListener {
            val now = DateModel()
            val startTime = DateModel(if (now.getMonth() == 1) now.getYear() - 1 else now.getYear(), if (now.getMonth() == 1) 12 else now.getMonth() - 1).getTimeInMills()
            val endTime = DateModel(now.getYear(), now.getMonth()).getTimeInMills() - 1
            callback.invoke(TIME_TYPE_LAST_MONTH, startTime, endTime)
            dismiss()
        }

        tv_current_year.setOnClickListener {
            val now = DateModel()
            val startTime = DateModel(now.getYear()).getTimeInMills()
            val endTime = DateModel(now.getYear(), 12, 31, 23, 59, 59).getTimeInMills()
            callback.invoke(TIME_TYPE_CURRENT_YEAR, startTime, endTime)
            dismiss()
        }

        tv_last_year.setOnClickListener {
            val now = DateModel()
            val startTime = DateModel(now.getYear() - 1).getTimeInMills()
            val endTime = DateModel(now.getYear()).getTimeInMills() - 1
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
            PopupUtil.showDatePickerDialog(now.getYear(), now.getMonth(), now.getDay()) { _, year, month, day ->
                startTime = DateModel(year, month, day).getTimeInMills()
                tv_start_time.text = "%d-%02d-%02d".format(year, month, day)
                tv_confirm.isVisible = startTime in 1 until endTime
            }
        }

        tv_end_time.setOnClickListener {
            val now = DateModel()
            PopupUtil.showDatePickerDialog(now.getYear(), now.getMonth(), now.getDay()) { _, year, month, day ->
                endTime = DateModel(year, month, day, 23, 59, 59).getTimeInMills()
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