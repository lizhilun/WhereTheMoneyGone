package com.lizl.wtmg.module.account

import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.ExpenditureModel

object AccountDataManager
{
    fun addExpenditure(expenditureModel: ExpenditureModel)
    {
        AppDatabase.getInstance().getExpenditureDao().insert(expenditureModel)

        when (AccountManager.getAccountCategoryByType(expenditureModel.accountType))
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL ->
            {
                AppDatabase.getInstance().getCapitalAccountDao().queryAccountByType(expenditureModel.accountType)?.let {
                    it.amount -= expenditureModel.amonunt
                    AppDatabase.getInstance().getCapitalAccountDao().insert(it)
                }
            }
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT  ->
            {
                AppDatabase.getInstance().getCreditAccountDao().queryAccountByType(expenditureModel.accountType)?.let {
                    it.usedQuota += expenditureModel.amonunt
                    AppDatabase.getInstance().getCreditAccountDao().insert(it)
                }
            }
        }
    }

    fun deleteExpenditure(expenditureModel: ExpenditureModel)
    {
        AppDatabase.getInstance().getExpenditureDao().delete(expenditureModel)

        when (AccountManager.getAccountCategoryByType(expenditureModel.accountType))
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL ->
            {
                AppDatabase.getInstance().getCapitalAccountDao().queryAccountByType(expenditureModel.accountType)?.let {
                    it.amount += expenditureModel.amonunt
                    AppDatabase.getInstance().getCapitalAccountDao().insert(it)
                }
            }
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT  ->
            {
                AppDatabase.getInstance().getCreditAccountDao().queryAccountByType(expenditureModel.accountType)?.let {
                    it.usedQuota -= expenditureModel.amonunt
                    AppDatabase.getInstance().getCreditAccountDao().insert(it)
                }
            }
        }
    }
}