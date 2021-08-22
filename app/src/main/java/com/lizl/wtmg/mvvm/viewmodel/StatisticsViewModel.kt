package com.lizl.wtmg.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.launch
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.model.statistics.QuantityModel
import com.lizl.wtmg.mvvm.model.statistics.StatisticsOutlineModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class StatisticsViewModel : ViewModel()
{
    private val channel = Channel<ConditionModel>()
    private val expenditureStatisticsLd = MutableLiveData<ArrayList<QuantityModel>>()
    private val incomeStatisticsLd = MutableLiveData<ArrayList<QuantityModel>>()
    private val financialTransactionsStatisticsLd = MutableLiveData<ArrayList<QuantityModel>>()
    private val statisticsOutlineLd = MutableLiveData<StatisticsOutlineModel>()

    init
    {
        launch {
            channel.receiveAsFlow().flowOn(Dispatchers.IO).debounce(200).distinctUntilChanged().collectLatest { condition ->
                val traceList = if (condition.month == 0)
                {
                    AppDatabase.getInstance().getMoneyTracesDao().queryTracesByYear(condition.year)
                }
                else
                {
                    AppDatabase.getInstance().getMoneyTracesDao().queryTracesByMonth(condition.year, condition.month)
                }

                val statisticsOutlineModel = StatisticsOutlineModel()
                statisticsOutlineModel.expenditure = traceList.filter { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE }.sumOf { it.amount }
                statisticsOutlineModel.income = traceList.filter { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME }.sumOf { it.amount }
                statisticsOutlineLd.postValue(statisticsOutlineModel)

                expenditureStatisticsLd.postValue(tracesToQuantities(traceList,
                                                                     { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE },
                                                                     { it.tracesType }))
                incomeStatisticsLd.postValue(tracesToQuantities(traceList,
                                                                { it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME },
                                                                { it.tracesType }))
                financialTransactionsStatisticsLd.postValue(tracesToQuantities(traceList,
                                                                               { it.tracesCategory == AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS },
                                                                               { it.accountType }))
            }
        }
    }

    fun setYearAndMonth(year: Int, month: Int)
    {
        channel.trySend(ConditionModel(year, month))
    }

    fun obExpenditureStatistics(): LiveData<ArrayList<QuantityModel>> = expenditureStatisticsLd

    fun obIncomeStatistics(): LiveData<ArrayList<QuantityModel>> = incomeStatisticsLd

    fun obFinancialTransactionsStatistics(): LiveData<ArrayList<QuantityModel>> = financialTransactionsStatisticsLd

    fun obStatisticsOutline(): LiveData<StatisticsOutlineModel> = statisticsOutlineLd

    private fun tracesToQuantities(traceList: MutableList<MoneyTracesModel>,
                                   filterCondition: (MoneyTracesModel) -> Boolean,
                                   groupCondition: (MoneyTracesModel) -> String): ArrayList<QuantityModel>
    {
        return ArrayList(mutableListOf<QuantityModel>().apply {
            traceList.filter { filterCondition.invoke(it) }
                .groupBy { groupCondition.invoke(it) }
                .forEach { (t, u) -> add(QuantityModel(t, u.sumOf { it.amount })) }
            sortByDescending { it.quantity }
        })
    }

    class ConditionModel(val year: Int, val month: Int)
}