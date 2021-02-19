package com.lizl.wtmg.custom.popup

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R.drawable
import com.lizl.wtmg.R.string
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.popup.*
import com.lizl.wtmg.custom.popup.operaion.OperationModel
import com.lizl.wtmg.custom.popup.operaion.PopupOperationList
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.activity.AddAccountActivity
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lizl.wtmg.util.DateUtil.Date
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.coroutines.*
import java.util.*

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

    fun showBottomListPopup(bottomList: MutableList<PolymerizeModel>, onSelectFinishListener: (PolymerizeChildModel) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupBottomList(context, bottomList, onSelectFinishListener)))
    }

    fun showSetPasswordPopup(onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_NEW, null, onInputFinishListener)))
    }

    fun showModifyPasswordPopup(oldPassword: String, onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_MODIFY, oldPassword, onInputFinishListener)))
    }

    fun showConfirmPasswordPopup(rightPassword: String, onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_CHECK, rightPassword, onInputFinishListener)))
    }

    fun showInputPasswordPopup(onInputFinishListener: (String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_INPUT, null, onInputFinishListener)))
    }

    fun showBottomAccountList(onSelectFinishListener: (AccountModel) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
            AppDatabase.getInstance().getAccountDao().queryAllAccount().groupBy { it.category }.forEach { (category, accountList) ->

                val childList = mutableListOf<PolymerizeChildModel>()
                accountList.forEach { childList.add(PolymerizeChildModel(it.type.getIcon(), it.type.translate(), "", it)) }
                add(PolymerizeGroupModel(category.translate(), "", childList))
            }
            add(PolymerizeChildModel(drawable.ic_baseline_add_colourful_24, context.getString(string.add), "", "A"))
        }) {
            if (it.tag == "A")
            {
                ActivityUtils.startActivity(AddAccountActivity::class.java)
                return@showBottomListPopup
            }
            onSelectFinishListener.invoke(it.tag as AccountModel)
        }
    }

    fun showTracesDetailPopup(tracesModel: MoneyTracesModel)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupTracesDetail(context, tracesModel)))
    }

    fun showOperationPopup(operationList: MutableList<OperationModel>)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupOperationList(context, operationList)))
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

    fun showDataAndTimePickerDialog(date: Date = Date(), callback: (Date) -> Unit)
    {
        showDatePickerDialog(date.year, date.month - 1, date.day) { _, year, month, dayOfMonth ->
            showTimePickerDialog(date.hour, date.minute) { _, hourOfDay, minute ->
                val selectedDate = Date().apply { set(year, month + 1, dayOfMonth, hourOfDay, minute, 0) }
                callback.invoke(selectedDate)
            }
        }
    }

    fun showMonthSelectPopup(listener: (year: Int, month: Int) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(MonthSelectPopup(context, listener)))
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