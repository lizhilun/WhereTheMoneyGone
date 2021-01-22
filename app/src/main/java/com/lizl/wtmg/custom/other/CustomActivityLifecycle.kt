package com.lizl.wtmg.custom.other

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lizl.wtmg.module.skin.util.SkinUtil
import com.lizl.wtmg.mvvm.activity.LockActivity
import com.lizl.wtmg.util.ActivityUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object CustomActivityLifecycle : Application.ActivityLifecycleCallbacks
{
    private var startStatusActivityCount = 0

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?)
    {
        GlobalScope.launch { SkinUtil.updateStatusBarLightMode(activity) }
    }

    override fun onActivityStarted(activity: Activity)
    {
        startStatusActivityCount++
        if (startStatusActivityCount == 1 && activity !is LockActivity)
        {
            ActivityUtil.turnToActivity(LockActivity::class.java)
        }
    }

    override fun onActivityResumed(activity: Activity)
    {
    }

    override fun onActivityPaused(activity: Activity)
    {
    }

    override fun onActivityStopped(activity: Activity)
    {
        startStatusActivityCount--
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle)
    {
    }

    override fun onActivityDestroyed(activity: Activity)
    {
    }
}