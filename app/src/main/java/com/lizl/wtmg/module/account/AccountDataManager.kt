package com.lizl.wtmg.module.account

import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel

object AccountDataManager
{
    fun addExpenditure(moneyTracesModel: MoneyTracesModel)
    {
        AppDatabase.getInstance().getMoneyTracesDao().insert(moneyTracesModel)

        when (AccountManager.getAccountCategoryByType(moneyTracesModel.accountType))
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL ->
            {
                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType)?.let {
                    it.amount -= moneyTracesModel.amonunt
                    AppDatabase.getInstance().getAccountDao().insert(it)
                }
            }
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT  ->
            {
                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType)?.let {
                    it.usedQuota += moneyTracesModel.amonunt
                    AppDatabase.getInstance().getAccountDao().insert(it)
                }
            }
        }
    }

    fun deleteExpenditure(moneyTracesModel: MoneyTracesModel)
    {
        AppDatabase.getInstance().getMoneyTracesDao().delete(moneyTracesModel)

        when (AccountManager.getAccountCategoryByType(moneyTracesModel.accountType))
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL ->
            {
                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType)?.let {
                    it.amount += moneyTracesModel.amonunt
                    AppDatabase.getInstance().getAccountDao().insert(it)
                }
            }
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT  ->
            {
                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType)?.let {
                    it.usedQuota -= moneyTracesModel.amonunt
                    AppDatabase.getInstance().getAccountDao().insert(it)
                }
            }
        }
    }
}