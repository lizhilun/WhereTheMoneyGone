package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.FragmentPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.mvvm.activity.AddAccountActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.util.TranslateUtil
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

            tv_total_property.text = allAccountList.sumBy { it.amount.toInt() }.toString()
            tv_net_property.text = allAccountList.sumBy { it.amount.toInt() }.toString()
            tv_total_liabilities.text = allAccountList.sumBy { it.usedQuota.toInt() }.toString()

            val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()
            allAccountList.groupBy { it.category }.forEach { (category, accountList) ->
                polymerizeGroupList.add(PolymerizeGroupModel(category.translate(), when (category)
                {
                    AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> accountList.sumBy { it.usedQuota.toInt() }.toString()
                    else                                     -> accountList.sumBy { it.amount.toInt() }.toString()
                }, mutableListOf<PolymerizeChildModel>().apply {
                    accountList.forEach { accountModel ->
                        add(PolymerizeChildModel(AccountManager.getAccountIcon(accountModel.type), accountModel.type.translate(), when (category)
                        {
                            AppConstant.ACCOUNT_CATEGORY_TYPE_CREDIT -> accountModel.usedQuota.toInt().toString()
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
    }
}