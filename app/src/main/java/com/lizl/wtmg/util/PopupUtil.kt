package com.lizl.wtmg.util

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.widget.DatePicker
import android.widget.TimePicker
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.popup.*
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.mvvm.activity.AddAccountActivity
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
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

    fun showBottomAccountList(onSelectFinishListener: (AccountModel) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
            AppDatabase.getInstance().getAccountDao().queryAllAccount().groupBy { it.category }.forEach { (category, accountList) ->

                val childList = mutableListOf<PolymerizeChildModel>()
                accountList.forEach { childList.add(PolymerizeChildModel(it.type.getIcon(), it.type.translate(), "", it)) }
                add(PolymerizeGroupModel(category.translate(), "", childList))
            }
            add(PolymerizeChildModel(R.drawable.ic_baseline_add_colourful_24, context.getString(R.string.add), "", "A"))
        }) {
            if (it.tag == "A")
            {
                ActivityUtils.startActivity(AddAccountActivity::class.java)
                return@showBottomListPopup
            }
            onSelectFinishListener.invoke(it.tag as AccountModel)
        }
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