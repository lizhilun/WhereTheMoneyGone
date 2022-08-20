package com.lizl.wtmg.module.skin.util

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.ColorStateList
import android.content.res.Configuration
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.BarUtils
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.custom.function.launchDefault
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.module.config.util.ConfigUtil
import kotlinx.coroutines.runBlocking
import skin.support.SkinCompatManager
import skin.support.SkinCompatManager.SkinLoaderListener
import skin.support.app.SkinAppCompatViewInflater
import skin.support.app.SkinCardViewInflater
import skin.support.constraint.app.SkinConstraintViewInflater
import skin.support.content.res.SkinCompatResources
import skin.support.design.app.SkinMaterialViewInflater

object SkinUtil {
    private val TAG = "SkinUtil"

    private const val SKIN_DARK = "dark"

    fun init(application: Application) {
        SkinCompatManager.withoutActivity(application).addInflater(SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .addInflater(SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
                .setSkinWindowBackgroundEnable(true)               // windowBg换肤

        runBlocking { loadSkin() }

        ConfigUtil.obConfig(ConfigConstant.CONFIG_DARK_MODE).observeForever { launchDefault { loadSkin() } }
    }

    suspend fun loadSkin() {
        val skinLoadListener = object : SkinLoaderListener {
            override fun onSuccess() {
                val topActivity = ActivityUtils.getTopActivity() ?: return
                launchDefault { updateStatusBarLightMode(topActivity) }
            }

            override fun onFailed(errMsg: String?) {
            }

            override fun onStart() {
            }
        }

        if (isNightModeOn()) {
            SkinCompatManager.getInstance().loadSkin(SKIN_DARK, skinLoadListener, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
        } else {
            SkinCompatManager.getInstance().loadSkin("", skinLoadListener, SkinCompatManager.SKIN_LOADER_STRATEGY_BUILD_IN)
        }
    }

    fun getColor(context: Context, colorResId: Int) = SkinCompatResources.getColor(context, colorResId)

    fun getColorStateList(context: Context, colorResId: Int): ColorStateList = SkinCompatResources.getColorStateList(context, colorResId)

    suspend fun updateStatusBarLightMode(activity: Activity) {
        val isDarkMode = isNightModeOn()
        Utils.runOnUiThread { BarUtils.setStatusBarLightMode(activity, !isDarkMode) }
    }

    private suspend fun isNightModeOn(): Boolean {
        return when (ConfigUtil.getString(ConfigConstant.CONFIG_DARK_MODE)) {
            ConfigConstant.APP_NIGHT_MODE_ON -> true
            ConfigConstant.APP_NIGHT_MODE_OFF -> false
            ConfigConstant.APP_NIGHT_MODE_FOLLOW_SYSTEM -> isSystemDarkMode()
            else -> false
        }
    }

    private fun isSystemDarkMode(): Boolean {
        return Utils.getApp().resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES
    }
}