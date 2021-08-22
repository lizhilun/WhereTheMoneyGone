package com.lizl.wtmg.mvvm.activity

import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.*
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.ActivityAccountDetailBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel

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
        dataBinding.rvTracesList.adapter = polymerizeGroupAdapter
        dataBinding.rvTracesList.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initListener()
    {
        dataBinding.ctbTitle.setOnBackBtnClickListener { onBackPressed() }

        polymerizeGroupAdapter.setOnChildItemClickListener {
            if (it.tag is MoneyTracesModel)
            {
                PopupUtil.showTracesDetailPopup(it.tag)
            }
        }
    }

    override fun initData()
    {
        intent?.getStringExtra(DATA_ACCOUNT_TYPE)?.let { accountType ->
            dataBinding.ctbTitle.setTitle(accountType.translate())
            AppDatabase.getInstance().getAccountDao().queryAccountByType(accountType)?.let { accountModel ->
                when (accountModel.category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT ->
                    {
                        dataBinding.tvAccountOutline.setDecText(getString(R.string.used_quota))
                        dataBinding.tvAccountOutline.setMainText(accountModel.usedQuota.toAmountStr())

                        dataBinding.tvAccountInfo1.setDecText(getString(R.string.total_quota))
                        dataBinding.tvAccountInfo1.setMainText(accountModel.totalQuota.toAmountStr())

                        dataBinding.tvAccountInfo2.setDecText(getString(R.string.remaining_quota))
                        dataBinding.tvAccountInfo2.setMainText((accountModel.totalQuota - accountModel.usedQuota).toAmountStr())
                    }
                    AppConstant.ACCOUNT_CATEGORY_TYPE_INVESTMENT ->
                    {
                        dataBinding.tvAccountOutline.setDecText(getString(R.string.account_balance))
                        dataBinding.tvAccountOutline.setMainText(accountModel.amount.toAmountStr())

                        AppDatabase.getInstance().getMoneyTracesDao().obTracesByAccount(accountModel.type).observe(this, Observer { tracesList ->
                            val totalProfit = tracesList.filter { it.tracesType == AppConstant.INCOME_TYPE_FINANCIAL_TRANSACTIONS }.sumOf { it.amount }
                            dataBinding.tvAccountInfo1.setDecText(getString(R.string.total_profit))
                            dataBinding.tvAccountInfo1.setMainText(totalProfit.toAmountStr())

                            dataBinding.tvAccountInfo2.setDecText(getString(R.string.profit_rate))
                            dataBinding.tvAccountInfo2.setMainText("%.2f%%".format(totalProfit * 100 / (accountModel.amount - totalProfit)))
                        })
                    }
                    else                                         ->
                    {
                        dataBinding.tvAccountOutline.setDecText(getString(R.string.account_balance))
                        dataBinding.tvAccountOutline.setMainText(accountModel.amount.toAmountStr())

                        dataBinding.tvAccountInfo1.isVisible = false
                        dataBinding.tvAccountInfo2.isVisible = false
                    }
                }

                AppDatabase.getInstance().getMoneyTracesDao().obTracesByAccount(accountModel.type).observe(this, Observer { tracesList ->
                    launch {
                        val polymerizeGroupList = AccountManager.polymerizeTrancesList(tracesList) {
                            PolymerizeChildModel(it.tracesCategory.getIcon(), when (it.tracesCategory)
                            {
                                AppConstant.MONEY_TRACES_CATEGORY_TRANSFER ->
                                {
                                    if (accountModel.type == it.accountType) getString(R.string.transfer_out)
                                    else getString(R.string.transfer_in)
                                }
                                else                                       -> it.tracesType.translate()
                            }, it.amount.toAmountStr(), it)
                        }
                        ui { polymerizeGroupAdapter.setDiffNewData(polymerizeGroupList) }
                    }
                })
            }
        }
    }
}