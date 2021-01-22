package com.lizl.wtmg.mvvm.activity

import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.R
import com.lizl.wtmg.custom.function.setOnItemClickListener
import com.lizl.wtmg.databinding.ActivityLockBinding
import com.lizl.wtmg.module.config.constant.ConfigConstant
import com.lizl.wtmg.module.config.util.ConfigUtil
import com.lizl.wtmg.mvvm.adapter.NumberKeyAdapter
import com.lizl.wtmg.mvvm.base.BaseActivity
import com.lizl.wtmg.util.ActivityUtil
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

        val password = ConfigUtil.getStringBlocking(ConfigConstant.CONFIG_APP_LOCK_PASSWORD)

        rv_number_key_list.adapter = NumberKeyAdapter().apply {

            setNewData(listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "E", "0", "R").toMutableList())

            setOnItemClickListener { key ->
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

    override fun onResume()
    {
        super.onResume()
        input.clear()
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
}