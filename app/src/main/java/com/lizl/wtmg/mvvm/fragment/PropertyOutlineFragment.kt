package com.lizl.wtmg.mvvm.fragment

import androidx.lifecycle.Observer
import com.blankj.utilcode.util.ActivityUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.AppConstant
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.custom.view.MenuDrawLayout
import com.lizl.wtmg.databinding.FragmentPropertyOutlineBinding
import com.lizl.wtmg.db.AppDatabase
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.account.AccountManager
import com.lizl.wtmg.module.mainpic.MainPicHandler
import com.lizl.wtmg.mvvm.activity.MoneyTracesRecordActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.util.DateUtil
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import kotlinx.android.synthetic.main.fragment_property_outline.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PropertyOutlineFragment : BaseFragment<FragmentPropertyOutlineBinding>(R.layout.fragment_property_outline)
{
    private val menuDrawLayout: MenuDrawLayout by lazy { MenuDrawLayout(this) }

    private lateinit var polymerizeGroupAdapter: PolymerizeGroupAdapter

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter()
        rv_daily_outline.adapter = polymerizeGroupAdapter
        rv_daily_outline.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(R.dimen.global_content_padding_content)))

        initCoverImage()
    }

    override fun initData()
    {
        val date = DateUtil.Date()

        AppDatabase.getInstance().getMoneyTracesDao().obTracesByMonth(date.year, date.month).observe(this, Observer { tracesList ->

            GlobalScope.launch {

                dataBinding.monthExpenditure = tracesList.filter {
                    it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_EXPENDITURE && it.tracesCategory != AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
                }.sumByDouble { it.amonunt }

                dataBinding.monthIncome = tracesList.filter {
                    it.tracesCategory == AppConstant.MONEY_TRACES_CATEGORY_INCOME && it.tracesCategory != AppConstant.MONEY_TRACES_CATEGORY_TRANSFER
                }.sumByDouble { it.amonunt }

                val polymerizeGroupList = AccountManager.polymerizeTrancesList(tracesList)

                GlobalScope.ui { polymerizeGroupAdapter.replaceData(polymerizeGroupList) }
            }
        })

        LiveEventBus.get(EventConstant.EVENT_COVER_IMAGE_UPDATE).observe(this, Observer { initCoverImage() })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(MoneyTracesRecordActivity::class.java) }

        iv_menu.setOnClickListener {
            XPopup.Builder(requireContext()).popupPosition(PopupPosition.Left).hasStatusBarShadow(false).asCustom(menuDrawLayout).show()
        }

        iv_property_manager.setOnClickListener { LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).post(true) }

        polymerizeGroupAdapter.setOnChildItemClickListener {
            PopupUtil.showTracesDetailPopup(it.tag as MoneyTracesModel)
        }
    }

    private fun initCoverImage()
    {
        val mainPicBitmap = MainPicHandler.getMainImageBitmap()
        if (mainPicBitmap != null)
        {
            iv_main_pic.setImageBitmap(mainPicBitmap)
        }
        else
        {
            iv_main_pic.setImageResource(R.mipmap.pic_main_default)
        }
    }
}