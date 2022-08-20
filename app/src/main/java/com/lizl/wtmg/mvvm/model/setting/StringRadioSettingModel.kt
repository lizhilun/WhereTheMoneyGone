package com.lizl.wtmg.mvvm.model.setting

import com.lizl.wtmg.module.config.util.ConfigUtil
import kotlinx.coroutines.runBlocking

class StringRadioSettingModel(override val name: String, override val key: String, override val icon: Int? = null, override val radioMap: Map<String, String>,
                              override val callback: (StringRadioSettingModel) -> Unit) :
        RadioSettingModel<String, StringRadioSettingModel>(name, key, icon, radioMap, callback) {
    override fun getValue() = runBlocking { ConfigUtil.getString(key) }

    override suspend fun saveValue(value: String) = ConfigUtil.set(key, value)
}