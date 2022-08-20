package com.lizl.wtmg.module.account

import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.db.model.MoneyTracesModel

object TracesManager {
    fun addMoneyTraces(moneyTracesModel: MoneyTracesModel) {
        AppDatabase.getInstance().getMoneyTracesDao().insert(moneyTracesModel)

        val payAccountModel = AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType)
                ?: return

        when (moneyTracesModel.tracesCategory) {
            AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE -> handleAccountMoneyOut(payAccountModel, moneyTracesModel.amount)
            AppConstant.MONEY_TRACES_CATEGORY_INCOME -> handleAccountMoneyIn(payAccountModel, moneyTracesModel.amount)
            AppConstant.MONEY_TRACES_CATEGORY_TRANSFER -> {
                handleAccountMoneyOut(payAccountModel, moneyTracesModel.amount)

                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.transferToAccount)?.let { inAccountModel ->
                    handleAccountMoneyIn(inAccountModel, moneyTracesModel.amount)
                    AppDatabase.getInstance().getAccountDao().insert(inAccountModel)
                }
            }
            AppConstant.MONEY_TRACES_CATEGORY_DEBT -> {
                handleAccountMoneyOut(payAccountModel, moneyTracesModel.amount)

                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.transferToAccount)?.let { inAccountModel ->
                    handleAccountMoneyIn(inAccountModel, moneyTracesModel.amount)
                    AppDatabase.getInstance().getAccountDao().insert(inAccountModel)
                }
            }
        }

        AppDatabase.getInstance().getAccountDao().insert(payAccountModel)
    }

    fun deleteMoneyTraces(moneyTracesModel: MoneyTracesModel) {
        AppDatabase.getInstance().getMoneyTracesDao().delete(moneyTracesModel)

        val payAccountModel = AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.accountType)
                ?: return

        when (moneyTracesModel.tracesCategory) {
            AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE -> handleAccountMoneyIn(payAccountModel, moneyTracesModel.amount)
            AppConstant.MONEY_TRACES_CATEGORY_INCOME -> handleAccountMoneyOut(payAccountModel, moneyTracesModel.amount)
            AppConstant.MONEY_TRACES_CATEGORY_TRANSFER -> {
                handleAccountMoneyIn(payAccountModel, moneyTracesModel.amount)

                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.transferToAccount)?.let { inAccountModel ->
                    handleAccountMoneyOut(inAccountModel, moneyTracesModel.amount)
                    AppDatabase.getInstance().getAccountDao().insert(inAccountModel)
                }
            }
            AppConstant.MONEY_TRACES_CATEGORY_DEBT -> {
                handleAccountMoneyIn(payAccountModel, moneyTracesModel.amount)

                AppDatabase.getInstance().getAccountDao().queryAccountByType(moneyTracesModel.transferToAccount)?.let { inAccountModel ->
                    handleAccountMoneyOut(inAccountModel, moneyTracesModel.amount)
                    AppDatabase.getInstance().getAccountDao().insert(inAccountModel)
                }
            }
        }

        AppDatabase.getInstance().getAccountDao().insert(payAccountModel)
    }

    private fun handleAccountMoneyOut(accountModel: AccountModel, amount: Double) {
        when (accountModel.category) {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL -> accountModel.amount -= amount
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> accountModel.usedQuota += amount
            AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT -> accountModel.amount -= amount
            AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT -> accountModel.amount -= amount
        }
    }

    private fun handleAccountMoneyIn(accountModel: AccountModel, amount: Double) {
        handleAccountMoneyOut(accountModel, 0 - amount)
    }
}