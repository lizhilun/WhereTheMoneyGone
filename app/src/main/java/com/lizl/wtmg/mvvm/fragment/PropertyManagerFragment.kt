package com.lizl.wtmg.mvvm.fragment

import android.accounts.Account
import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.ToastUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.FragmentPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountDataManager
import com.lizl.wtmg.mvvm.activity.AddAccountActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.util.PopupUtil
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

            dataBinding.totalProperty = allAccountList.sumBy { it.amount.toInt() }
            dataBinding.netProperty = allAccountList.sumBy {
                when (it.category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> 0 - it.usedQuota.toInt()
                    else                                     -> it.amount.toInt()
                }
            }
            dataBinding.totalLiabilities = allAccountList.sumBy { it.usedQuota.toInt() }

            val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()
            allAccountList.groupBy { it.category }.forEach { (category, accountList) ->
                polymerizeGroupList.add(PolymerizeGroupModel(category.translate(), when (category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> "${accountList.sumBy { it.usedQuota.toInt() }}/${accountList.sumBy { it.totalQuota.toInt() }}"
                    else                                     -> accountList.sumBy { it.amount.toInt() }.toString()
                }, mutableListOf<PolymerizeChildModel>().apply {
                    accountList.forEach { accountModel ->
                        add(PolymerizeChildModel(accountModel.type.getIcon(), accountModel.type.translate(), when (category)
                        {
                            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> "${accountModel.usedQuota.toInt()}/${accountModel.totalQuota.toInt()}"
                            else                                     -> accountModel.amount.toInt().toString()
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

        polymerizeGroupAdapter.setOnChildItemClickListener {
            when (it.tag)
            {
                is AccountModel ->
                {
                    ActivityUtil.turnToActivity(AddAccountActivity::class.java, Pair(AddAccountActivity.DATA_ACCOUNT_TYPE, it.tag.type),
                            Pair(AddAccountActivity.DATA_ACCOUNT_ID, it.tag.id))
                }
            }
        }

        polymerizeGroupAdapter.setOnChildItemLongClickListener { polymerizeChildModel ->
            PopupUtil.showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
                add(PolymerizeChildModel(name = getString(R.string.delete), tag = "D"))
            }) {
                when (it.tag)
                {
                    "D" ->
                    {
                        val accountModel = polymerizeChildModel.tag as AccountModel
                        PopupUtil.showConfirmPopup(getString(R.string.sure_to_delete_account)) {
                            AppDatabase.getInstance().getAccountDao().delete(accountModel)
                        }
                    }
                }
            }
        }
    }
}