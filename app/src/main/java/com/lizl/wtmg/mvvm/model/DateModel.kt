package com.lizl.wtmg.mvvm.model

import com.lizl.wtmg.util.DateUtil
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DateModel(private val localDateTime: LocalDateTime)
{
    companion object
    {
        private const val ZONE_OFFSET_ID = "+8"

        fun dayStart(year: Int, month: Int = 1, day: Int = 1) = DateModel(year, month, day)

        fun dayEnd(year: Int, month: Int = 1, day: Int = 1) = DateModel(year, month, day, 23, 59, 59, 999_999_999)

        fun monthStart(year: Int, month: Int = 1) = dayStart(year, month, 1)

        fun monthEnd(year: Int, month: Int) = dayEnd(year, month, DateUtil.getDayCountInMonth(year, month))

        fun yearStart(year: Int) = monthStart(year, 1)

        fun yearEnd(year: Int) = monthEnd(year, 12)
    }

    constructor() : this(LocalDateTime.now())

    constructor(year: Int, month: Int = 1, day: Int = 1, hour: Int = 0, minute: Int = 0, second: Int = 0, nanoOfSecond: Int = 0) : this(
            LocalDateTime.of(year, month, day, hour, minute, second, nanoOfSecond))

    constructor(timeInMills: Long) : this(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMills), ZoneOffset.of(ZONE_OFFSET_ID)))

    fun getYear() = localDateTime.year
    fun getMonth() = localDateTime.monthValue
    fun getDay() = localDateTime.dayOfMonth
    fun getHour() = localDateTime.hour
    fun getMinute() = localDateTime.minute
    fun getSecond() = localDateTime.second

    fun getTimeInMills() = localDateTime.toInstant(ZoneOffset.of(ZONE_OFFSET_ID)).toEpochMilli()

    fun minusMonth(months: Long) = DateModel(localDateTime.minusMonths(months))

    fun toFormatString(showSecond: Boolean = false): String
    {
        return if (showSecond)
        {
            "%d-%02d-%02d %02d:%02d:%02d".format(getYear(), getMonth(), getDay(), getHour(), getMinute(), getSecond())
        }
        else
        {
            "%d-%02d-%02d %02d:%02d".format(getYear(), getMonth(), getDay(), getHour(), getMinute())
        }
    }
}