package com.lizl.wtmg.custom.view

import android.content.Context
import com.lizl.wtmg.R
import com.lizl.wtmg.mvvm.adapter.SettingListAdapter
import com.lizl.wtmg.mvvm.model.setting.NormalSettingModel
import com.lxj.xpopup.core.DrawerPopupView
import kotlinx.android.synthetic.main.layout_drawer_menu.view.*

class MenuDrawLayout(context: Context) : DrawerPopupView(context)
{
    override fun getImplLayoutId() = R.layout.layout_drawer_menu

    private val settingAdapter = SettingListAdapter()

    private lateinit var clearCacheModel: NormalSettingModel

    override fun onCreate()
    {
        super.onCreate()

        rv_menu.adapter = settingAdapter
    }
}