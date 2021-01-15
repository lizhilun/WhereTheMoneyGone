package com.lizl.wtmg.mvvm.fragment

import com.blankj.utilcode.util.ActivityUtils
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.custom.view.MenuDrawLayout
import com.lizl.wtmg.custom.view.titlebar.TitleBarBtnBean
import com.lizl.wtmg.databinding.FragmentPropertyOutlineBinding
import com.lizl.wtmg.mvvm.activity.MoneyRecordActivity
import com.lizl.wtmg.mvvm.base.BaseFragment
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import kotlinx.android.synthetic.main.fragment_property_outline.*

class PropertyOutlineFragment : BaseFragment<FragmentPropertyOutlineBinding>(R.layout.fragment_property_outline)
{
    private val menuDrawLayout: MenuDrawLayout by lazy { MenuDrawLayout(requireContext()) }

    override fun initView()
    {
        ctb_title.setActionList(mutableListOf<TitleBarBtnBean.BaseBtnBean>().apply {
            add(TitleBarBtnBean.ImageBtnBean(R.drawable.ic_baseline_property_24) {
                LiveEventBus.get(EventConstant.EVENT_GO_TO_PROPERTY_MANAGER_VIEW).post(true)
            })
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(MoneyRecordActivity::class.java) }

        ctb_title.setOnBackBtnClickListener {
            XPopup.Builder(requireContext()).popupPosition(PopupPosition.Left).hasStatusBarShadow(false).asCustom(menuDrawLayout).show()
        }
    }
}