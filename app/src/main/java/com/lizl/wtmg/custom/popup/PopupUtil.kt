package com.lizl.wtmg.custom.popup

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R.drawable
import com.lizl.wtmg.R.string
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.launchMain
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.popup.*
import com.lizl.wtmg.custom.popup.operaion.OperationModel
import com.lizl.wtmg.custom.popup.operaion.PopupOperationList
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.activity.AddAccountActivity
import com.lizl.wtmg.mvvm.model.DateModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.coroutines.*
import java.util.*

object PopupUtil {
    private var curPopup: BasePopupView? = null
    private var curDialog: Dialog? = null
    private var showPopupJob: Job? = null

    fun showRadioGroupPopup(title: String, radioList: List<String>, checkedRadio: String, onSelectFinishListener: (result: String) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupRadioGroup(context, title, radioList, checkedRadio, onSelectFinishListener)))
    }

    fun showBottomListPopup(bottomList: MutableList<PolymerizeModel>, onSelectFinishListener: (PolymerizeChildModel) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupBottomList(context, bottomList, onSelectFinishListener)))
    }

    fun showSetPasswordPopup(onInputFinishListener: (String) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_NEW, null, onInputFinishListener)))
    }

    fun showModifyPasswordPopup(oldPassword: String, onInputFinishListener: (String) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_MODIFY, oldPassword, onInputFinishListener)))
    }

    fun showConfirmPasswordPopup(rightPassword: String, onInputFinishListener: (String) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_CHECK, rightPassword, onInputFinishListener)))
    }

    fun showInputPasswordPopup(onInputFinishListener: (String) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupPassword(context, PopupPassword.PASSWORD_OPERATION_INPUT, null, onInputFinishListener)))
    }

    fun showBottomAccountList(onSelectFinishListener: (AccountModel) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
            AppDatabase.getInstance().getAccountDao().queryAllAccount().filter { it.showInTotal }.groupBy { it.category }.forEach { (category, accountList) ->

                val childList = mutableListOf<PolymerizeChildModel>()
                accountList.forEach { childList.add(PolymerizeChildModel(it.type.getIcon(), it.type.translate(), "", it)) }
                add(PolymerizeGroupModel(category.translate(), "", childList))
            }
            add(PolymerizeChildModel(drawable.ic_baseline_add_colourful_24, context.getString(string.add), "", "A"))
        }) {
            if (it.tag == "A") {
                ActivityUtils.startActivity(AddAccountActivity::class.java)
                return@showBottomListPopup
            }
            onSelectFinishListener.invoke(it.tag as AccountModel)
        }
    }

    fun showTracesDetailPopup(tracesModel: MoneyTracesModel) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupTracesDetail(context, tracesModel)))
    }

    fun showOperationPopup(operationList: MutableList<OperationModel>) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupOperationList(context, operationList)))
    }

    fun showInputPopup(title: String, onInputFinish: (String) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupInput(context, title, onInputFinish)))
    }

    fun showConfirmPopup(notify: String, onConfirm: () -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupConfirm(context, notify, onConfirm)))
    }

    fun showDatePickerDialog(year: Int, month: Int, day: Int, callback: (year: Int, month: Int, day: Int) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showDialog(DatePickerDialog(context, { _, yearResult, monthOfYear, dayOfMonth -> callback.invoke(yearResult, monthOfYear + 1, dayOfMonth) },
                year, month - 1, day))
    }

    fun showTimePickerDialog(hour: Int, minute: Int, callback: (hour: Int, minute: Int) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showDialog(TimePickerDialog(context, { _, hourOfDay, minuteOfHour -> callback.invoke(hourOfDay, minuteOfHour) }, hour, minute, true))
    }

    fun showDataAndTimePickerDialog(date: DateModel = DateModel(), callback: (DateModel) -> Unit) {
        showDatePickerDialog(date.getYear(), date.getMonth(), date.getDay()) { year, month, dayOfMonth ->
            showTimePickerDialog(date.getHour(), date.getMinute()) { hourOfDay, minute ->
                val selectedDate = DateModel(year, month, dayOfMonth, hourOfDay, minute, 0)
                callback.invoke(selectedDate)
            }
        }
    }

    fun showMonthSelectPopup(withAllYear: Boolean = false, listener: (year: Int, month: Int) -> Unit) {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(MonthSelectPopup(context, withAllYear, listener)))
    }

    fun dismissAll() {
        launchMain {
            showPopupJob?.cancel()
            curPopup?.dismiss()
        }
    }

    private fun showPopup(popup: BasePopupView) {
        launchMain {
            showPopupJob?.cancel()
            showPopupJob = launchMain {
                if (curPopup?.isShow == true) {
                    curPopup?.dismiss()
                    delay(300)
                }
                curPopup = popup
                curPopup?.show()
            }
        }
    }

    private fun showDialog(dialog: Dialog) {
        launchMain {
            showPopupJob?.cancel()
            curPopup?.dismiss()
            curDialog?.dismiss()

            curDialog = dialog
            curDialog?.show()
        }
    }
}