package com.lizl.wtmg.mvvm.activity

import android.view.View
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.*
import com.lizl.wtmg.custom.view.tracesrecord.AccountTransferView
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
import com.lizl.wtmg.custom.view.tracesrecord.BorrowMoneyView
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import kotlinx.android.synthetic.main.activity_money_record_traces.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoneyTracesRecordActivity : BaseActivity<ActivityMoneyRecordTracesBinding>(R.layout.activity_money_record_traces)
{
    private var accountType = ""
    private var selectTime = DateUtil.Date()
    private var expenditureType = AppConstant.EXPENDITURE_TYPE_BUY_FOOD
    private var incomeType = AppConstant.INCOME_TYPE_WAGES
    private var transferCharge = 0.0

    private var curPageType = PAGE_TYPE_EXPENDITURE

    private var oriTracesModel: MoneyTracesModel? = null

    private val accountTransferView by lazy { AccountTransferView(this) }

    private val borrowMoneyView by lazy { BorrowMoneyView(this) }

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
            selectTime = DateUtil.Date(it.recordTime)
            et_remarks.setText(it.remarks)
            view_number_input.setInputNumber(it.amonunt)
            when (it.tracesCategory)
            {
                AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE ->
                {
                    expenditureType = it.tracesType
                    curPageType = PAGE_TYPE_EXPENDITURE
                }
                AppConstant.MONEY_TRACES_CATEGORY_INCOME ->
                {
                    incomeType = it.tracesType
                    curPageType = PAGE_TYPE_INCOME
                }
                AppConstant.MONEY_TRACES_CATEGORY_TRANSFER ->
                {
                    tv_account.isVisible = false
                    tv_transfer_charge.isVisible = true

                    curPageType = PAGE_TYPE_TRANSFER
                    accountTransferView.setOutAccountType(it.accountType)
                    accountTransferView.setInAccountType(it.transferToAccount)
                }
                AppConstant.MONEY_TRACES_CATEGORY_BORROW ->
                {
                    tv_account.isVisible = false
                    curPageType = PAGE_TYPE_DEBT

                    borrowMoneyView.setBorrowType(it.tracesType)
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

        vp_type.adapter = ViewPagerAdapter(mutableListOf<View>().apply {
            add(expenditureTypeSelectionView)
            add(incomeTypeSelectionView)
            add(accountTransferView)
            add(borrowMoneyView)
        })
        vp_type.offscreenPageLimit = 3
        vp_type.setCurrentItem(curPageType, false)

        val titleList =
                listOf(getString(R.string.expenditure), getString(R.string.income), getString(R.string.transfer), getString(R.string.borrow_and_payback))
        TabLayoutMediator(tl_title, vp_type, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            if (position >= titleList.size) return@TabConfigurationStrategy
            tab.text = titleList[position]
        }).attach()
    }

    override fun initData()
    {
        tv_account.text = "${getString(R.string.account)}：${if (accountType.isEmpty()) "" else accountType.translate()}"
        tv_time.text = selectTime.toFormatString()
        tv_transfer_charge.text = "${getString(R.string.brokerage)}：${transferCharge.toAmountStr()}"
        tv_input_amount.text = view_number_input.getInputNumber().toAmountStr()
    }

    override fun initListener()
    {
        iv_back.setOnClickListener { onBackPressed() }

        view_number_input.setOnTextChangedListener {
            et_remarks.clearFocus()
            tv_input_amount.text = if (it.isBlank()) "0.0" else it
        }

        view_number_input.setOnMainActionClickListener {
            GlobalScope.launch {
                if (saveInput())
                {
                    GlobalScope.launch(Dispatchers.Main) { onBackPressed() }
                }
            }
        }

        view_number_input.setOnSubActionClickListener {
            GlobalScope.launch {
                if (saveInput())
                {
                    view_number_input.clearInput()
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
            PopupUtil.showDataAndTimePickerDialog {
                selectTime = it
                tv_time.text = selectTime.toFormatString()
            }
        }

        vp_type.registerOnPageChangeCallback {
            curPageType = it
            tv_account.isVisible = it != PAGE_TYPE_TRANSFER && it != PAGE_TYPE_DEBT
            tv_transfer_charge.isVisible = it == PAGE_TYPE_TRANSFER
            tv_transfer_charge_mode.isVisible = false
            et_remarks.isVisible = true
        }

        tv_transfer_charge.setOnClickListener(true) {
            et_remarks.isVisible = false
            tv_transfer_charge_mode.isVisible = true
            view_number_input.clearInput()
        }
    }

    private fun saveInput(): Boolean
    {
        val amount = view_number_input.getInputNumber()

        if (tv_transfer_charge_mode.isVisible)
        {
            transferCharge = amount
            GlobalScope.ui {
                tv_transfer_charge.text = "${getString(R.string.brokerage)}：${transferCharge.toAmountStr()}"
                tv_transfer_charge_mode.isVisible = false
                et_remarks.isVisible = true
                view_number_input.clearInput()
            }
            return false
        }

        if (amount == 0.0)
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

        if (curPageType == PAGE_TYPE_DEBT && !borrowMoneyView.checkInput())
        {
            return false
        }

        oriTracesModel?.let { AccountDataManager.deleteMoneyTraces(it) }

        val traceCategory = when (curPageType)
        {
            PAGE_TYPE_TRANSFER -> AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
            PAGE_TYPE_INCOME -> AppConstant.MONEY_TRACES_CATEGORY_INCOME
            PAGE_TYPE_DEBT -> AppConstant.MONEY_TRACES_CATEGORY_BORROW
            else               -> AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE
        }

        val moneyTracesModel = when (curPageType)
        {
            PAGE_TYPE_EXPENDITURE -> MoneyTracesModel(amonunt = amount, tracesType = expenditureType, tracesCategory = traceCategory, accountType = accountType,
                    recordTime = selectTime.timeInMills, recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day,
                    remarks = et_remarks.text.toString())

            PAGE_TYPE_INCOME -> MoneyTracesModel(amonunt = amount, tracesType = incomeType, tracesCategory = traceCategory, accountType = accountType,
                    recordTime = selectTime.timeInMills, recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day,
                    remarks = et_remarks.text.toString())
            PAGE_TYPE_TRANSFER ->
            {
                if (transferCharge > 0)
                {
                    val chargeModel = MoneyTracesModel(amonunt = transferCharge, tracesType = AppConstant.EXPENDITURE_TYPE_BROKERAGE,
                            tracesCategory = AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE, accountType = accountTransferView.getOutAccountType(),
                            recordTime = selectTime.timeInMills + 1, recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day)
                    AccountDataManager.addMoneyTraces(chargeModel)
                }

                MoneyTracesModel(amonunt = amount - transferCharge, tracesType = AppConstant.TRANSFER_TYPE_TRANSFER, tracesCategory = traceCategory,
                        accountType = accountTransferView.getOutAccountType(), recordTime = selectTime.timeInMills, recordYear = selectTime.year,
                        recordMonth = selectTime.month, recordDay = selectTime.day, remarks = et_remarks.text.toString(),
                        transferToAccount = accountTransferView.getInAccountType())
            }
            PAGE_TYPE_DEBT ->
            {
                val borrowInfoModel = borrowMoneyView.getBorrowInfo()

                listOf(borrowInfoModel.inAccountType, borrowInfoModel.outAccountType).forEach {
                    if (AppDatabase.getInstance().getAccountDao().queryAccountByType(it) == null)
                    {
                        val accountModel = AccountModel(category = AppConstant.MONEY_TRACES_CATEGORY_BORROW, type = it, name = it, showInTotal = false)
                        AppDatabase.getInstance().getAccountDao().insert(accountModel)
                    }
                }

                MoneyTracesModel(amonunt = amount, tracesType = borrowInfoModel.borrowType, tracesCategory = traceCategory,
                        accountType = borrowInfoModel.outAccountType, recordTime = selectTime.timeInMills, recordYear = selectTime.year,
                        recordMonth = selectTime.month, recordDay = selectTime.day, remarks = et_remarks.text.toString(),
                        transferToAccount = borrowInfoModel.inAccountType)
            }
            else                  -> return false
        }

        AccountDataManager.addMoneyTraces(moneyTracesModel)

        return true
    }
}