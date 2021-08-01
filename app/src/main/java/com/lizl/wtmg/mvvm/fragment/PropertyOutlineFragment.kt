package com.lizl.wtmg.mvvm.fragment

import android.util.Log
import androidx.lifecycle.*
import com.blankj.utilcode.util.ActivityUtils
import com.chad.library.adapter.base.BaseQuickAdapter.AnimationType
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.custom.view.ListDividerItemDecoration
import com.lizl.wtmg.custom.view.MenuDrawLayout
import com.lizl.wtmg.databinding.FragmentPropertyOutlineBinding
import com.lizl.wtmg.db.model.MoneyTracesModel
import com.lizl.wtmg.module.mainpic.MainPicHandler
import com.lizl.wtmg.mvvm.activity.MoneyTracesRecordActivity
import com.lizl.wtmg.mvvm.adapter.PolymerizeGroupAdapter
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lizl.wtmg.custom.popup.PopupUtil
import com.lizl.wtmg.mvvm.activity.TracesSearchActivity
import com.lizl.wtmg.mvvm.model.DateModel
import com.lizl.wtmg.mvvm.viewmodel.TracesViewModel
import com.lizl.wtmg.util.ActivityUtil
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition

class PropertyOutlineFragment : BaseFragment<FragmentPropertyOutlineBinding>(R.layout.fragment_property_outline)
{
    private val menuDrawLayout: MenuDrawLayout by lazy { MenuDrawLayout(this) }
    private val tracesViewModel by lazy { createViewModel(TracesViewModel::class.java) }

    private lateinit var polymerizeGroupAdapter: PolymerizeGroupAdapter

    override fun initView()
    {
        polymerizeGroupAdapter = PolymerizeGroupAdapter().apply {
            animationEnable = true
            setAnimationWithDefault(AnimationType.SlideInLeft)
        }
        dataBinding.rvDailyOutline.adapter = polymerizeGroupAdapter
        dataBinding.rvDailyOutline.addItemDecoration(ListDividerItemDecoration(resources.getDimensionPixelSize(R.dimen.global_content_padding_content)))

        initCoverImage()
    }

    override fun initData()
    {
        val now = DateModel()
        dataBinding.tvMonth.text = "%d.%02d".format(now.getYear(), now.getMonth())
        showMonthOutline(now.getYear(), now.getMonth())

        LiveEventBus.get(EventConstant.EVENT_COVER_IMAGE_UPDATE).observe(this, { initCoverImage() })

        tracesViewModel.obPolymerizedTraces().observe(this, Observer {
            polymerizeGroupAdapter.setDiffNewData(it)
        })

        tracesViewModel.obMonthTracesOutline().observe(this, Observer {
            dataBinding.monthExpenditure = it.monthExpenditure
            dataBinding.monthIncome = it.monthIncome
        })
    }

    override fun initListener()
    {
        dataBinding.fabAdd.setOnClickListener { ActivityUtils.startActivity(MoneyTracesRecordActivity::class.java) }

        dataBinding.ivMenu.setOnClickListener {
            XPopup.Builder(requireContext()).popupPosition(PopupPosition.Left).hasStatusBarShadow(false).asCustom(menuDrawLayout).show()
        }

        dataBinding.ivPropertyManager.setOnClickListener { LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).post(true) }

        dataBinding.ivSearch.setOnClickListener { ActivityUtil.turnToActivity(TracesSearchActivity::class.java) }

        polymerizeGroupAdapter.setOnChildItemClickListener {
            PopupUtil.showTracesDetailPopup(it.tag as MoneyTracesModel)
        }

        dataBinding.tvMonth.setOnClickListener {
            PopupUtil.showMonthSelectPopup { year, month ->
                dataBinding.tvMonth.text = "%d.%02d".format(year, month)
                showMonthOutline(year, month)
            }
        }
    }

    private fun showMonthOutline(year: Int, month: Int)
    {
        tracesViewModel.setYearAndMonth(year, month)
    }

    private fun initCoverImage()
    {
        val mainPicBitmap = MainPicHandler.getMainImageBitmap()
        if (mainPicBitmap != null)
        {
            dataBinding.ivMainPic.setImageBitmap(mainPicBitmap)
        }
        else
        {
            dataBinding.ivMainPic.setImageResource(R.mipmap.pic_main_default)
        }
    }
}