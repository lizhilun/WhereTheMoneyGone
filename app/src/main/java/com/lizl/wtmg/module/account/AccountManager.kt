package com.lizl.wtmg.module.account

import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import java.util.concurrent.locks.*

object AccountManager
{
    private val capitalAccountList =
            listOf(AppConstant.ACCOUNT_TYPE_CASH, AppConstant.ACCOUNT_TYPE_ALI_PAY, AppConstant.ACCOUNT_TYPE_WE_CHAT, AppConstant.ACCOUNT_TYPE_BACK_CARD)

    private val creditAccountList = listOf(AppConstant.ACCOUNT_TYPE_ANT_CREDIT_PAY, AppConstant.ACCOUNT_TYPE_JD_BT, AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMB,
            AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMBC)

    private val investmentAccountList =
            listOf(AppConstant.ACCOUNT_TYPE_ALI_PAY_FUND, AppConstant.ACCOUNT_TYPE_JD_FINANCE, AppConstant.ACCOUNT_TYPE_TIANTIAN_FUND)

    val expenditureTypeList = listOf(AppConstant.EXPENDITURE_TYPE_MEALS, AppConstant.EXPENDITURE_TYPE_SNACKS, AppConstant.EXPENDITURE_TYPE_CLOTHES,
            AppConstant.EXPENDITURE_TYPE_NECESSARY, AppConstant.EXPENDITURE_TYPE_RENT, AppConstant.EXPENDITURE_TYPE_GAME,
            AppConstant.EXPENDITURE_TYPE_HOUSING_LOAN, AppConstant.EXPENDITURE_TYPE_LIVE_PAYMENT, AppConstant.EXPENDITURE_TYPE_BROKERAGE,
            AppConstant.EXPENDITURE_TYPE_TRAFFIC, AppConstant.EXPENDITURE_TYPE_MEMBERSHIP, AppConstant.EXPENDITURE_TYPE_DIGITAL,
            AppConstant.EXPENDITURE_TYPE_TRANSFER_OUT, AppConstant.EXPENDITURE_TYPE_ENTERTAINMENT, AppConstant.EXPENDITURE_TYPE_RED_ENVELOPES)

    val incomeTypeList = listOf(AppConstant.INCOME_TYPE_WAGES, AppConstant.INCOME_TYPE_RED_ENVELOPES, AppConstant.INCOME_TYPE_SECOND_HAND,
            AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS, AppConstant.INCOME_TYPE_TRANSFER_IN)

    val accountCategoryList =
            listOf(AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL, AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT, AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT)

    fun getAccountListByCategory(accountCategory: String): List<String>
    {
        return when (accountCategory)
        {
            AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL -> capitalAccountList
            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> creditAccountList
            AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT -> investmentAccountList
            else                                         -> capitalAccountList
        }
    }

    fun polymerizeTrancesList(tracesList: MutableList<MoneyTracesModel>): MutableList<PolymerizeGroupModel>
    {
        val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()

        tracesList.groupBy { it.recordDay }.forEach { (_, u) ->

            val dateInfo = String.format("%02d-%02d", u.first().recordMonth, u.first().recordDay)

            val amountInfo = u.sumBy {
                when (it.tracesCategory)
                {
                    AppConstant.MONEY_TRACES_CATEGORY_INCOME -> it.amonunt.toInt()
                    AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE -> 0 - it.amonunt.toInt()
                    else                                          -> 0
                }
            }.toString()

            val childList = mutableListOf<PolymerizeChildModel>().apply {
                u.forEach { tracesModel ->
                    add(PolymerizeChildModel(tracesModel.tracesCategory.getIcon(), tracesModel.tracesType.translate(), tracesModel.amonunt.toInt().toString(),
                            tracesModel))
                }
            }

            polymerizeGroupList.add(PolymerizeGroupModel(dateInfo, amountInfo, childList))
        }

        return polymerizeGroupList
    }
}