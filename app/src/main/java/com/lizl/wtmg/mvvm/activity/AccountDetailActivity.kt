package com.lizl.wtmg.mvvm.activity

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.ActivityAccountDetailBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import kotlinx.android.synthetic.main.activity_account_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class AccountDetailActivity : BaseActivity<ActivityAccountDetailBinding>(R.layout.activity_account_detail)
{
    companion object
    {
        const val DATA_ACCOUNT_TYPE = "DATA_ACCOUNT_TYPE"
    }

    private lateinit var polymerizeGroupAdapter: PolymerizeGroupAdapter

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter()
        rv_traces_list.adapter = polymerizeGroupAdapter
        rv_traces_list.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }
    }

    override fun initData()
    {
        intent?.getStringExtra(DATA_ACCOUNT_TYPE)?.let { accountType ->
            ctb_title.setTitle(accountType.translate())
            AppDatabase.getInstance().getAccountDao().queryAccountByType(accountType)?.let { accountModel ->
                when (accountModel.category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT ->
                    {
                        tv_account_outline.setDecText(getString(R.string.used_quota))
                        tv_account_outline.setMainText(accountModel.usedQuota.toInt().toString())

                        tv_account_info_1.setDecText(getString(R.string.used_quota))
                        tv_account_info_1.setMainText(accountModel.totalQuota.toInt().toString())

                        tv_account_info_2.setDecText(getString(R.string.used_quota_rate))
                        tv_account_info_2.setMainText(String.format("%.2f%%", accountModel.usedQuota * 100 / accountModel.totalQuota))
                    }
                    AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT ->
                    {
                        tv_account_outline.setDecText(getString(R.string.account_balance))
                        tv_account_outline.setMainText("${accountModel.amount.toInt()}")

                        AppDatabase.getInstance().getMoneyTracesDao().queryTracesByAccount(accountModel.type).observe(this, Observer { tracesList ->
                            val totalProfit = tracesList.filter { it.tracesType == AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS }.sumBy { it.amonunt.toInt() }
                            tv_account_info_1.setDecText(getString(R.string.total_profit))
                            tv_account_info_1.setMainText(totalProfit.toString())

                            tv_account_info_2.setDecText(getString(R.string.profit_rate))
                            tv_account_info_2.setMainText(String.format("%.2f%%", totalProfit * 100 / (accountModel.amount - totalProfit)))
                        })
                    }
                    else                                         ->
                    {
                        tv_account_outline.setDecText(getString(R.string.account_balance))
                        tv_account_outline.setMainText("${accountModel.amount.toInt()}")

                        tv_account_info_1.isVisible = false
                        tv_account_info_2.isVisible = false
                    }
                }

                AppDatabase.getInstance().getMoneyTracesDao().queryTracesByAccount(accountModel.type).observe(this, Observer { tracesList ->
                    GlobalScope.launch {
                        tracesList.sortByDescending { it.recordTime }
                        val polymerizeGroupList = AccountManager.polymerizeTrancesList(tracesList)
                        GlobalScope.ui { polymerizeGroupAdapter.replaceData(polymerizeGroupList) }
                    }
                })
            }
        }
    }
}