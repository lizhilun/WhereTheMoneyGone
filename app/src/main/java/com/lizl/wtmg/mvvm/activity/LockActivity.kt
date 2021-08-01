package com.lizl.wtmg.mvvm.activity

import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.custom.function.ui
import com.lizl.wtmg.databinding.ActivityLockBinding
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.module.config.util.ConfigUtil
import com.lizl.wtmg.mvvm.adapter.NumberKeyAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.util.BiometricAuthenticationUtil
import kotlinx.coroutines.launch
import java.lang.StringBuilder

class LockActivity : BaseActivity<ActivityLockBinding>(R.layout.activity_lock)
{
    private val input = StringBuilder()

    override fun initView()
    {
        dataBinding.rvNumberKeyList.adapter = NumberKeyAdapter().apply {

            setNewData(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "E", "0", "R").toMutableList())

            setOnItemClickListener(true) { key ->
                when (key)
                {
                    "E"  -> onBackPressed()
                    "R"  -> input.clear()
                    else ->
                    {
                        input.append(key)
                        if (input.toString() == ConfigUtil.getStringBlocking(ConfigConstant.CONFIG_APP_LOCK_PASSWORD))
                        {
                            onUnlock()
                        }
                    }
                }
            }
        }
    }

    override fun initListener()
    {
        dataBinding.ivFingerprint.setOnClickListener { startFingerprintAuthentication() }
    }

    override fun onStart()
    {
        super.onStart()
        input.clear()

        lifecycleScope.launch {
            val appLockEnable = ConfigUtil.getBooleanBlocking(ConfigConstant.CONFIG_APP_LOCK_ENABLE)
            ui {
                if (!appLockEnable)
                {
                    onUnlock()
                }
            }

            val fingerprintEnable =
                    BiometricAuthenticationUtil.isFingerprintSupport() && ConfigUtil.getBooleanBlocking(ConfigConstant.CONFIG_FINGERPRINT_LOCK_ENABLE)
            ui {
                dataBinding.ivFingerprint.isVisible = fingerprintEnable
                if (appLockEnable && fingerprintEnable)
                {
                    startFingerprintAuthentication()
                }
            }
        }
    }

    private fun onUnlock()
    {
        val lastActivity = ActivityUtil.getLastActivity()
        if (lastActivity != null)
        {
            ActivityUtils.startActivity(lastActivity.javaClass)
        }
        else
        {

            ActivityUtil.turnToActivity(MainActivity::class.java)
        }
    }

    override fun onBackPressed()
    {
        ActivityUtils.startHomeActivity()
    }

    private fun startFingerprintAuthentication()
    {
        BiometricAuthenticationUtil.authenticate { result, error ->
            Log.d(TAG, "startFingerprintAuthentication() called with: result = [$result], error = [$error]")
            if (result) onUnlock()
        }
    }
}