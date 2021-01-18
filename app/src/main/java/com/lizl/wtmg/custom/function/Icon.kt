package com.lizl.wtmg.custom.function

import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant

fun String.getIcon(default: Int = R.drawable.ic_baseline_others_24): Int
{
    return when (this)
    {
        AppConstant.ACCOUNT_TYPE_CASH -> R.drawable.ic_baseline_cash_24
        AppConstant.ACCOUNT_TYPE_WE_CHAT -> R.drawable.ic_baseline_we_chat_24
        AppConstant.ACCOUNT_TYPE_ALI_PAY -> R.drawable.ic_baseline_ali_pay_24
        AppConstant.ACCOUNT_TYPE_BACK_CARD -> R.drawable.ic_baseline_bank_card_24
        AppConstant.ACCOUNT_TYPE_ANT_CREDIT_PAY -> R.drawable.ic_baseline_ant_credit_pay_24
        AppConstant.ACCOUNT_TYPE_JD_BT -> R.drawable.ic_baseline_jd_bt_24
        AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMB -> R.drawable.ic_baseline_cmb_24
        AppConstant.ACCOUNT_TYPE_CREDIT_CARD_CMBC -> R.drawable.ic_baseline_cmbc_24

        AppConstant.ACCOUNT_CATEGORY_TYPE_CAPITAL -> R.drawable.ic_baseline_cash_24
        AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> R.drawable.ic_baseline_credit_24
        AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT -> R.drawable.ic_baseline_investment_24

        AppConstant.EXPENDITURE_TYPE_MEALS -> R.drawable.ic_baseline_meals_24
        AppConstant.EXPENDITURE_TYPE_SNACKS -> R.drawable.ic_baseline_snacks_24
        AppConstant.EXPENDITURE_TYPE_CLOTHES -> R.drawable.ic_baseline_clothes_24
        AppConstant.EXPENDITURE_TYPE_GAME -> R.drawable.ic_baseline_game_24
        AppConstant.EXPENDITURE_TYPE_RENT -> R.drawable.ic_baseline_rent_24
        AppConstant.EXPENDITURE_TYPE_NECESSARY -> R.drawable.ic_baseline_necessary_24

        AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS -> R.drawable.ic_baseline_financial_transactions_24
        AppConstant.INCOME_TYPE_RED_ENVELOPES -> R.drawable.ic_baseline_red_envelopes_24
        AppConstant.INCOME_TYPE_WAGES -> R.drawable.ic_baseline_wages_24
        AppConstant.INCOME_TYPE_SECOND_HAND -> R.drawable.ic_ic_baseline_second_hand_24

        AppConstant.MONEY_TRACES_CATEGORY_INCOME -> R.drawable.ic_spot_green
        AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE -> R.drawable.ic_spot_red

        else                                           -> default
    }
}