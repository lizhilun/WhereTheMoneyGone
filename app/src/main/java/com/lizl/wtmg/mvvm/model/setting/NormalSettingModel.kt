package com.lizl.wtmg.mvvm.model.setting

class NormalSettingModel(val name: String, val icon: Int? = null, var value: String = "", val callback: (NormalSettingModel) -> Unit) : BaseSettingModel()