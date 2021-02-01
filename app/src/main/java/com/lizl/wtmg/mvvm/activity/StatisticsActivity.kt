package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.databinding.ActivityStatisticsBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.adapter.QuantityListAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.statistics.QuantityModel
import com.lizl.wtmg.util.DateUtil.Date
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StatisticsActivity : BaseActivity<ActivityStatisticsBinding>(R.layout.activity_statistics)
{
    private val expenditureTypeAdapter = QuantityListAdapter()
    private val financialTransactionsTypeAdapter = QuantityListAdapter()

    override fun initView()
    {
        rv_expenditure_statistics.adapter = expenditureTypeAdapter
        rv_financial_transactions_statistics.adapter = financialTransactionsTypeAdapter

        val curDate = Date()
        tv_statistics_month.text = String.format("%d.%02d", curDate.year, curDate.month)
        showStatistics(curDate.year, curDate.month)
    }

    override fun initListener()
    {
        iv_back.setOnClickListener { onBackPressed() }

        tv_statistics_month.setOnClickListener {
            PopupUtil.showMonthSelectPopup { year, month ->
                tv_statistics_month.text = String.format("%d.%02d", year, month)
                showStatistics(year, month)
            }
        }
    }

    private fun showStatistics(year: Int, month: Int)
    {
        GlobalScope.launch {
            val traceList = AppDatabase.getInstance().getMoneyTracesDao().queryTracesByMonth(year, month)
            showExpenditureTypeStatistics(traceList)
            showFinancialTransactions(traceList)
        }
    }

    private fun showExpenditureTypeStatistics(traceList: MutableList<MoneyTracesModel>)
    {
        val expenditureList = traceList.filter { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE }
        val expenditureTypeMap = expenditureList.groupBy { it.tracesType }

        val expenditureQuantityList = mutableListOf<QuantityModel>()
        expenditureTypeMap.forEach { (t, u) ->
            expenditureQuantityList.add(QuantityModel(t, u.sumByDouble { it.amonunt }))
        }
        expenditureQuantityList.sortByDescending { it.quantity }
        GlobalScope.ui { expenditureTypeAdapter.setData(expenditureQuantityList) }
    }

    private fun showFinancialTransactions(traceList: MutableList<MoneyTracesModel>)
    {
        val financialTransactionsList = traceList.filter { it.tracesType == AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS }
        val financialTransactionsTypeMap = financialTransactionsList.groupBy { it.accountType }

        val financialTransactionsQuantityList = mutableListOf<QuantityModel>()
        financialTransactionsTypeMap.forEach { (t, u) ->
            financialTransactionsQuantityList.add(QuantityModel(t, u.sumByDouble { it.amonunt }))
        }
        financialTransactionsQuantityList.sortByDescending { it.quantity }
        GlobalScope.ui { financialTransactionsTypeAdapter.setData(financialTransactionsQuantityList) }
    }
}