package com.lizl.wtmg.mvvm.activity

import android.util.Log
import androidx.core.view.isVisible
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.databinding.ActivityLockBinding
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.module.config.util.ConfigUtil
import com.lizl.wtmg.mvvm.adapter.NumberKeyAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.util.ActivityUtil
import com.lizl.wtmg.util.BiometricAuthenticationUtil
import kotlinx.android.synthetic.main.activity_lock.*
import java.lang.StringBuilder

class LockActivity : BaseActivity<ActivityLockBinding>(R.layout.activity_lock)
{
    private val input = StringBuilder()

    override fun initView()
    {
        if (!ConfigUtil.getBooleanBlocking(ConfigConstant.CONFIG_APP_LOCK_ENABLE))
        {
            onUnlock()
            return
        }

        iv_fingerprint.isVisible = BiometricAuthenticationUtil.isFingerprintSupport()
                                   && ConfigUtil.getBooleanBlocking(ConfigConstant.CONFIG_FINGERPRINT_LOCK_ENABLE)

        val password = ConfigUtil.getStringBlocking(ConfigConstant.CONFIG_APP_LOCK_PASSWORD)

        rv_number_key_list.adapter = NumberKeyAdapter().apply {

            setNewData(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "E", "0", "R").toMutableList())

            setOnItemClickListener(true) { key ->
                when (key)
                {
                    "E" -> onBackPressed()
                    "R" -> input.clear()
                    else ->
                    {
                        input.append(key)
                        if (input.toString() == password)
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
        iv_fingerprint.setOnClickListener { startFingerprintAuthentication() }
    }

    override fun onStart()
    {
        super.onStart()
        input.clear()

        if (iv_fingerprint.isVisible)
        {
            startFingerprintAuthentication()
        }
    }

    private fun onUnlock()
    {
        if (ActivityUtils.getActivityList().size == 1)
        {
            ActivityUtil.turnToActivity(MainActivity::class.java)
        }
        finish()
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