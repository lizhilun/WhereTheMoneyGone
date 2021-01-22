package com.lizl.wtmg.custom.other

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.lizl.wtmg.module.skin.util.SkinUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

object CustomActivityLifecycle : Application.ActivityLifecycleCallbacks
{
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