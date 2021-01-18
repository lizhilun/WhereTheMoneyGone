package com.lizl.wtmg.module.account

import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.db.model.MoneyTracesModel

object AccountDataManager
{
    fun addMoneyTraces(moneyTracesModel: MoneyTracesModel)
    {
        AppDatabase.getInstance().getMoneyTracesDao().insert(moneyTracesModel)

        val payAccountModel = AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType) ?: return

        when (moneyTracesModel.tracesCategory)
        {
            AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE ->
            {
                handleMoneyOut(payAccountModel, moneyTracesModel.amonunt)
            }
            AppConstant.MONEY_TRACES_CATEGORY_INCOME ->
            {
                handleMoneyIn(payAccountModel, moneyTracesModel.amonunt)
            }
            AppConstant.MONEY_TRACES_CATEGORY_TRANSFER ->
            {
                handleMoneyOut(payAccountModel, moneyTracesModel.amonunt)

                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.transferToAccount)?.let { inAccountModel ->
                    handleMoneyIn(inAccountModel, moneyTracesModel.amonunt)
                    AppDatabase.getInstance().getAccountDao().insert(inAccountModel)
                }
            }
        }

        AppDatabase.getInstance().getAccountDao().insert(payAccountModel)
    }

    fun deleteExpenditure(moneyTracesModel: MoneyTracesModel)
    {
        AppDatabase.getInstance().getMoneyTracesDao().delete(moneyTracesModel)

        val payAccountModel = AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType) ?: return

        when (moneyTracesModel.tracesCategory)
        {
            AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE ->
            {
                handleMoneyIn(payAccountModel, moneyTracesModel.amonunt)
            }
            AppConstant.MONEY_TRACES_CATEGORY_INCOME ->
            {
                handleMoneyOut(payAccountModel, moneyTracesModel.amonunt)
            }
            AppConstant.MONEY_TRACES_CATEGORY_TRANSFER ->
            {
                handleMoneyIn(payAccountModel, moneyTracesModel.amonunt)

                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.transferToAccount)?.let { inAccountModel ->
                    handleMoneyOut(inAccountModel, moneyTracesModel.amonunt)
                    AppDatabase.getInstance().getAccountDao().insert(inAccountModel)
                }
            }
        }

        AppDatabase.getInstance().getAccountDao().insert(payAccountModel)
    }

    private fun handleMoneyOut(accountModel: AccountModel, amount: Float)
    {
        if (accountModel.category == AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL)
        {
            accountModel.amount -= amount
        }
        else if (accountModel.category == AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT)
        {
            accountModel.usedQuota += amount
        }
        else if (accountModel.category == AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT)
        {
            accountModel.amount -= amount
        }
    }

    private fun handleMoneyIn(accountModel: AccountModel, amount: Float)
    {
        handleMoneyOut(accountModel, 0 - amount)
    }
}