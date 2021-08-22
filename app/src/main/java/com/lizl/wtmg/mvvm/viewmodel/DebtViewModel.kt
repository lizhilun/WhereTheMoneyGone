package com.lizl.wtmg.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.launch
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.mvvm.activity.DebtDetailActivity
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flowOn
import kotlin.math.abs

class DebtViewModel : ViewModel()
{
    private val allDebtsPolymerizeLd = MutableLiveData<MutableList<PolymerizeGroupModel>>()
    private val totalDebeLd = MutableLiveData<Double>()

    fun setDebtType(debtType: Int)
    {
        launch {
            AppDatabase.getInstance().getAccountDao().obAllAccount().flowOn(Dispatchers.IO).collectLatest { allAccountList ->
                val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()

                val debtAccountList = allAccountList.filter {
                    it.category == AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT && ((debtType == DebtDetailActivity.DEBT_TYPE_TOTAL_BORROW_IN && it.amount < 0) || (debtType == DebtDetailActivity.DEBT_TYPE_TOTAL_BORROW_OUT && it.amount > 0))
                }

                totalDebeLd.postValue(abs(debtAccountList.sumOf { it.amount }))

                debtAccountList.forEach { debtAccount ->
                    val polymerizeChildList = mutableListOf<PolymerizeChildModel>()
                    AppDatabase.getInstance().getMoneyTracesDao().queryTracesByAccount(debtAccount.type).forEach { tracesModel ->
                        polymerizeChildList.add(PolymerizeChildModel(tracesModel.tracesCategory.getIcon(),
                                                                     tracesModel.tracesType.translate(),
                                                                     tracesModel.amount.toAmountStr(),
                                                                     tracesModel))
                    }
                    polymerizeGroupList.add(PolymerizeGroupModel(debtAccount.name, abs(debtAccount.amount).toAmountStr(), polymerizeChildList))
                }
            }
        }
    }

    fun obPolymerizeDebts(): LiveData<MutableList<PolymerizeGroupModel>> = allDebtsPolymerizeLd

    fun obTotalDebt(): LiveData<Double> = totalDebeLd
}