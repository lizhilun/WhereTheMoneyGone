package com.lizl.wtmg.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.model.MonthTracesOutlineModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class TracesViewModel : ViewModel()
{
    private val polymerizedTraces = MutableLiveData<MutableList<PolymerizeGroupModel>>()
    private val monthTracesOutlineLd = MutableLiveData<MonthTracesOutlineModel>()

    private var lastJob: Job? = null

    fun setYearAndMonth(year: Int, month: Int)
    {
        lastJob?.cancel()
        lastJob = viewModelScope.launch {
            AppDatabase.getInstance()
                .getMoneyTracesDao()
                .obTracesByMonth(year, month)
                .distinctUntilChanged()
                .flowOn(Dispatchers.IO)
                .collectLatest { tracesList ->
                    val monthTracesOutlineModel = MonthTracesOutlineModel()
                    monthTracesOutlineModel.monthExpenditure = tracesList.filter {
                        it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE && it.tracesCategory != AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
                    }.sumOf { it.amount }
                    monthTracesOutlineModel.monthIncome = tracesList.filter {
                        it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME && it.tracesCategory != AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
                    }.sumOf { it.amount }
                    monthTracesOutlineLd.postValue(monthTracesOutlineModel)

                    polymerizedTraces.postValue(AccountManager.polymerizeTrancesList(tracesList))
                }
        }
    }

    fun obPolymerizedTraces(): LiveData<MutableList<PolymerizeGroupModel>> = polymerizedTraces

    fun obMonthTracesOutline(): LiveData<MonthTracesOutlineModel> = monthTracesOutlineLd
}