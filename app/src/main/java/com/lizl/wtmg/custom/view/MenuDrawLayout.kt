package com.lizl.wtmg.custom.view

import android.content.Context
import com.lizl.wtmg.R
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.mvvm.adapter.SettingListAdapter
import com.lizl.wtmg.mvvm.model.setting.*
import com.lxj.xpopup.core.DrawerPopupView
import kotlinx.android.synthetic.main.layout_drawer_menu.view.*

class MenuDrawLayout(context: Context) : DrawerPopupView(context)
{
    override fun getImplLayoutId() = R.layout.layout_drawer_menu

    private val settingAdapter by lazy { SettingListAdapter(getSettingList()) }

    override fun onCreate()
    {
        super.onCreate()

        rv_menu.adapter = settingAdapter
    }

    private fun getSettingList(): MutableList<BaseSettingModel>
    {
        return mutableListOf<BaseSettingModel>().apply {

            val darkModeMap = mapOf(ConfigConstant.APP_NIGHT_MODE_ON to context.getString(R.string.on),
                    ConfigConstant.APP_NIGHT_MODE_OFF to context.getString(R.string.off),
                    ConfigConstant.APP_NIGHT_MODE_FOLLOW_SYSTEM to context.getString(R.string.follow_system))

            add(StringRadioSettingModel(context.getString(R.string.dark_mode_config), ConfigConstant.CONFIG_DARK_MODE, R.drawable.ic_baseline_dark_mode_24,
                    darkModeMap) { })
        }
    }
}