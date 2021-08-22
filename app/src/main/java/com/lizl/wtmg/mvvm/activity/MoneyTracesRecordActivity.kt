package com.lizl.wtmg.mvvm.activity

import android.view.View
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.*
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.custom.view.selection.SingleSelectionModel
import com.lizl.wtmg.custom.view.selection.SingleSelectionView
import com.lizl.wtmg.custom.view.tracesrecord.AccountTransferView
import com.lizl.wtmg.custom.view.tracesrecord.DebtInputView
import com.lizl.wtmg.databinding.ActivityMoneyRecordTracesBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.module.account.TracesManager
import com.lizl.wtmg.mvvm.adapter.ViewPagerAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.DateModel

class MoneyTracesRecordActivity : BaseActivity<ActivityMoneyRecordTracesBinding>(R.layout.activity_money_record_traces)
{
    private var accountType = ""
    private var selectTime = DateModel()
    private var expenditureType = AppConstant.EXPENDITURE_TYPE_MEALS
    private var incomeType = AppConstant.INCOME_TYPE_WAGES
    private var transferCharge = 0.0

    private var curPageType = PAGE_TYPE_EXPENDITURE

    private var oriTracesModel: MoneyTracesModel? = null

    private val accountTransferView by lazy { AccountTransferView(this) }

    private val borrowMoneyView by lazy { DebtInputView(this) }

    companion object
    {
        const val DATA_TRACES_ID = "DATA_TRACES_ID"

        private const val PAGE_TYPE_EXPENDITURE = 0
        private const val PAGE_TYPE_INCOME = 1
        private const val PAGE_TYPE_TRANSFER = 2
        private const val PAGE_TYPE_DEBT = 3
    }

    override fun initView()
    {
        val tracesId = intent?.extras?.getLong(DATA_TRACES_ID, -1L) ?: -1L
        if (tracesId >= 0)
        {
            oriTracesModel = AppDatabase.getInstance().getMoneyTracesDao().queryTracesById(tracesId)
        }

        oriTracesModel?.let {
            accountType = it.accountType
            selectTime = DateModel(it.recordTime)
            dataBinding.etRemarks.setText(it.remarks)
            dataBinding.viewNumberInput.setInputNumber(it.amount)
            when (it.tracesCategory)
            {
                AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE ->
                {
                    expenditureType = it.tracesType
                    curPageType = PAGE_TYPE_EXPENDITURE
                }
                AppConstant.MONEY_TRACES_CATEGORY_INCOME      ->
                {
                    incomeType = it.tracesType
                    curPageType = PAGE_TYPE_INCOME
                }
                AppConstant.MONEY_TRACES_CATEGORY_TRANSFER    ->
                {
                    dataBinding.tvAccount.isVisible = false
                    dataBinding.tvTransferCharge.isVisible = true

                    curPageType = PAGE_TYPE_TRANSFER
                    accountTransferView.setOutAccountType(it.accountType)
                    accountTransferView.setInAccountType(it.transferToAccount)
                }
                AppConstant.MONEY_TRACES_CATEGORY_DEBT        ->
                {
                    dataBinding.tvAccount.isVisible = false
                    curPageType = PAGE_TYPE_DEBT

                    borrowMoneyView.setDebtType(it.tracesType)
                    borrowMoneyView.setInAccountType(it.transferToAccount)
                    borrowMoneyView.setOutAccountType(it.accountType)
                }
                else                                          -> curPageType = PAGE_TYPE_EXPENDITURE
            }
        }

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

        dataBinding.vpType.adapter = ViewPagerAdapter(mutableListOf<View>().apply {
            add(expenditureTypeSelectionView)
            add(incomeTypeSelectionView)
            add(accountTransferView)
            add(borrowMoneyView)
        })
        dataBinding.vpType.offscreenPageLimit = 4
        dataBinding.vpType.setCurrentItem(curPageType, false)

        val titleList =
                listOf(getString(R.string.expenditure), getString(R.string.income), getString(R.string.transfer), getString(R.string.borrow_and_payback))
        TabLayoutMediator(dataBinding.tlTitle, dataBinding.vpType, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            if (position >= titleList.size) return@TabConfigurationStrategy
            tab.text = titleList[position]
        }).attach()
    }

    override fun initData()
    {
        dataBinding.tvAccount.text = "${getString(R.string.account)}：${if (accountType.isEmpty()) "" else accountType.translate()}"
        dataBinding.tvTime.text = selectTime.toFormatString()
        dataBinding.tvTransferCharge.text = "${getString(R.string.brokerage)}：${transferCharge.toAmountStr()}"
        dataBinding.tvInputAmount.text = dataBinding.viewNumberInput.getInputNumber().toAmountStr()
    }

    override fun initListener()
    {
        dataBinding.ivBack.setOnClickListener { onBackPressed() }

        dataBinding.viewNumberInput.setOnTextChangedListener {
            dataBinding.etRemarks.clearFocus()
            dataBinding.tvInputAmount.text = if (it.isBlank()) "0.0" else it
        }

        dataBinding.viewNumberInput.setOnMainActionClickListener {
            launch {
                if (saveInput())
                {
                    ui { onBackPressed() }
                }
            }
        }

        dataBinding.viewNumberInput.setOnSubActionClickListener {
            launch {
                if (saveInput())
                {
                    dataBinding.viewNumberInput.clearInput()
                }
            }
        }

        dataBinding.tvAccount.setOnClickListener(true) {
            PopupUtil.showBottomAccountList {
                accountType = it.type
                dataBinding.tvAccount.text = "${getString(R.string.account)}：${accountType.translate()}"
            }
        }

        dataBinding.tvTime.setOnClickListener(true) {
            PopupUtil.showDataAndTimePickerDialog(selectTime) {
                selectTime = it
                dataBinding.tvTime.text = selectTime.toFormatString()
            }
        }

        dataBinding.vpType.registerOnPageChangeCallback {
            curPageType = it
            dataBinding.tvAccount.isVisible = it != PAGE_TYPE_TRANSFER && it != PAGE_TYPE_DEBT
            dataBinding.tvTransferCharge.isVisible = it == PAGE_TYPE_TRANSFER
            dataBinding.tvTransferChargeMode.isVisible = false
            dataBinding.etRemarks.isVisible = true
        }

        dataBinding.tvTransferCharge.setOnClickListener(true) {
            dataBinding.etRemarks.isVisible = false
            dataBinding.tvTransferChargeMode.isVisible = true
            dataBinding.viewNumberInput.clearInput()
        }
    }

    private fun saveInput(): Boolean
    {
        val amount = dataBinding.viewNumberInput.getInputNumber()

        if (dataBinding.tvTransferChargeMode.isVisible)
        {
            transferCharge = amount
            ui {
                dataBinding.tvTransferCharge.text = "${getString(R.string.brokerage)}：${transferCharge.toAmountStr()}"
                dataBinding.tvTransferChargeMode.isVisible = false
                dataBinding.etRemarks.isVisible = true
                dataBinding.viewNumberInput.clearInput()
            }
            return false
        }

        if (amount == 0.0)
        {
            ToastUtils.showShort(R.string.please_input_amount)
            return false
        }

        if (dataBinding.tvAccount.isVisible && accountType.isBlank())
        {
            ToastUtils.showShort(R.string.please_select_account)
            return false
        }

        if (curPageType == PAGE_TYPE_TRANSFER && !accountTransferView.checkSelect())
        {
            return false
        }

        if (curPageType == PAGE_TYPE_DEBT && !borrowMoneyView.checkInput())
        {
            return false
        }

        oriTracesModel?.let { TracesManager.deleteMoneyTraces(it) }

        val traceCategory = when (curPageType)
        {
            PAGE_TYPE_TRANSFER -> AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
            PAGE_TYPE_INCOME   -> AppConstant.MONEY_TRACES_CATEGORY_INCOME
            PAGE_TYPE_DEBT     -> AppConstant.MONEY_TRACES_CATEGORY_DEBT
            else               -> AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE
        }

        val moneyTracesModel = when (curPageType)
        {
            PAGE_TYPE_EXPENDITURE -> MoneyTracesModel(amount = amount,
                                                      tracesType = expenditureType,
                                                      tracesCategory = traceCategory,
                                                      accountType = accountType,
                                                      recordTime = selectTime.getTimeInMills(),
                                                      recordYear = selectTime.getYear(),
                                                      recordMonth = selectTime.getMonth(),
                                                      recordDay = selectTime.getDay(),
                                                      remarks = dataBinding.etRemarks.text.toString())

            PAGE_TYPE_INCOME      -> MoneyTracesModel(amount = amount,
                                                      tracesType = incomeType,
                                                      tracesCategory = traceCategory,
                                                      accountType = accountType,
                                                      recordTime = selectTime.getTimeInMills(),
                                                      recordYear = selectTime.getYear(),
                                                      recordMonth = selectTime.getMonth(),
                                                      recordDay = selectTime.getDay(),
                                                      remarks = dataBinding.etRemarks.text.toString())
            PAGE_TYPE_TRANSFER    ->
            {
                if (transferCharge > 0)
                {
                    val chargeModel =
                            MoneyTracesModel(amount = transferCharge,
                                             tracesType = AppConstant.EXPENDITURE_TYPE_BROKERAGE,
                                             tracesCategory = AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE,
                                             accountType = accountTransferView.getOutAccountType(),
                                             recordTime = selectTime.getTimeInMills() + 1,
                                             recordYear = selectTime.getYear(),
                                             recordMonth = selectTime.getMonth(),
                                             recordDay = selectTime.getDay())
                    TracesManager.addMoneyTraces(chargeModel)
                }

                MoneyTracesModel(amount = amount - transferCharge,
                                 tracesType = AppConstant.TRANSFER_TYPE_TRANSFER,
                                 tracesCategory = traceCategory,
                                 accountType = accountTransferView.getOutAccountType(),
                                 recordTime = selectTime.getTimeInMills(),
                                 recordYear = selectTime.getYear(),
                                 recordMonth = selectTime.getMonth(),
                                 recordDay = selectTime.getDay(),
                                 remarks = dataBinding.etRemarks.text.toString(),
                                 transferToAccount = accountTransferView.getInAccountType())
            }
            PAGE_TYPE_DEBT        ->
            {
                val borrowInfoModel = borrowMoneyView.getDebtInfo()

                listOf(borrowInfoModel.inAccountType, borrowInfoModel.outAccountType).forEach {
                    if (AppDatabase.getInstance().getAccountDao().queryAccountByType(it) == null)
                    {
                        val accountModel = AccountModel(category = AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT, type = it, name = it, showInTotal = false)
                        AppDatabase.getInstance().getAccountDao().insert(accountModel)
                    }
                }

                MoneyTracesModel(amount = amount,
                                 tracesType = borrowInfoModel.debtType,
                                 tracesCategory = traceCategory,
                                 accountType = borrowInfoModel.outAccountType,
                                 recordTime = selectTime.getTimeInMills(),
                                 recordYear = selectTime.getYear(),
                                 recordMonth = selectTime.getMonth(),
                                 recordDay = selectTime.getDay(),
                                 remarks = dataBinding.etRemarks.text.toString(),
                                 transferToAccount = borrowInfoModel.inAccountType)
            }
            else                  -> return false
        }

        TracesManager.addMoneyTraces(moneyTracesModel)

        return true
    }
}