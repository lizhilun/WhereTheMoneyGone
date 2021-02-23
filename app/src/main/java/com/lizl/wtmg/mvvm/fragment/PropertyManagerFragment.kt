package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.toAmountStr
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.FragmentPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.mvvm.activity.AccountDetailActivity
import com.lizl.wtmg.mvvm.activity.AddAccountActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.mvvm.activity.DebtDetailActivity
import kotlinx.android.synthetic.main.fragment_property_manager.*

class PropertyManagerFragment : BaseFragment<FragmentPropertyManagerBinding>(R.layout.fragment_property_manager)
{
    private lateinit var polymerizeGroupAdapter: PolymerizeGroupAdapter

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter()
        rv_property_category_group.adapter = polymerizeGroupAdapter
        rv_property_category_group.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initData()
    {
        AppDatabase.getInstance().getAccountDao().obAllAccount().observe(this, Observer { allAccountList ->

            dataBinding.totalProperty = allAccountList.sumByDouble { it.amount }
            dataBinding.netProperty = allAccountList.sumByDouble {
                when (it.category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> 0 - it.usedQuota
                    else                                     -> it.amount
                }
            }
            dataBinding.totalLiabilities = allAccountList.sumByDouble {
                when (it.category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> it.usedQuota
                    AppConstant.MONEY_TRACES_CATEGORY_DEBT   -> if (it.amount < 0) 0 - it.amount else 0.0
                    else                                     -> 0.0
                }
            }
            dataBinding.totalBorrowOut = allAccountList.filter { it.category == AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT && it.amount > 0 }.sumByDouble {
                it.amount
            }
            dataBinding.totalBorrowIn = 0 - allAccountList.filter { it.category == AppConstant.ACCOUNT_CATEGORY_TYPE_DEBT && it.amount < 0 }.sumByDouble {
                it.amount
            }

            val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()
            allAccountList.filter { it.showInTotal }.groupBy { it.category }.forEach { (category, accountList) ->
                polymerizeGroupList.add(PolymerizeGroupModel(category.translate(), when (category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> "${accountList.sumByDouble { it.usedQuota }.toAmountStr()}/${
                        accountList.sumByDouble { it.totalQuota }.toAmountStr()
                    }"
                    else                                     -> accountList.sumByDouble { it.amount }.toAmountStr()
                }, mutableListOf<PolymerizeChildModel>().apply {
                    accountList.forEach { accountModel ->
                        add(PolymerizeChildModel(accountModel.type.getIcon(), accountModel.type.translate(), when (category)
                        {
                            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> "${accountModel.usedQuota.toAmountStr()}/${accountModel.totalQuota.toAmountStr()}"
                            else                                     -> accountModel.amount.toAmountStr()
                        }, accountModel))
                    }
                }))
            }

            polymerizeGroupAdapter.replaceData(polymerizeGroupList)
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(AddAccountActivity::class.java) }

        tv_borrow_out.setOnClickListener {
            ActivityUtil.turnToActivity(DebtDetailActivity::class.java, Pair(DebtDetailActivity.DEBT_TYPE, DebtDetailActivity.DEBT_TYPE_TOTAL_BORROW_OUT))
        }

        tv_borrow_in.setOnClickListener {
            ActivityUtil.turnToActivity(DebtDetailActivity::class.java, Pair(DebtDetailActivity.DEBT_TYPE, DebtDetailActivity.DEBT_TYPE_TOTAL_BORROW_IN))
        }

        polymerizeGroupAdapter.setOnChildItemClickListener {
            ActivityUtil.turnToActivity(AccountDetailActivity::class.java, Pair(AccountDetailActivity.DATA_ACCOUNT_TYPE, (it.tag as AccountModel).type))
        }

        polymerizeGroupAdapter.setOnChildItemLongClickListener { polymerizeChildModel ->
            PopupUtil.showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
                add(PolymerizeChildModel(name = getString(R.string.modify), tag = "M"))
                add(PolymerizeChildModel(name = getString(R.string.delete), tag = "D"))
            }) {
                val accountModel = polymerizeChildModel.tag as AccountModel
                when (it.tag)
                {
                    "M" ->
                    {
                        ActivityUtil.turnToActivity(AddAccountActivity::class.java, Pair(AddAccountActivity.DATA_ACCOUNT_TYPE, accountModel.type),
                                Pair(AddAccountActivity.DATA_ACCOUNT_ID, accountModel.id))
                    }
                    "D" ->
                    {
                        PopupUtil.showConfirmPopup(getString(R.string.sure_to_delete_account)) {
                            AppDatabase.getInstance().getAccountDao().delete(accountModel)
                        }
                    }
                }
            }
        }
    }
}