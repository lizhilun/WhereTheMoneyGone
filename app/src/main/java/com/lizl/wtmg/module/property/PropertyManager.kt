package com.lizl.wtmg.module.property

import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant

object PropertyManager
{
    fun getPropertyList() =
            listOf(AppConstant.PROPERTY_TYPE_CASH, AppConstant.PROPERTY_TYPE_ALI_PAY, AppConstant.PROPERTY_TYPE_WE_CHAT, AppConstant.PROPERTY_TYPE_BACK_CARD)

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
}