package com.lizl.wtmg.mvvm.activity

import android.view.View
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.*
import com.lizl.wtmg.custom.view.AccountTransferView
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountDataManager
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.custom.view.selection.SingleSelectionModel
import com.lizl.wtmg.custom.view.selection.SingleSelectionView
import com.lizl.wtmg.databinding.ActivityMoneyRecordTracesBinding
import com.lizl.wtmg.mvvm.adapter.ViewPagerAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.util.DateUtil
import com.lizl.wtmg.custom.popup.PopupUtil
import kotlinx.android.synthetic.main.activity_money_record_traces.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoneyTracesRecordActivity : BaseActivity<ActivityMoneyRecordTracesBinding>(R.layout.activity_money_record_traces)
{
    private var inputTemp = ""

    private var accountType = ""
    private var selectTime = DateUtil.Date()
    private var expenditureType = AppConstant.EXPENDITURE_TYPE_MEALS
    private var incomeType = AppConstant.INCOME_TYPE_WAGES

    private var curPageType = PAGE_TYPE_EXPENDITURE

    private lateinit var accountTransferView: AccountTransferView

    companion object
    {
        private const val PAGE_TYPE_EXPENDITURE = 0
        private const val PAGE_TYPE_INCOME = 1
        private const val PAGE_TYPE_TRANSFER = 2
    }

    override fun initView()
    {
        clearInput()

        val expenditureTypeSelectionView = SingleSelectionView(this).apply {
            val expenditureTypeList = mutableListOf<SingleSelectionModel>()
            AccountManager.expenditureTypeList.forEach {
                expenditureTypeList.add(SingleSelectionModel(it.getIcon(), it.translate(), it == expenditureType, it))
            }
            setData(expenditureTypeList)
            setOnSelectionChangedListener { expenditureType = it.tag as String }
        }

        val incomeTypeSelectionView = SingleSelectionView(this).apply {
            val expenditureTypeList = mutableListOf<SingleSelectionModel>()
            AccountManager.incomeTypeList.forEach {
                expenditureTypeList.add(SingleSelectionModel(it.getIcon(), it.translate(), it == incomeType, it))
            }
            setData(expenditureTypeList)
            setOnSelectionChangedListener { incomeType = it.tag as String }
        }

        accountTransferView = AccountTransferView(this)

        vp_type.adapter = ViewPagerAdapter(mutableListOf<View>().apply {
            add(expenditureTypeSelectionView)
            add(incomeTypeSelectionView)
            add(accountTransferView)
        })
        vp_type.offscreenPageLimit = 3

        val titleList = listOf(getString(R.string.expenditure), getString(R.string.income), getString(R.string.transfer))
        TabLayoutMediator(tl_title, vp_type, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            if (position >= titleList.size) return@TabConfigurationStrategy
            tab.text = titleList[position]
        }).attach()

        tv_account.text = "${getString(R.string.account)}：${accountType.translate()}"
        tv_time.text = String.format("%d-%02d-%02d %02d:%02d", selectTime.year, selectTime.month, selectTime.day, selectTime.hour, selectTime.minute)
    }

    override fun initListener()
    {
        iv_back.setOnClickListener { onBackPressed() }

        view_input_key.setOnKeyClickListener { onNewKeyInput(it) }

        iv_backspace.setOnClickListener(true) {
            onNewKeyInput("D")
        }

        tv_record_one_more.setOnClickListener(true) {
            GlobalScope.launch {
                if (saveInput())
                {
                    clearInput()
                }
            }
        }

        tv_save.setOnClickListener(true) {
            GlobalScope.launch {
                if (saveInput())
                {
                    GlobalScope.launch(Dispatchers.Main) { onBackPressed() }
                }
            }
        }

        tv_account.setOnClickListener(true) {
            PopupUtil.showBottomAccountList {
                accountType = it.type
                tv_account.text = "${getString(R.string.account)}：${accountType.translate()}"
            }
        }

        tv_time.setOnClickListener(true) {
            PopupUtil.showDatePickerDialog(selectTime.year, selectTime.month - 1, selectTime.day) { _, year, month, dayOfMonth ->
                run {
                    PopupUtil.showTimePickerDialog(selectTime.hour, selectTime.minute) { _, hourOfDay, minute ->
                        run {
                            selectTime.set(year, month + 1, dayOfMonth, hourOfDay, minute, 0)
                            tv_time.text = String.format("%d-%02d-%02d %02d:%02d", selectTime.year, selectTime.month, selectTime.day, selectTime.hour,
                                    selectTime.minute)
                        }
                    }
                }
            }
        }

        vp_type.registerOnPageChangeCallback {
            curPageType = it
            tv_account.isVisible = it != PAGE_TYPE_TRANSFER
        }
    }

    private fun clearInput()
    {
        GlobalScope.launch(Dispatchers.Main) {
            inputTemp = ""
            tv_input_amount.text = "0"
        }
    }

    private fun onNewKeyInput(key: String)
    {
        when (key)
        {
            "D" -> inputTemp = inputTemp.backspace()
            "-", "+" ->
            {
                cluInput()
                inputTemp += key
            }
            else     -> inputTemp += key
        }

        tv_input_amount.text = if (inputTemp.isBlank()) "0" else inputTemp
    }

    private fun cluInput()
    {
        arrayListOf("-", "+").forEach { operator ->
            if (!inputTemp.contains(operator)) return@forEach
            val inputInfo = inputTemp.split(operator)

            var numberOne = 0
            var numberTwo = 0

            if (inputTemp.startsWith("-"))
            {
                if (inputInfo.size != 3) return@forEach
                numberOne = 1 - inputInfo[1].toInt()
                numberTwo = inputInfo[2].toInt()
            }
            else
            {
                if (inputInfo.size != 2) return@forEach
                numberOne = inputInfo[0].toInt()
                numberTwo = inputInfo[1].toInt()
            }
            when (operator)
            {
                "-" -> inputTemp = (numberOne - numberTwo).toString()
                "+" -> inputTemp = (numberOne + numberTwo).toString()
            }
        }
    }

    private fun saveInput(): Boolean
    {
        cluInput()

        val amount = inputTemp.toIntOrNull() ?: 0

        if (amount == 0)
        {
            ToastUtils.showShort(R.string.please_input_amount)
            return false
        }

        if (tv_account.isVisible && accountType.isBlank())
        {
            ToastUtils.showShort(R.string.please_select_account)
            return false
        }

        if (curPageType == PAGE_TYPE_TRANSFER && !accountTransferView.checkSelect())
        {
            return false
        }

        val traceCategory = when (curPageType)
        {
            PAGE_TYPE_TRANSFER -> AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
            PAGE_TYPE_INCOME -> AppConstant.MONEY_TRACES_CATEGORY_INCOME
            else               -> AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE
        }

        val moneyTracesModel = when (curPageType)
        {
            PAGE_TYPE_EXPENDITURE -> MoneyTracesModel(amonunt = amount.toDouble(), tracesType = expenditureType, tracesCategory = traceCategory,
                    accountType = accountType, recordTime = selectTime.timeInMills, recordYear = selectTime.year, recordMonth = selectTime.month,
                    recordDay = selectTime.day)

            PAGE_TYPE_INCOME -> MoneyTracesModel(amonunt = amount.toDouble(), tracesType = incomeType, tracesCategory = traceCategory, accountType = accountType,
                    recordTime = selectTime.timeInMills, recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day)

            else                  -> MoneyTracesModel(amonunt = amount.toDouble(), tracesType = AppConstant.TRANSFER_TYPE_TRANSFER,
                    tracesCategory = traceCategory, accountType = accountTransferView.getOutAccountType(), recordTime = selectTime.timeInMills,
                    recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day,
                    transferToAccount = accountTransferView.getInAccountType())
        }

        AccountDataManager.addMoneyTraces(moneyTracesModel)

        return true
    }
}