package com.lizl.wtmg.custom.function

import com.blankj.utilcode.util.StringUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant

fun String.translate(): String
{
    return when (this)
    {
        AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL      -> StringUtils.getString(R.string.capital_account)
        AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT       -> StringUtils.getString(R.string.credit_account)
        AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT   -> StringUtils.getString(R.string.investment_account)

        AppConstant.ACCOUNT_TYPE_CASH                  -> StringUtils.getString(R.string.cash)
        AppConstant.ACCOUNT_TYPE_ALI_PAY               -> StringUtils.getString(R.string.ali_pay)
        AppConstant.ACCOUNT_TYPE_WE_CHAT               -> StringUtils.getString(R.string.we_chat)
        AppConstant.ACCOUNT_TYPE_BACK_CARD             -> StringUtils.getString(R.string.bank_card)
        AppConstant.ACCOUNT_TYPE_ANT_CREDIT_PAY        -> StringUtils.getString(R.string.ant_credit_pay)
        AppConstant.ACCOUNT_TYPE_JD_BT                 -> StringUtils.getString(R.string.jdbt)
        AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMB       -> StringUtils.getString(R.string.cmb)
        AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMBC      -> StringUtils.getString(R.string.cmbc)

        AppConstant.ACCOUNT_TYPE_ALI_PAY_FUND          -> StringUtils.getString(R.string.ali_pay_fund)
        AppConstant.ACCOUNT_TYPE_JD_FINANCE            -> StringUtils.getString(R.string.jd_finance)
        AppConstant.ACCOUNT_TYPE_TIANTIAN_FUND         -> StringUtils.getString(R.string.tiantian_fund)

        AppConstant.EXPENDITURE_TYPE_MEALS             -> StringUtils.getString(R.string.meals)
        AppConstant.EXPENDITURE_TYPE_SNACKS            -> StringUtils.getString(R.string.snacks)
        AppConstant.EXPENDITURE_TYPE_CLOTHES           -> StringUtils.getString(R.string.clothes)
        AppConstant.EXPENDITURE_TYPE_RENT              -> StringUtils.getString(R.string.rent)
        AppConstant.EXPENDITURE_TYPE_GAME              -> StringUtils.getString(R.string.game)
        AppConstant.EXPENDITURE_TYPE_NECESSARY         -> StringUtils.getString(R.string.necessary)
        AppConstant.EXPENDITURE_TYPE_HOUSING_LOAN      -> StringUtils.getString(R.string.housing_loan)
        AppConstant.EXPENDITURE_TYPE_LIVE_PAYMENT      -> StringUtils.getString(R.string.live_payment)
        AppConstant.EXPENDITURE_TYPE_BROKERAGE         -> StringUtils.getString(R.string.brokerage)
        AppConstant.EXPENDITURE_TYPE_TRAFFIC           -> StringUtils.getString(R.string.traffic)
        AppConstant.EXPENDITURE_TYPE_MEMBERSHIP        -> StringUtils.getString(R.string.membership)
        AppConstant.EXPENDITURE_TYPE_DIGITAL           -> StringUtils.getString(R.string.digital)
        AppConstant.EXPENDITURE_TYPE_TRANSFER_OUT      -> StringUtils.getString(R.string.transfer_out)
        AppConstant.EXPENDITURE_TYPE_ENTERTAINMENT     -> StringUtils.getString(R.string.entertainment)
        AppConstant.EXPENDITURE_TYPE_RED_ENVELOPES     -> StringUtils.getString(R.string.red_envelopes)

        AppConstant.TRANSFER_TYPE_TRANSFER             -> StringUtils.getString(R.string.transfer)

        AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS -> StringUtils.getString(R.string.financial_transactions)
        AppConstant.INCOME_TYPE_RED_ENVELOPES          -> StringUtils.getString(R.string.red_envelopes)
        AppConstant.INCOME_TYPE_WAGES                  -> StringUtils.getString(R.string.wages)
        AppConstant.INCOME_TYPE_SECOND_HAND            -> StringUtils.getString(R.string.second_hand)
        AppConstant.INCOME_TYPE_TRANSFER_IN            -> StringUtils.getString(R.string.transfer_in)

        else                                           -> ""
    }
}