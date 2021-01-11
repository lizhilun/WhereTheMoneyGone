package com.lizl.wtmg.mvvm.model.setting

abstract class RadioSettingModel<T, T2>(open val name: String, open val key: String, open val icon: Int? = null, open val radioMap: Map<T, String>,
                                        open val callback: ((bean: T2) -> Unit)? = null) : BaseSettingModel()
{
    abstract fun getValue(): T

    abstract suspend fun saveValue(value: T)
}