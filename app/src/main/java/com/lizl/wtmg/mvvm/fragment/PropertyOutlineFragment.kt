package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.custom.function.setOnClickListener
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.custom.view.MenuDrawLayout
import com.lizl.wtmg.databinding.FragmentPropertyOutlineBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.ExpenditureModel
import com.lizl.wtmg.mvvm.activity.MoneyRecordActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.mvvm.model.BottomModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeChildModel
import com.lizl.wtmg.mvvm.model.polymerize.PolymerizeGroupModel
import com.lizl.wtmg.util.DateUtil
import com.lizl.wtmg.util.PopupUtil
import com.lizl.wtmg.util.TranslateUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import kotlinx.android.synthetic.main.fragment_property_outline.*

class PropertyOutlineFragment : BaseFragment<FragmentPropertyOutlineBinding>(R.layout.fragment_property_outline)
{
    private val menuDrawLayout: MenuDrawLayout by lazy { MenuDrawLayout(requireContext()) }

    private var monthExpenditure = 0
    private var monthIncome = 0

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

        AppDatabase.getInstance().getExpenditureDao().queryExpenditureByMonth(date.month).observe(this, Observer { expenditureLis ->
            monthExpenditure = expenditureLis.sumBy { it.amonunt.toInt() }
            updateMonthOutline()

            val polymerizeGroupList = mutableListOf<PolymerizeGroupModel>()

            expenditureLis.groupBy { it.recordDay }.forEach { (t, u) ->

                val dateInfo = String.format("%02d-%02d", date.month, t)
                val amountInfo = u.sumBy { it.amonunt.toInt() }.toString()
                val childList = mutableListOf<PolymerizeChildModel>().apply {
                    u.forEach { expenditureModel ->
                        add(PolymerizeChildModel(R.drawable.ic_spot_red, TranslateUtil.translateExpenditureType(expenditureModel.expenditureType),
                                expenditureModel.amonunt.toInt().toString(), expenditureModel))
                    }
                }

                polymerizeGroupList.add(PolymerizeGroupModel(dateInfo, amountInfo, childList))
            }
            polymerizeGroupAdapter.replaceData(polymerizeGroupList)
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(MoneyRecordActivity::class.java) }

        iv_menu.setOnClickListener {
            XPopup.Builder(requireContext()).popupPosition(PopupPosition.Left).hasStatusBarShadow(false).asCustom(menuDrawLayout).show()
        }

        iv_property_manager.setOnClickListener(true) { LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).post(true) }

        polymerizeGroupAdapter.setOnChildItemClickListener { }

        polymerizeGroupAdapter.setOnChildItemLongClickListener { polymerizeChildModel ->
            PopupUtil.showBottomListPopup(mutableListOf<BottomModel>().apply {
                add(BottomModel(name = getString(R.string.delete), tag = "D"))
            }) {
                when (it.tag)
                {
                    "D" -> deleteExpenditure(polymerizeChildModel.tag as ExpenditureModel)
                }
            }
        }
    }

    private fun deleteExpenditure(expenditureModel: ExpenditureModel)
    {
        AppDatabase.getInstance().getExpenditureDao().delete(expenditureModel)
        AppDatabase.getInstance().getPropertyDao().queryPropertyByType(expenditureModel.accountType)?.let {
            it.amount = it.amount + expenditureModel.amonunt
            AppDatabase.getInstance().getPropertyDao().update(it)
        }
    }

    private fun updateMonthOutline()
    {
        dataBinding.monthExpenditure = monthExpenditure.toString()
        dataBinding.monthIncome = monthIncome.toString()
        dataBinding.monthBalance = (monthIncome - monthExpenditure).toString()
    }
}