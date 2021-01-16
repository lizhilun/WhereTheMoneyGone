package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.custom.view.MenuDrawLayout
import com.lizl.wtmg.databinding.FragmentPropertyOutlineBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.mvvm.activity.MoneyRecordActivity
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.util.DateUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import kotlinx.android.synthetic.main.fragment_property_outline.*

class PropertyOutlineFragment : BaseFragment<FragmentPropertyOutlineBinding>(R.layout.fragment_property_outline)
{
    private val menuDrawLayout: MenuDrawLayout by lazy { MenuDrawLayout(requireContext()) }

    private var monthExpenditure = 0
    private var monthIncome = 0

    override fun initData()
    {
        val date = DateUtil.Date()

        AppDatabase.getInstance().getExpenditureDao().queryExpenditureByMonth(date.month).observe(this, Observer { expenditureLis ->
            monthExpenditure = expenditureLis.sumBy { it.amonunt.toInt() }
            updateMonthOutline()
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(MoneyRecordActivity::class.java) }

        iv_menu.setOnClickListener {
            XPopup.Builder(requireContext()).popupPosition(PopupPosition.Left).hasStatusBarShadow(false).asCustom(menuDrawLayout).show()
        }

        iv_property_manager.setOnClickListener { LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).post(true) }
    }

    private fun updateMonthOutline()
    {
        dataBinding.monthExpenditure = monthExpenditure.toString()
        dataBinding.monthIncome = monthIncome.toString()
        dataBinding.monthBalance = (monthIncome - monthExpenditure).toString()
    }
}