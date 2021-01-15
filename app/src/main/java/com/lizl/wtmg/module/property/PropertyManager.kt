package com.lizl.wtmg.module.property

import com.blankj.utilcode.util.StringUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant

object PropertyManager
{
    fun getPropertyNameByType(propertyType: String): String
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

    fun getPropertyIcon(propertyType: String): Int
    {
        return when (propertyType)
        {
            AppConstant.PROPERTY_TYPE_CASH -> R.drawable.ic_baseline_cash_24
            AppConstant.PROPERTY_TYPE_WE_CHAT -> R.drawable.ic_baseline_we_chat_24
            AppConstant.PROPERTY_TYPE_ALI_PAY -> R.drawable.ic_baseline_ali_pay_24
            AppConstant.PROPERTY_TYPE_BACK_CARD -> R.drawable.ic_baseline_bank_card_24
            else                                -> R.drawable.ic_baseline_cash_24
        }
    }

    fun getPropertyCategoryByType(propertyType: String): String
    {
        return when (propertyType)
        {
            AppConstant.PROPERTY_TYPE_CASH -> AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL
            AppConstant.PROPERTY_TYPE_ALI_PAY -> AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL
            AppConstant.PROPERTY_TYPE_WE_CHAT -> AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL
            AppConstant.PROPERTY_TYPE_BACK_CARD -> AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL
            else                                -> AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL
        }
    }

    fun getPropertyCategoryName(category: String): String
    {
        return when (category)
        {
            AppConstant.PROPERTY_CATEGORY_TYPE_CAPITAL -> StringUtils.getString(R.string.capital_account)
            AppConstant.PROPERTY_CATEGORY_TYPE_CREDIT -> StringUtils.getString(R.string.credit_account)
            AppConstant.PROPERTY_CATEGORY_TYPE_INVESTMENT -> StringUtils.getString(R.string.investment_account)
            else                                          -> ""
        }
    }

}