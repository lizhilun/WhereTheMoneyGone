package com.lizl.wtmg.util

import com.blankj.utilcode.util.StringUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant

object TranslateUtil
{
    fun translatePropertyType(propertyType: String): String
    {
        return when (propertyType)
        {
            AppConstant.PROPERTY_TYPE_CASH -> StringUtils.getString(R.string.cash)
            AppConstant.PROPERTY_TYPE_ALI_PAY -> StringUtils.getString(R.string.ali_pay)
            AppConstant.PROPERTY_TYPE_WE_CHAT -> StringUtils.getString(R.string.we_chat)
            AppConstant.PROPERTY_TYPE_BACK_CARD -> StringUtils.getString(R.string.bank_card)
            else                                -> ""
        }
    }

    fun translatePropertyCategory(category: String): String
    {
        return when (category)
        {
            AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL -> StringUtils.getString(R.string.capital_account)
            AppConstant.PROPERTY_CATEGORY_TYPE_CREDIT -> StringUtils.getString(R.string.credit_account)
            AppConstant.PROPERTY_CATEGORY_TYPE_INVESTMENT -> StringUtils.getString(R.string.investment_account)
            else                                          -> ""
        }
    }

    fun translateExpenditureType(expenditureType: String): String
    {
        return when (expenditureType)
        {
            AppConstant.EXPENDITURE_TYPE_MEALS -> StringUtils.getString(R.string.meals)
            AppConstant.EXPENDITURE_TYPE_SNACKS -> StringUtils.getString(R.string.snacks)
            AppConstant.EXPENDITURE_TYPE_CLOTHES -> StringUtils.getString(R.string.clothes)
            AppConstant.EXPENDITURE_TYPE_RENT -> StringUtils.getString(R.string.rent)
            AppConstant.EXPENDITURE_TYPE_GAME -> StringUtils.getString(R.string.game)
            AppConstant.EXPENDITURE_TYPE_NECESSARY -> StringUtils.getString(R.string.necessary)
            else                                   -> ""
        }
    }
}