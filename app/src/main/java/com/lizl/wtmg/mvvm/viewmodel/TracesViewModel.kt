package com.lizl.wtmg.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.launch
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.model.MonthTracesOutlineModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

class TracesViewModel : ViewModel() {
    private val polymerizedTraces = MutableLiveData<MutableList<PolymerizeGroupModel>>()
    private val monthTracesOutlineLd = MutableLiveData<MonthTracesOutlineModel>()

    private var lastJob: Job? = null

    fun setYearAndMonth(year: Int, month: Int) {
        lastJob?.cancel()
        lastJob = launch {
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