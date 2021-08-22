package com.lizl.wtmg.custom.other

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.TimeUtils
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.custom.function.launchDefault
import com.lizl.wtmg.module.skin.util.SkinUtil
import com.lizl.wtmg.mvvm.activity.LockActivity
import com.lizl.wtmg.util.ActivityUtil

object CustomActivityLifecycle : Application.ActivityLifecycleCallbacks
{
    var isFromActivityResult = false

    private var lastAppStopTime = 0L

    init
    {
        AppUtils.registerAppStatusChangedListener(this, object : Utils.OnAppStatusChangedListener
        {
            override fun onForeground()
            {
                if (isFromActivityResult || ActivityUtils.getTopActivity() is LockActivity)
                {
                    return
                }
                if (TimeUtils.getNowMills() - lastAppStopTime <= 30_000)
                {
                    return
                }
                ActivityUtil.turnToActivity(LockActivity::class.java)
            }

            override fun onBackground()
            {
                lastAppStopTime = TimeUtils.getNowMills()
            }
        })
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)
    {
        launchDefault { SkinUtil.updateStatusBarLightMode(activity) }
    }

    override fun onActivityStarted(activity: Activity)
    {
    }

    override fun onActivityResumed(activity: Activity)
    {
    }

    override fun onActivityPaused(activity: Activity)
    {
    }

    override fun onActivityStopped(activity: Activity)
    {
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle)
    {
    }

    override fun onActivityDestroyed(activity: Activity)
    {
    }
}