package com.lizl.wtmg.mvvm.activity

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.view.MenuDrawLayout
import com.lizl.wtmg.custom.view.titlebar.TitleBarBtnBean
import com.lizl.wtmg.databinding.ActivityMainBinding
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.enums.PopupPosition
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main)
{
    private val menuDrawLayout: MenuDrawLayout by lazy { MenuDrawLayout(this) }

    override fun initView()
    {
        ctb_title.setActionList(mutableListOf<TitleBarBtnBean.BaseBtnBean>().apply {
            add(TitleBarBtnBean.ImageBtnBean(R.drawable.ic_baseline_property_24) {
                ActivityUtils.startActivity(PropertyManagerActivity::class.java)
            })
        })
    }

    override fun initListener()
    {
        fab_add.setOnClickListener { ActivityUtils.startActivity(MoneyRecordActivity::class.java) }

        ctb_title.setOnBackBtnClickListener {
            XPopup.Builder(this).popupPosition(PopupPosition.Left).hasStatusBarShadow(false).asCustom(menuDrawLayout).show()
        }
    }
}