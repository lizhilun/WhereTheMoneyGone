package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.util.TranslateUtil
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.backspace
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.databinding.ActivityMoneyRecordBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.ExpenditureModel
import com.lizl.wtmg.module.property.PropertyManager
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lizl.wtmg.util.DateUtil
import com.lizl.wtmg.util.PopupUtil
import kotlinx.android.synthetic.main.activity_money_record.*
import kotlinx.android.synthetic.main.activity_money_record.ctb_title
import kotlinx.android.synthetic.main.activity_money_record.tv_save
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MoneyRecordActivity : BaseActivity<ActivityMoneyRecordBinding>(R.layout.activity_money_record)
{
    private var inputTemp = ""

    private var accountType = AppConstant.PROPERTY_TYPE_CASH
    private var selectTime = DateUtil.Date()

    override fun initView()
    {
        clearInput()

        tv_account.text = "${getString(R.string.account)}：${TranslateUtil.translatePropertyType(accountType)}"
        tv_time.text = String.format("%d-%02d-%02d %02d:%2d", selectTime.year, selectTime.month, selectTime.day, selectTime.hour, selectTime.minute)
    }

    override fun initListener()
    {
        ctb_title.setOnClickListener { onBackPressed() }

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
                PropertyManager.getPropertyList().forEach {
                    add(BottomModel(PropertyManager.getPropertyIcon(it), TranslateUtil.translatePropertyType(it), it))
                }
            }) {
                accountType = it.tag as String
                tv_account.text = "${getString(R.string.account)}：${TranslateUtil.translatePropertyType(accountType)}"
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

    private fun saveInput(): Boolean
    {
        cluInput()

        val amount = inputTemp.toIntOrNull() ?: 0

        if (amount == 0)
        {
            ToastUtils.showShort(R.string.pleas_input_amount)
            return false
        }

        val expenditureModel = ExpenditureModel(amonunt = amount.toFloat(), expenditureType = AppConstant.EXPENDITURE_TYPE_MEALS, payType = accountType,
                recordTime = selectTime.time, recordYear = selectTime.year, recordMonth = selectTime.month, recordDay = selectTime.day)

        AppDatabase.getInstance().getExpenditureDao().insert(expenditureModel)

        return true
    }
}