package com.lizl.wtmg.util

import android.app.Activity
import android.content.Intent
import com.blankj.utilcode.util.ActivityUtils
import com.lizl.wtmg.custom.other.CustomActivityLifecycle
import java.io.Serializable

object ActivityUtil
{
    fun turnToActivity(cls: Class<out Activity>, vararg extraList: Pair<String, Any>)
    {
        ActivityUtils.finishActivity(cls)
        val topActivity = ActivityUtils.getTopActivity() ?: return
        val intent = Intent(topActivity, cls)

        extraList.forEach {
            when (it.second)
            {
                is Int -> intent.putExtra(it.first, it.second as Int)
                is String -> intent.putExtra(it.first, it.second as String)
                is Boolean -> intent.putExtra(it.first, it.second as Boolean)
                is ArrayList<*> -> intent.putExtra(it.first, it.second as ArrayList<*>)
                is Serializable -> intent.putExtra(it.first, it.second as Serializable)
            }
        }

        topActivity.startActivity(intent)
    }
}