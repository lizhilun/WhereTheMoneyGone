package com.lizl.wtmg.mvvm.activity

import android.view.View
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.util.TranslateUtil
import com.blankj.utilcode.util.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.backspace
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.databinding.ActivityMoneyRecordBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.ExpenditureModel
import com.lizl.wtmg.module.account.AccountDataManager
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.custom.view.selection.SingleSelectionModel
import com.lizl.wtmg.custom.view.selection.SingleSelectionView
import com.lizl.wtmg.mvvm.adapter.ViewPagerAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lizl.wtmg.util.DateUtil
import com.lizl.wtmg.util.PopupUtil
import kotlinx.android.synthetic.main.activity_money_record.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoneyRecordActivity : BaseActivity<ActivityMoneyRecordBinding>(R.layout.activity_money_record)
{
    private var inputTemp = ""

    private var accountType = ""
    private var selectTime = DateUtil.Date()
    private var expenditureType = AppConstant.EXPENDITURE_TYPE_MEALS
    private var incomeType = AppConstant.INCOME_TYPE_WAGES

    override fun initView()
    {
        clearInput()

        val expenditureTypeSelectionView = SingleSelectionView(this).apply {
            val expenditureTypeList = mutableListOf<SingleSelectionModel>()
            AccountManager.expenditureTypeList.forEach {
                expenditureTypeList.add(
                        SingleSelectionModel(AccountManager.getExpenditureTypeIcon(it), TranslateUtil.translateExpenditureType(it), it == expenditureType))
            }
            setData(expenditureTypeList)
        }

        val incomeTypeSelectionView = SingleSelectionView(this).apply {
            val expenditureTypeList = mutableListOf<SingleSelectionModel>()
            AccountManager.incomeTypeList.forEach {
                expenditureTypeList.add(SingleSelectionModel(AccountManager.getIncomeTypeIcon(it), TranslateUtil.translateIncomeType(it), it == incomeType))
            }
            setData(expenditureTypeList)
        }

        vp_type.adapter = ViewPagerAdapter(mutableListOf<View>().apply {
            add(expenditureTypeSelectionView)
            add(incomeTypeSelectionView)
        })
        vp_type.offscreenPageLimit = 3

        val titleList = listOf(getString(R.string.expenditure), getString(R.string.income))
        TabLayoutMediator(tl_title, vp_type, TabLayoutMediator.TabConfigurationStrategy { tab, position ->
            if (position >= titleList.size) return@TabConfigurationStrategy
            tab.text = titleList[position]
        }).attach()

        tv_account.text = "${getString(R.string.account)}：${TranslateUtil.translateAccountType(accountType)}"
        tv_time.text = String.format("%d-%02d-%02d %02d:%2d", selectTime.year, selectTime.month, selectTime.day, selectTime.hour, selectTime.minute)
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
            PopupUtil.showBottomListPopup(mutableListOf<BottomModel>().apply {
                AppDatabase.getInstance().getAccountDao().queryAllAccount().forEach {
                    add(BottomModel(AccountManager.getAccountIcon(it.type), TranslateUtil.translateAccountType(it.type), it.type))
                }
                add(BottomModel(R.drawable.ic_baseline_add_colourful_24, getString(R.string.add), "A"))
            }) {
                if (it.tag == "A")
                {
                    ActivityUtils.startActivity(AddAccountActivity::class.java)
                    return@showBottomListPopup
                }
                accountType = it.tag as String
                tv_account.text = "${getString(R.string.account)}：${TranslateUtil.translateAccountType(accountType)}"
            }
        }

        tv_time.setOnClickListener(true) {
            PopupUtil.showDatePickerDialog(selectTime.year, selectTime.month - 1, selectTime.day) { _, year, month, dayOfMonth ->
                run {
                    PopupUtil.showTimePickerDialog(selectTime.hour, selectTime.minute) { _, hourOfDay, minute ->
                        run {
                            selectTime.year = year
                            selectTime.month = month + 1
                            selectTime.day = dayOfMonth
                            selectTime.hour = hourOfDay
                            selectTime.minute = minute
                            tv_time.text = String.format("%d-%02d-%02d %02d:%2d", selectTime.year, selectTime.month, selectTime.day, selectTime.hour,
                                    selectTime.minute)
                        }
                    }
                }
            }
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

    private fun saveExpenditure(amount: Int)
    {
        val expenditureModel =
                ExpenditureModel(amonunt = amount.toFloat(), expenditureType = expenditureType, accountType = accountType, recordTime = selectTime.time,
                        recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day)

        AccountDataManager.addExpenditure(expenditureModel)
    }

    private fun saveIncome(amount: Int)
    {
        val expenditureModel =
                ExpenditureModel(amonunt = amount.toFloat(), expenditureType = expenditureType, accountType = accountType, recordTime = selectTime.time,
                        recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day)

        AccountDataManager.addExpenditure(expenditureModel)
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

        if (accountType.isBlank())
        {
            ToastUtils.showShort(R.string.please_select_account)
            return false
        }

        if (vp_type.currentItem == 0)
        {
            saveExpenditure(amount)
        }
        else
        {
            saveIncome(amount)
        }

        return true
    }
}