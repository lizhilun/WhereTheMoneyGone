package com.lizl.wtmg.util

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.custom.popup.PopupConfirm
import com.lizl.wtmg.custom.popup.PopupInput
import com.lizl.wtmg.custom.popup.PopupRadioGroup
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView
import kotlinx.coroutines.*


object PopupUtil
{
    private var curPopup: BasePopupView? = null
    private var showPopupJob: Job? = null

    fun showRadioGroupPopup(title: String, radioList: List<String>, checkedRadio: String, onSelectFinishListener: (result: String) -> Unit)
    {
        val context = ActivityUtils.getTopActivity() ?: return
        showPopup(XPopup.Builder(context).asCustom(PopupRadioGroup(context, title, radioList, checkedRadio, onSelectFinishListener)))
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
}