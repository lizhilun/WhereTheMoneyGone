package com.lizl.wtmg.mvvm.fragment

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.R.dimen
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.databinding.FragmentPropertyManagerBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.AccountModel
import com.lizl.wtmg.mvvm.activity.AccountDetailActivity
import com.lizl.wtmg.mvvm.activity.AddAccountActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.mvvm.activity.DebtDetailActivity
import com.lizl.wtmg.mvvm.viewmodel.AccountViewModel
import com.lizl.wtmg.util.ViewModelUtil
import kotlinx.android.synthetic.main.fragment_property_manager.*

class PropertyManagerFragment : BaseFragment<FragmentPropertyManagerBinding>(R.layout.fragment_property_manager)
{
    private lateinit var polymerizeGroupAdapter: PolymerizeGroupAdapter
    private val accountViewModel: AccountViewModel by lazy { ViewModelUtil.getSharedViewModel(AccountViewModel::class.java) }

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter()
        rv_property_category_group.adapter = polymerizeGroupAdapter
        rv_property_category_group.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(dimen.global_content_padding_content)))
    }

    override fun initData()
    {
        accountViewModel.obAllAccounts().observe(this, {
            polymerizeGroupAdapter.setDiffNewData(it)
        })

        accountViewModel.obPropertyOutline().observe(this, {
            dataBinding.propertyOutlineModel = it
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
                        ActivityUtil.turnToActivity(AddAccountActivity::class.java,
                                                    Pair(AddAccountActivity.DATA_ACCOUNT_TYPE, accountModel.type),
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