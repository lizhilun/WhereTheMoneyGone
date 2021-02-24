package com.lizl.wtmg.custom.other

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.blankj.utilcode.util.ActivityUtils
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.module.skin.util.SkinUtil
import com.lizl.wtmg.mvvm.activity.LockActivity
import com.lizl.wtmg.util.ActivityUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object CustomActivityLifecycle : Application.ActivityLifecycleCallbacks
{
    var isFromActivityResult = false

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
                ActivityUtil.turnToActivity(LockActivity::class.java)
            }

            override fun onBackground()
            {
            }
        })
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)
    {
        GlobalScope.launch { SkinUtil.updateStatusBarLightMode(activity) }
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