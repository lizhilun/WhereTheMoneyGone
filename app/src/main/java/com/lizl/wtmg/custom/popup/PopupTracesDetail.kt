package com.lizl.wtmg.custom.popup

import android.content.Context
import com.blankj.utilcode.util.StringUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountDataManager
import com.lizl.wtmg.mvvm.activity.MoneyTracesRecordActivity
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.util.DateUtil
import com.lxj.xpopup.core.BottomPopupView
import kotlinx.android.synthetic.main.layout_money_traces_detail.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PopupTracesDetail(context: Context, private val tracesModel: MoneyTracesModel) : BottomPopupView(context)
{
    override fun getImplLayoutId() = R.layout.layout_money_traces_detail

    override fun onCreate()
    {
        popupInfo?.let {
            it.hasShadowBg = true
        }

        layout_account_amount.setMainText(tracesModel.amonunt.toInt().toString())
        layout_account_type.setMainText(tracesModel.accountType.translate())
        layout_traces_type.setMainText(tracesModel.tracesType.translate())

        val tracesTime = DateUtil.Date(tracesModel.recordTime)
        layout_traces_time.setMainText(tracesTime.toFormatString())

        tv_delete.setOnClickListener {
            AccountDataManager.deleteMoneyTraces(tracesModel)
            dismiss()
        }

        tv_modify.setOnClickListener {
            ActivityUtil.turnToActivity(MoneyTracesRecordActivity::class.java, Pair(MoneyTracesRecordActivity.DATA_TRACES_ID, tracesModel.id))
            dismiss()
        }
    }
}