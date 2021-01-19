package com.lizl.wtmg.module.account

import com.lizl.wtmg.constant.AppConstant

object AccountManager
{
    private val capitalAccountList =
        listOf(AppConstant.ACCOUNT_TYPE_CASH, AppConstant.ACCOUNT_TYPE_ALI_PAY, AppConstant.ACCOUNT_TYPE_WE_CHAT, AppConstant.ACCOUNT_TYPE_BACK_CARD)

    private val creditAccountList = listOf(AppConstant.ACCOUNT_TYPE_ANT_CREDIT_PAY, AppConstant.ACCOUNT_TYPE_JD_BT, AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMB,
            AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMBC)

    private val investmentAccountList =
        listOf(AppConstant.ACCOUNT_TYPE_ALI_PAY_FUND, AppConstant.ACCOUNT_TYPE_JD_FINANCE, AppConstant.ACCOUNT_TYPE_TIANTIAN_FUND)

    val expenditureTypeList =
        listOf(AppConstant.EXPENDITURE_TYPE_MEALS, AppConstant.EXPENDITURE_TYPE_SNACKS, AppConstant.EXPENDITURE_TYPE_CLOTHES, AppConstant.EXPENDITURE_TYPE_GAME,
                AppConstant.EXPENDITURE_TYPE_RENT, AppConstant.EXPENDITURE_TYPE_NECESSARY, AppConstant.EXPENDITURE_TYPE_HOUSING_LOAN,
                AppConstant.EXPENDITURE_TYPE_LIVE_PAYMENT, AppConstant.EXPENDITURE_TYPE_BROKERAGE, AppConstant.EXPENDITURE_TYPE_TRAFFIC)

    val incomeTypeList = listOf(AppConstant.INCOME_TYPE_WAGES, AppConstant.INCOME_TYPE_RED_ENVELOPES, AppConstant.INCOME_TYPE_SECOND_HAND,
            AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS)

    val accountCategoryList =
        listOf(AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL, AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT, AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT)

    fun getAccountListByCategory(accountCategory: String): List<String>
    {
        return when (accountCategory)
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL    -> capitalAccountList
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT     -> creditAccountList
            AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT -> investmentAccountList
            else                                         -> capitalAccountList
        }
    }
}