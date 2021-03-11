package com.lizl.wtmg.mvvm.activity

import androidx.lifecycle.Observer
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.databinding.ActivityDebtDetailBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import kotlinx.android.synthetic.main.activity_debt_detail.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.math.abs

class DebtDetailActivity : BaseActivity<ActivityDebtDetailBinding>(R.layout.activity_debt_detail)
{
    companion object
    {
        const val DEBT_TYPE = "DEBT_TYPE"
        const val DEBT_TYPE_TOTAL_BORROW_OUT = 1
        const val DEBT_TYPE_TOTAL_BORROW_IN = 2
    }

    private lateinit var debtListAdapter: PolymerizeGroupAdapter

    override fun initView()
    {
        debtListAdapter = PolymerizeGroupAdapter()
        rv_debt.adapter = debtListAdapter
    }

    override fun initData()
    {
        val debtType = intent?.extras?.getInt(DEBT_TYPE, DEBT_TYPE_TOTAL_BORROW_OUT) ?: DEBT_TYPE_TOTAL_BORROW_OUT

        ctb_title.setTitle(if (debtType == DEBT_TYPE_TOTAL_BORROW_IN) getString(R.string.total_borrow_in) else getString(R.string.total_borrow_out))
        tv_debt_outline.setDecText(if (debtType == DEBT_TYPE_TOTAL_BORROW_IN) getString(R.string.total_borrow_in) else getString(R.string.total_borrow_out))

        AppDatabase.getInstance().getAccountDao().obAllAccount().observe(this, Observer { allAccountList ->
            GlobalScope.launch {
                val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()

                val debtAccountList = allAccountList.filter {
                    it.category == AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT && ((debtType == DEBT_TYPE_TOTAL_BORROW_IN && it.amount < 0) || (debtType == DEBT_TYPE_TOTAL_BORROW_OUT && it.amount > 0))
                }

                dataBinding.totalDebt = abs(debtAccountList.sumByDouble { it.amount })

                debtAccountList.forEach { debtAccount ->
                    val polymerizeChildList = mutableListOf<PolymerizeChildModel>()
                    AppDatabase.getInstance().getMoneyTracesDao().queryTracesByAccount(debtAccount.type).forEach { tracesModel ->
                        polymerizeChildList.add(PolymerizeChildModel(tracesModel.tracesCategory.getIcon(), tracesModel.tracesType.translate(),
                                tracesModel.amount.toAmountStr(), tracesModel))
                    }
                    polymerizeGroupList.add(PolymerizeGroupModel(debtAccount.name, abs(debtAccount.amount).toAmountStr(), polymerizeChildList))
                }
                ui { debtListAdapter.setDiffNewData(polymerizeGroupList) }
            }
        })
    }

    override fun initListener()
    {
        ctb_title.setOnBackBtnClickListener { onBackPressed() }

        debtListAdapter.setOnChildItemClickListener {
            PopupUtil.showTracesDetailPopup(it.tag as MoneyTracesModel)
        }
    }
}