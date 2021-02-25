package com.lizl.wtmg.custom.popup

import android.content.Context
import androidx.core.view.isVisible
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.TracesManager
import com.lizl.wtmg.mvvm.activity.MoneyTracesRecordActivity
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.util.DateUtil
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.layout_money_traces_detail.view.*

class PopupTracesDetail(context: Context, private val tracesModel: MoneyTracesModel) : BottomPopupView(context)
{
    override fun getImplLayoutId() = R.layout.layout_money_traces_detail

    override fun onCreate()
    {
        popupInfo?.let {
            it.hasShadowBg = true
        }

        layout_account_amount.setMainText(tracesModel.amount.toAmountStr())
        layout_account_type.setMainText(tracesModel.accountType.translate())
        layout_traces_type.setMainText(tracesModel.tracesType.translate())

        layout_traces_remarks.isVisible = tracesModel.remarks.isNotBlank()
        layout_traces_remarks.setMainText(tracesModel.remarks)

        layout_to_account_type.isVisible = tracesModel.transferToAccount.isNotBlank()
        when (tracesModel.tracesCategory)
        {
            AppConstant.MONEY_TRACES_CATEGORY_TRANSFER -> layout_to_account_type.setMainText(tracesModel.transferToAccount.translate())
            AppConstant.MONEY_TRACES_CATEGORY_DEBT -> layout_to_account_type.setMainText(tracesModel.transferToAccount)
        }

        val tracesTime = DateUtil.Date(tracesModel.recordTime)
        layout_traces_time.setMainText(tracesTime.toFormatString())

        tv_delete.setOnClickListener {
            TracesManager.deleteMoneyTraces(tracesModel)
            dismiss()
        }

        tv_modify.setOnClickListener {
            ActivityUtil.turnToActivity(MoneyTracesRecordActivity::class.java, Pair(MoneyTracesRecordActivity.DATA_TRACES_ID, tracesModel.id))
            dismiss()
        }
    }
}