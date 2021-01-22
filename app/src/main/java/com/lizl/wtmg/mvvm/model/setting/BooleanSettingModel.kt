package com.lizl.wtmg.mvvm.model.setting

import com.lizl.wtmg.module.config.util.ConfigUtil
import kotlinx.coroutines.runBlocking

class BooleanSettingModel(val name: String, val key: String, val icon: Int? = null, val autoSave: Boolean = true, val callback: ((Boolean) -> Unit)? = null) :
    BaseSettingModel()
{
    fun getValue() = runBlocking { ConfigUtil.getBoolean(key) }

    suspend fun saveValue(value: Boolean) = ConfigUtil.set(key, value)
}