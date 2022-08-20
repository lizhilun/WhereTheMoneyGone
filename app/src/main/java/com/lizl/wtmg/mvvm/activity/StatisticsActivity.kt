package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.databinding.ActivityStatisticsBinding
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.DateModel
import com.lizl.wtmg.mvvm.model.statistics.QuantityModel
import com.lizl.wtmg.mvvm.viewmodel.StatisticsViewModel
import com.lizl.wtmg.util.ActivityUtil

class StatisticsActivity : BaseActivity<ActivityStatisticsBinding>(R.layout.activity_statistics) {
    private val statisticsViewModel by lazy { createViewModel(StatisticsViewModel::class.java) }

    override fun initView() {
        val curDate = DateModel()
        dataBinding.tvStatisticsMonth.text = "%d.%02d".format(curDate.getYear(), curDate.getMonth())
        showStatistics(curDate.getYear(), curDate.getMonth())
    }

    override fun initData() {
        statisticsViewModel.obExpenditureStatistics().observe(this, {
            dataBinding.expenditureStatistics = it
        })
        statisticsViewModel.obIncomeStatistics().observe(this, {
            dataBinding.incomeStatistics = it
        })
        statisticsViewModel.obFinancialTransactionsStatistics().observe(this, {
            dataBinding.financialTransactionsStatistics = it
        })
        statisticsViewModel.obStatisticsOutline().observe(this, {
            dataBinding.expenditure = it.expenditure
            dataBinding.income = it.income
        })
    }

    override fun initListener() {
        dataBinding.ivBack.setOnClickListener { onBackPressed() }

        dataBinding.tvStatisticsMonth.setOnClickListener {
            PopupUtil.showMonthSelectPopup(true) { year, month ->
                if (month == 0) {
                    dataBinding.tvStatisticsMonth.text = "${year}${getString(R.string.whole_year)}"
                } else {
                    dataBinding.tvStatisticsMonth.text = "%d.%02d".format(year, month)
                }
                showStatistics(year, month)
            }
        }
    }

    private fun showStatistics(year: Int, month: Int) {
        statisticsViewModel.setYearAndMonth(year, month)

        val onQuantityItemClickListener = { quantityModel: QuantityModel ->
            val startTime = if (month == 0) DateModel.yearStart(year) else DateModel.monthStart(year, month)
            val endTime = if (month == 0) DateModel.yearEnd(year) else DateModel.monthEnd(year, month)
            ActivityUtil.turnToActivity(TracesSearchActivity::class.java,
                    Pair(TracesSearchActivity.DATA_START_TIME, startTime.getTimeInMills()),
                    Pair(TracesSearchActivity.DATA_END_TIME, endTime.getTimeInMills()),
                    Pair(TracesSearchActivity.DATA_KEY_WORD, quantityModel.name.translate()))
        }

        dataBinding.qsvExpenditureStatistics.setOnQuantityItemClickListener(onQuantityItemClickListener)
        dataBinding.qsvIncomeStatistics.setOnQuantityItemClickListener(onQuantityItemClickListener)
    }
}