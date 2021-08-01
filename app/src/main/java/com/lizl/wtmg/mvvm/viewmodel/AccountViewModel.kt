package com.lizl.wtmg.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.mvvm.model.PropertyOutlineModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch

class AccountViewModel : ViewModel()
{
    private val allAccountsPolymerizeLd = MutableLiveData<MutableList<PolymerizeGroupModel>>()
    private val propertyOutlineLd = MutableLiveData<PropertyOutlineModel>()

    init
    {
        viewModelScope.launch {
            AppDatabase.getInstance().getAccountDao().obAllAccount().flowOn(Dispatchers.IO).debounce(200).collectLatest { allAccountList ->
                val propertyOutlineModel = PropertyOutlineModel()
                propertyOutlineModel.totalProperty = allAccountList.sumByDouble { it.amount }
                propertyOutlineModel.netProperty = allAccountList.sumByDouble {
                    when (it.category)
                    {
                        AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> 0 - it.usedQuota
                        else                                     -> it.amount
                    }
                }
                propertyOutlineModel.totalLiabilities = allAccountList.sumByDouble {
                    when (it.category)
                    {
                        AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> it.usedQuota
                        AppConstant.MONEY_TRACES_CATEGORY_DEBT   -> if (it.amount < 0) 0 - it.amount else 0.0
                        else                                     -> 0.0
                    }
                }
                propertyOutlineModel.totalBorrowOut =
                        allAccountList.filter { it.category == AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT && it.amount > 0 }.sumByDouble {
                            it.amount
                        }
                propertyOutlineModel.totalBorrowIn =
                        0 - allAccountList.filter { it.category == AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT && it.amount < 0 }.sumByDouble {
                            it.amount
                        }
                propertyOutlineLd.postValue(propertyOutlineModel)

                val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()
                allAccountList.filter { it.showInTotal }.groupBy { it.category }.forEach { (category, accountList) ->
                    polymerizeGroupList.add(PolymerizeGroupModel(category.translate(), when (category)
                    {
                        AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> "${accountList.sumByDouble { it.usedQuota }.toAmountStr()}/${
                            accountList.sumByDouble { it.totalQuota }.toAmountStr()
                        }"
                        else                                     -> accountList.sumByDouble { it.amount }.toAmountStr()
                    }, mutableListOf<PolymerizeChildModel>().apply {
                        accountList.forEach { accountModel ->
                            add(PolymerizeChildModel(accountModel.type.getIcon(), accountModel.type.translate(), when (category)
                            {
                                AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> "${accountModel.usedQuota.toAmountStr()}/${accountModel.totalQuota.toAmountStr()}"
                                else                                     -> accountModel.amount.toAmountStr()
                            }, accountModel))
                        }
                    }))
                }
                allAccountsPolymerizeLd.postValue(polymerizeGroupList)
            }
        }
    }

    fun obAllAccounts(): LiveData<MutableList<PolymerizeGroupModel>> = allAccountsPolymerizeLd

    fun obPropertyOutline(): LiveData<PropertyOutlineModel> = propertyOutlineLd
}