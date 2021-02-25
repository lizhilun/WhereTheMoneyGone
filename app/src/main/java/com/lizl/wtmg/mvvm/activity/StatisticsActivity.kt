package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.databinding.ActivityStatisticsBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.statistics.QuantityModel
import com.lizl.wtmg.util.DateUtil.Date
import kotlinx.android.synthetic.main.activity_statistics.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class StatisticsActivity : BaseActivity<ActivityStatisticsBinding>(R.layout.activity_statistics)
{

    override fun initView()
    {
        val curDate = Date()
        tv_statistics_month.text = "%d.%02d".format(curDate.year, curDate.month)
        showStatistics(curDate.year, curDate.month)
    }

    override fun initListener()
    {
        iv_back.setOnClickListener { onBackPressed() }

        tv_statistics_month.setOnClickListener {
            PopupUtil.showMonthSelectPopup(true) { year, month ->
                if (month == 0)
                {
                    tv_statistics_month.text = "${year}${getString(R.string.whole_year)}"
                }
                else
                {
                    tv_statistics_month.text = "%d.%02d".format(year, month)
                }
                showStatistics(year, month)
            }
        }
    }

    private fun showStatistics(year: Int, month: Int)
    {
        GlobalScope.launch {
            val traceList = if (month == 0)
            {
                AppDatabase.getInstance().getMoneyTracesDao().queryTracesByYear(year)
            }
            else
            {
                AppDatabase.getInstance().getMoneyTracesDao().queryTracesByMonth(year, month)
            }

            dataBinding.expenditure = traceList.filter { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE }.sumByDouble { it.amount }
            dataBinding.income = traceList.filter { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME }.sumByDouble { it.amount }

            dataBinding.expenditureStatistics =
                    tracesToQuantities(traceList, { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE }, { it.tracesType })
            dataBinding.incomeStatistics = tracesToQuantities(traceList, { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME }, { it.tracesType })
            dataBinding.financialTransactionsStatistics =
                    tracesToQuantities(traceList, { it.tracesType == AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS }, { it.accountType })
        }
    }

    private fun tracesToQuantities(traceList: MutableList<MoneyTracesModel>, filterCondition: (MoneyTracesModel) -> Boolean,
                                   groupCondition: (MoneyTracesModel) -> String): ArrayList<QuantityModel>
    {
        return ArrayList(mutableListOf<QuantityModel>().apply {
            traceList.filter { filterCondition.invoke(it) }.groupBy { groupCondition.invoke(it) }
                .forEach { (t, u) -> add(QuantityModel(t, u.sumByDouble { it.amount })) }
            sortByDescending { it.quantity }
        })
    }
}