package com.lizl.wtmg.util

import java.util.*

object DateUtil
{
    class Date(var timeInMills: Long)
    {
        constructor() : this(System.currentTimeMillis())

        private val calendar = Calendar.getInstance()

        var year = 0
        var month = 0
        var day = 0
        var hour = 0
        var minute = 0
        var second = 0

        fun set(year: Int, month: Int, day: Int, hour: Int, minute: Int, second: Int)
        {
            this.year = year
            this.month = month
            this.day = day
            this.hour = day
            this.minute = day
            this.second = second

            calendar.set(year, month - 1, day, hour, minute, second)
            timeInMills = calendar.timeInMillis
        }

        fun set(timeInMills: Long)
        {
            this.timeInMills = timeInMills
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = timeInMills
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH) + 1
            day = calendar.get(Calendar.DAY_OF_MONTH)
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            second = calendar.get(Calendar.SECOND)
        }

        init
        {
            set(timeInMills)
        }
    }
}