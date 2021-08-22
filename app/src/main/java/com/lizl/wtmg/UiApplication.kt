package com.lizl.wtmg

import android.app.Application
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import android.util.Log
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner
import com.blankj.utilcode.util.AppUtils
import com.blankj.utilcode.util.FileIOUtils
import com.blankj.utilcode.util.PathUtils
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.custom.function.launchDefault
import com.lizl.wtmg.custom.other.CustomActivityLifecycle
import com.lizl.wtmg.module.backup.BackupUtil
import com.lizl.wtmg.module.config.util.ConfigUtil
import com.lizl.wtmg.module.skin.util.SkinUtil
import com.lizl.wtmg.mvvm.activity.MoneyTracesRecordActivity

class UiApplication : Application(), Thread.UncaughtExceptionHandler, ViewModelStoreOwner
{
    private val TAG = "UiApplication"

    private val exceptionLogFilePath: String by lazy { PathUtils.getExternalAppFilesPath() + "/exception.log" }

    private lateinit var viewModelStore: ViewModelStore

    companion object
    {
        lateinit var instance: UiApplication
    }

    override fun onCreate()
    {
        super.onCreate()
        viewModelStore = ViewModelStore()
        instance = this

        Utils.init(this)

        ConfigUtil.initConfig(this)

        SkinUtil.init(this)

        registerActivityLifecycleCallbacks(CustomActivityLifecycle)

        setupShortcuts()

        BackupUtil.init()

        Thread.setDefaultUncaughtExceptionHandler(this)
    }

    override fun getViewModelStore() = viewModelStore

    override fun uncaughtException(t: Thread, e: Throwable)
    {
        val exceptionInfo = Log.getStackTraceString(e)
        Log.d(TAG, "uncaughtException() called with: t = [$t], e = [$exceptionInfo]")
        launchDefault {
            FileIOUtils.writeFileFromString(exceptionLogFilePath, exceptionInfo, true)
            AppUtils.relaunchApp(true)
        }
    }

    private fun setupShortcuts()
    {
        val shortcutManager = getSystemService(ShortcutManager::class.java)

        val intent = Intent(this, MoneyTracesRecordActivity::class.java)
        intent.action = Intent.ACTION_VIEW
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        val shortcutInfoList = mutableListOf<ShortcutInfo>().apply {
            add(ShortcutInfo.Builder(this@UiApplication, "new add")
                    .setShortLabel(getString(R.string.record_one))
                    .setLongLabel(getString(R.string.record_one))
                    .setIcon(Icon.createWithResource(this@UiApplication, R.drawable.ic_add_round))
                    .setIntent(intent)
                    .build())
        }

        shortcutManager.dynamicShortcuts = shortcutInfoList
    }
}