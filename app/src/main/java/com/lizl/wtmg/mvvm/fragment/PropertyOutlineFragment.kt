package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.custom.function.getIcon
import com.lizl.wtmg.custom.function.translate
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.custom.view.MenuDrawLayout
import com.lizl.wtmg.databinding.FragmentPropertyOutlineBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountDataManager
import com.lizl.wtmg.mvvm.activity.MoneyTracesRecordActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeModel
import com.lizl.wtmg.util.DateUtil
import com.lizl.wtmg.util.PopupUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import kotlinx.android.synthetic.main.fragment_property_outline.*

class PropertyOutlineFragment : BaseFragment<FragmentPropertyOutlineBinding>(R.layout.fragment_property_outline)
{
    private val menuDrawLayout: MenuDrawLayout by lazy { MenuDrawLayout(requireContext()) }

    private lateinit var polymerizeGroupAdapter: PolymerizeGroupAdapter

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter()
        rv_daily_outline.adapter = polymerizeGroupAdapter
        rv_daily_outline.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(R.dimen.global_content_padding_content)))
    }

    override fun initData()
    {
        val date = DateUtil.Date()

        AppDatabase.getInstance().getMoneyTracesDao().queryTracesByMonth(date.month).observe(this, Observer { tracesList ->

            dataBinding.monthExpenditure = tracesList.filter {
                it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE && it.tracesCategory != AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
            }.sumBy { it.amonunt.toInt() }

            dataBinding.monthIncome = tracesList.filter {
                it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME && it.tracesCategory != AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
            }.sumBy { it.amonunt.toInt() }

            val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()

            tracesList.groupBy { it.recordDay }.forEach { (t, u) ->

                val dateInfo = String.format("%02d-%02d", date.month, t)

                val amountInfo = u.sumBy {
                    when (it.tracesCategory)
                    {
                        AppConstant.MONEY_TRACES_CATEGORY_INCOME -> it.amonunt.toInt()
                        else                                     -> 0 - it.amonunt.toInt()
                    }
                }.toString()

                val childList = mutableListOf<PolymerizeChildModel>().apply {
                    u.forEach { tracesModel ->
                        add(PolymerizeChildModel(tracesModel.tracesCategory.getIcon(), tracesModel.tracesType.translate(),
                                tracesModel.amonunt.toInt().toString(), tracesModel))
                    }
                }

                polymerizeGroupList.add(PolymerizeGroupModel(dateInfo, amountInfo, childList))
            }
            polymerizeGroupAdapter.replaceData(polymerizeGroupList)
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(MoneyTracesRecordActivity::class.java) }

        iv_menu.setOnClickListener {
            XPopup.Builder(requireContext()).popupPosition(PopupPosition.Left).hasStatusBarShadow(false).asCustom(menuDrawLayout).show()
        }

        iv_property_manager.setOnClickListener { LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).post(true) }

        polymerizeGroupAdapter.setOnChildItemClickListener { }

        polymerizeGroupAdapter.setOnChildItemLongClickListener { polymerizeChildModel ->
            PopupUtil.showBottomListPopup(mutableListOf<PolymerizeModel>().apply {
                add(PolymerizeChildModel(name = getString(R.string.delete), tag = "D"))
            }) {
                when (it.tag)
                {
                    "D" -> AccountDataManager.deleteExpenditure(polymerizeChildModel.tag as MoneyTracesModel)
                }
            }
        }
    }
}