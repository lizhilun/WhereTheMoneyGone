package com.lizl.wtmg.mvvm.activity

import com.lizl.wtmg.R
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.ActivityDebtDetailBinding
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.mvvm.viewmodel.DebtViewModel
import com.lizl.wtmg.util.ViewModelUtil

class DebtDetailActivity : BaseActivity<ActivityDebtDetailBinding>(R.layout.activity_debt_detail)
{
    companion object
    {
        const val DEBT_TYPE = "DEBT_TYPE"
        const val DEBT_TYPE_TOTAL_BORROW_OUT = 1
        const val DEBT_TYPE_TOTAL_BORROW_IN = 2
    }

    private lateinit var debtListAdapter: PolymerizeGroupAdapter
    private val debtViewModel: DebtViewModel by lazy { ViewModelUtil.getSharedViewModel(DebtViewModel::class.java) }

    override fun initView()
    {
        debtListAdapter = PolymerizeGroupAdapter()
        dataBinding.rvDebt.adapter = debtListAdapter
        dataBinding.rvDebt.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(R.dimen.global_content_padding_content)))
    }

    override fun initData()
    {
        val debtType = intent?.extras?.getInt(DEBT_TYPE, DEBT_TYPE_TOTAL_BORROW_OUT) ?: DEBT_TYPE_TOTAL_BORROW_OUT

        dataBinding.ctbTitle.setTitle(if (debtType == DEBT_TYPE_TOTAL_BORROW_IN) getString(R.string.total_borrow_in) else getString(R.string.total_borrow_out))
        dataBinding.tvDebtOutline.setDecText(if (debtType == DEBT_TYPE_TOTAL_BORROW_IN) getString(R.string.total_borrow_in) else getString(R.string.total_borrow_out))

        debtViewModel.setDebtType(debtType)
        debtViewModel.obPolymerizeDebts().observe(this, {
            debtListAdapter.setDiffNewData(it)
        })
        debtViewModel.obTotalDebt().observe(this, {
            dataBinding.totalDebt = it
        })
    }

    override fun initListener()
    {
        dataBinding.ctbTitle.setOnBackBtnClickListener { onBackPressed() }

        debtListAdapter.setOnChildItemClickListener {
            PopupUtil.showTracesDetailPopup(it.tag as MoneyTracesModel)
        }
    }
}