package com.lizl.wtmg.mvvm.model

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

class DateModel(private val localDateTime: LocalDateTime)
{
    companion object
    {
        private const val ZONE_OFFSET_ID = "+8"
    }

    constructor() : this(LocalDateTime.now())

    constructor(year: Int, month: Int = 1, day: Int = 1, hour: Int = 0, minute: Int = 0, second: Int = 0) : this(
            LocalDateTime.of(year, month, day, hour, minute, second))

    constructor(timeInMills: Long) : this(LocalDateTime.ofInstant(Instant.ofEpochMilli(timeInMills), ZoneOffset.of(ZONE_OFFSET_ID)))

    fun getYear() = localDateTime.year
    fun getMonth() = localDateTime.monthValue
    fun getDay() = localDateTime.dayOfMonth
    fun getHour() = localDateTime.hour
    fun getMinute() = localDateTime.minute
    fun getSecond() = localDateTime.second

    fun getTimeInMills() = localDateTime.toInstant(ZoneOffset.of(ZONE_OFFSET_ID)).toEpochMilli()

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