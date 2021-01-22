package com.lizl.wtmg.custom.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.jeremyliao.liveeventbus.LiveEventBus
import com.lizl.wtmg.R
import com.lizl.wtmg.constant.EventConstant
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.module.config.util.ConfigUtil
import com.lizl.wtmg.mvvm.adapter.SettingListAdapter
import com.lizl.wtmg.mvvm.model.setting.*
import com.lizl.wtmg.util.BiometricAuthenticationUtil
import com.lizl.wtmg.util.PopupUtil
import com.lxj.xpopup.core.DrawerPopupView
import kotlinx.android.synthetic.main.layout_drawer_menu.view.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MenuDrawLayout(private val fragment: Fragment) : DrawerPopupView(fragment.requireContext())
{
    override fun getImplLayoutId() = R.layout.layout_drawer_menu

    private val settingAdapter by lazy { SettingListAdapter(getSettingList()) }

    private val appLockItem by lazy {
        BooleanSettingModel(context.getString(R.string.app_lock_config), ConfigConstant.CONFIG_APP_LOCK_ENABLE, R.drawable.ic_baseline_lock_24, false) {
            if (it)
            {
                val password = ConfigUtil.getStringBlocking(ConfigConstant.CONFIG_APP_LOCK_PASSWORD)
                if (password.isBlank())
                {
                    PopupUtil.showSetPasswordPopup {
                        GlobalScope.launch {
                            ConfigUtil.set(ConfigConstant.CONFIG_APP_LOCK_ENABLE, true)
                            ConfigUtil.set(ConfigConstant.CONFIG_APP_LOCK_PASSWORD, it)
                        }
                    }
                }
                else
                {
                    GlobalScope.launch { ConfigUtil.set(ConfigConstant.CONFIG_APP_LOCK_ENABLE, true) }
                }
            }
            else
            {
                GlobalScope.launch { ConfigUtil.set(ConfigConstant.CONFIG_APP_LOCK_ENABLE, false) }
            }
        }
    }

    private val modifyPasswordItem by lazy {
        NormalSettingModel(context.getString(R.string.modify_app_lock_password), R.drawable.ic_baseline_modify_password_24) {
            val password = ConfigUtil.getStringBlocking(ConfigConstant.CONFIG_APP_LOCK_PASSWORD)
            PopupUtil.showModifyPasswordPopup(password) {
                GlobalScope.launch { ConfigUtil.set(ConfigConstant.CONFIG_APP_LOCK_PASSWORD, it) }
            }
        }
    }

    private val fingerprintUnlockItem by lazy {
        BooleanSettingModel(context.getString(R.string.fingerprint_unlock_config), ConfigConstant.CONFIG_FINGERPRINT_LOCK_ENABLE,
                R.drawable.ic_baseline_fingerprint_24) {}
    }

    override fun onCreate()
    {
        super.onCreate()

        rv_menu.adapter = settingAdapter

        ConfigUtil.obConfig(ConfigConstant.CONFIG_APP_LOCK_ENABLE).observe(fragment, Observer {
            settingAdapter.update(appLockItem)
            if (it !is Boolean) return@Observer

            if (it)
            {
                var index = settingAdapter.data.indexOf(appLockItem)
                settingAdapter.addData(++index, modifyPasswordItem)
                if (BiometricAuthenticationUtil.isFingerprintSupport())
                {
                    settingAdapter.addData(++index, fingerprintUnlockItem)
                }
            }
            else
            {
                settingAdapter.remove(modifyPasswordItem)
                settingAdapter.remove(fingerprintUnlockItem)
            }
        })
    }

    private fun getSettingList(): MutableList<BaseSettingModel>
    {
        return mutableListOf<BaseSettingModel>().apply {

            add(NormalSettingModel(context.getString(R.string.main_image_config), R.drawable.ic_baseline_main_pic_24) {
                LiveEventBus.get(EventConstant.EVENT_GO_TO_COVER_IMAGE_SELECTION).post(true)
            })

            add(DividerSettingModel())

            add(appLockItem)

            if (ConfigUtil.getBooleanBlocking(ConfigConstant.CONFIG_APP_LOCK_ENABLE))
            {
                add(modifyPasswordItem)
                if (BiometricAuthenticationUtil.isFingerprintSupport())
                {
                    add(fingerprintUnlockItem)
                }
            }

            add(DividerSettingModel())

            val darkModeMap = mapOf(ConfigConstant.APP_NIGHT_MODE_ON to context.getString(R.string.on),
                    ConfigConstant.APP_NIGHT_MODE_OFF to context.getString(R.string.off),
                    ConfigConstant.APP_NIGHT_MODE_FOLLOW_SYSTEM to context.getString(R.string.follow_system))

            add(StringRadioSettingModel(context.getString(R.string.dark_mode_config), ConfigConstant.CONFIG_DARK_MODE, R.drawable.ic_baseline_dark_mode_24,
                    darkModeMap) { })
        }
    }
}