package com.lizl.wtmg.util

import com.blankj.utilcode.util.TimeUtils


object DateUtil {
    fun getDayCountInMonth(year: Int, month: Int): Int {
        return when (month) {
            2 -> if (TimeUtils.isLeapYear(year)) 29 else 28
            1, 3, 5, 7, 8, 10, 12 -> 31
            else -> 30
        }
    }
}