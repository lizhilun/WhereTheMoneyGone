package com.lizl.wtmg.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.custom.popup.PopupBottomList
import com.lizl.wtmg.custom.popup.PopupConfirm
import com.lizl.wtmg.custom.popup.PopupInput
import com.lizl.wtmg.custom.popup.PopupRadioGroup
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.coroutines.*

object PopupUtil
{
    private var curPopup: BasePopupView? = null
    private var curDialog: Dialog? = null
    private var showPopupJob: Job? = null

    fun showRadioGroupPopup(title: String, radioList: List<String>, checkedRadio: String, onSelectFinishListener: (result: String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupRadioGroup(context, title, radioList, checkedRadio, onSelectFinishListener)))
    }

    fun showBottomListPopup(bottomList: MutableList<BottomModel>, onSelectFinishListener: (BottomModel) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupBottomList(context, bottomList, onSelectFinishListener)))
    }

    fun showInputPopup(title: String, onInputFinish: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupInput(context, title, onInputFinish)))
    }

    fun showConfirmPopup(notify: String, onConfirm: () -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupConfirm(context, notify, onConfirm)))
    }

    fun showDatePickerDialog(year: Int, month: Int, day: Int, dateSetListener: (View: DatePicker, Int, Int, Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showDialog(DatePickerDialog(context, dateSetListener, year, month, day))
    }

    fun showTimePickerDialog(hour: Int, minute: Int, timeSetListener: (View: TimePicker, Int, Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showDialog(TimePickerDialog(context, timeSetListener, hour, minute, true))
    }

    fun dismissAll()
    {
        GlobalScope.launch(Dispatchers.Main) {
            showPopupJob?.cancel()
            curPopup?.dismiss()
        }
    }

    private fun showPopup(popup: BasePopupView)
    {
        GlobalScope.launch(Dispatchers.Main) {
            showPopupJob?.cancel()
            showPopupJob = GlobalScope.launch(Dispatchers.Main) {
                if (curPopup?.isShow == true)
                {
                    curPopup?.dismiss()
                    delay(300)
                }
                curPopup = popup
                curPopup?.show()
            }
        }
    }

    private fun showDialog(dialog: Dialog)
    {
        GlobalScope.launch(Dispatchers.Main) {
            showPopupJob?.cancel()
            curPopup?.dismiss()
            curDialog?.dismiss()

            curDialog = dialog
            curDialog?.show()
        }
    }
}