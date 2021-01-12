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
}