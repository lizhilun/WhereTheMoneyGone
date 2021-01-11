package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.backspace
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.databinding.ActivityMoneyRecordBinding
import com.lizl.wtmg.mvvm.base.BaseActivity
import kotlinx.android.synthetic.main.activity_money_record.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MoneyRecordActivity : BaseActivity<ActivityMoneyRecordBinding>(R.layout.activity_money_record)
{
    private var inputTemp = ""

    override fun initView()
    {
        clearInput()
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
                saveInput()
                clearInput()
            }
        }

        tv_save.setOnClickListener(true) {
            GlobalScope.launch {
                saveInput()
                GlobalScope.launch(Dispatchers.Main) { onBackPressed() }
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

    private fun saveInput()
    {
        cluInput()

        val amount = inputTemp.toIntOrNull() ?: 0
    }
}