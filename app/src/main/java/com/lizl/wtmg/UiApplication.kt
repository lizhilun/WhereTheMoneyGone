package com.lizl.wtmg

import android.app.Application
import android.util.Log
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.module.config.util.ConfigUtil
import com.lizl.wtmg.module.skin.util.SkinUtil
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UiApplication : Application(), Thread.UncaughtExceptionHandler
{
    private val TAG = "UiApplication"

    private val exceptionLogFilePath: String by lazy { PathUtils.getExternalAppFilesPath() + "/exception.log" }

    override fun onCreate()
    {
        super.onCreate()

        Utils.init(this)

        ConfigUtil.initConfig(this)

        SkinUtil.init(this)

        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun uncaughtException(t: Thread, e: Throwable)
    {
        val exceptionInfo = Log.getStackTraceString(e)
        Log.d(TAG, "uncaughtException() called with: t = [$t], e = [$exceptionInfo]")
//        GlobalScope.launch {
//            FileIOUtils.writeFileFromString(exceptionLogFilePath, exceptionInfo, true)
//            AppUtils.relaunchApp(true)
//        }
    }
}