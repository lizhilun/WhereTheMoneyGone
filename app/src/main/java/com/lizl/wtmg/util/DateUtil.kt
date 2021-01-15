package com.lizl.wtmg.util

import java.util.*

object DateUtil
{
    class Date(val time: Long)
    {
        constructor() : this(System.currentTimeMillis())

        var year = 0
        var month = 0
        var day = 0
        var hour = 0
        var minute = 0
        var second = 0

        init
        {
            try
            {
                val calendar = Calendar.getInstance()
                calendar.time = java.util.Date(time)
                year = calendar.get(Calendar.YEAR)
                month = calendar.get(Calendar.MONTH) + 1
                day = calendar.get(Calendar.DAY_OF_MONTH)
                hour = calendar.get(Calendar.HOUR_OF_DAY)
                minute = calendar.get(Calendar.MINUTE)
                second = calendar.get(Calendar.SECOND)
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
    }
}