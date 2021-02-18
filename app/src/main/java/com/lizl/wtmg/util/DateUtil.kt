package com.lizl.wtmg.util

import java.util.*

object DateUtil
{
    class Date(var timeInMills: Long)
    {
        private val calendar = Calendar.getInstance()

        constructor() : this(System.currentTimeMillis())

        init
        {
            set(timeInMills)
        }

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
            calendar.timeInMillis = timeInMills
            year = calendar.get(Calendar.YEAR)
            month = calendar.get(Calendar.MONTH) + 1
            day = calendar.get(Calendar.DAY_OF_MONTH)
            hour = calendar.get(Calendar.HOUR_OF_DAY)
            minute = calendar.get(Calendar.MINUTE)
            second = calendar.get(Calendar.SECOND)
        }

        fun toFormatString(showSecond: Boolean = false): String
        {
            return if (showSecond)
            {
                "%d-%02d-%02d %02d:%02d:%02d".format(year, month, day, hour, minute, second)
            }
            else
            {
                "%d-%02d-%02d %02d:%02d".format(year, month, day, hour, minute)
            }
        }
    }
}