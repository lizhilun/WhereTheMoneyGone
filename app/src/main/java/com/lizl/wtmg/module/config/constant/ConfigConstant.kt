package com.lizl.wtmg.module.config.constant

import com.lizl.wtmg.module.config.annotation.BooleanConfig
import com.lizl.wtmg.module.config.annotation.StringConfig

object ConfigConstant {
    const val APP_NIGHT_MODE_ON = "APP_NIGHT_MODE_ON"
    const val APP_NIGHT_MODE_OFF = "APP_NIGHT_MODE_OFF"
    const val APP_NIGHT_MODE_FOLLOW_SYSTEM = "APP_NIGHT_MODE_FOLLOW_SYSTEM"

    //暗黑模式
    @StringConfig(APP_NIGHT_MODE_FOLLOW_SYSTEM)
    const val CONFIG_DARK_MODE = "CONFIG_DARK_MODE"

    //应用锁
    @BooleanConfig(false)
    const val CONFIG_APP_LOCK_ENABLE = "CONFIG_APP_LOCK_ENABLE"

    //指纹识别
    @BooleanConfig(false)
    const val CONFIG_FINGERPRINT_LOCK_ENABLE = "CONFIG_FINGERPRINT_LOCK_ENABLE"

    //应用锁密码
    @StringConfig("")
    const val CONFIG_APP_LOCK_PASSWORD = "CONFIG_APP_LOCK_PASSWORD"
}