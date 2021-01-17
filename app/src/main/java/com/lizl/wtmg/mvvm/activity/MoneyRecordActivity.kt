package com.lizl.wtmg.mvvm.activity

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.util.TranslateUtil
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.backspace
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.update
import com.lizl.wtmg.databinding.ActivityMoneyRecordBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.ExpenditureModel
import com.lizl.wtmg.module.account.AccountDataManager
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.adapter.ExpenditureTypeAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lizl.wtmg.mvvm.model.ExpenditureTypeModel
import com.lizl.wtmg.util.DateUtil
import com.lizl.wtmg.util.PopupUtil
import kotlinx.android.synthetic.main.activity_money_record.*
import kotlinx.android.synthetic.main.activity_money_record.ctb_title
import kotlinx.android.synthetic.main.activity_money_record.tv_save
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoneyRecordActivity : BaseActivity<ActivityMoneyRecordBinding>(R.layout.activity_money_record)
{
    private var inputTemp = ""

    private var accountType = ""
    private var selectTime = DateUtil.Date()
    private var expenditureType = AppConstant.EXPENDITURE_TYPE_MEALS

    private lateinit var expenditureTypeAdapter: ExpenditureTypeAdapter

    override fun initView()
    {
        clearInput()

        expenditureTypeAdapter = ExpenditureTypeAdapter()
        rv_expenditure_type.adapter = expenditureTypeAdapter

        val expenditureTypeList = mutableListOf<ExpenditureTypeModel>()
        AccountManager.getExpenditureTypeList().forEach {
            expenditureTypeList.add(ExpenditureTypeModel(it, it == expenditureType))
        }
        expenditureTypeAdapter.setNewData(expenditureTypeList)

        tv_account.text = "${getString(R.string.account)}：${TranslateUtil.translateAccountType(accountType)}"
        tv_time.text = String.format("%d-%02d-%02d %02d:%2d", selectTime.year, selectTime.month, selectTime.day, selectTime.hour, selectTime.minute)
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }

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
                AppDatabase.getInstance().getPropertyAccountDao().queryAllAccount().forEach {
                    add(BottomModel(AccountManager.getAccountIcon(it.type), TranslateUtil.translateAccountType(it.type), it.type))
                }
                AppDatabase.getInstance().getCreditAccountDao().queryAllAccount().forEach {
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
                            tv_time.text =
                                String.format("%d-%02d-%02d %02d:%2d", selectTime.year, selectTime.month, selectTime.day, selectTime.hour, selectTime.minute)
                        }
                    }
                }
            }
        }

        expenditureTypeAdapter.setOnItemClickListener(true) { expenditureTypeModel ->
            if (expenditureTypeModel.isSelected) return@setOnItemClickListener
            expenditureTypeAdapter.data.forEach {
                if (it.isSelected)
                {
                    it.isSelected = false
                    expenditureTypeAdapter.update(it)
                }
            }
            expenditureTypeModel.isSelected = true
            expenditureTypeAdapter.update(expenditureTypeModel)
            expenditureType = expenditureTypeModel.type
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
            "D"      -> inputTemp = inputTemp.backspace()
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

        if (accountType.isBlank())
        {
            ToastUtils.showShort(R.string.please_select_account)
            return false
        }

        val expenditureModel = ExpenditureModel(amonunt = amount.toFloat(), expenditureType = expenditureType, accountType = accountType,
                recordTime = selectTime.time, recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day)

        AccountDataManager.addExpenditure(expenditureModel)


        return true
    }
}