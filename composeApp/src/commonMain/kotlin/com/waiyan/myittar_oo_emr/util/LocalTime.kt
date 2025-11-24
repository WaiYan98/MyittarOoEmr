package com.waiyan.myittar_oo_emr.util

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object LocalTime {

    private val timeZone by lazy { TimeZone.currentSystemDefault() }

    @OptIn(ExperimentalTime::class)
    private val todayLocalDate
        get() = Clock.System.now().toLocalDateTime(timeZone).date

    @OptIn(ExperimentalTime::class)
    fun getCurrentTimeMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    private const val MILLIS_IN_DAY = 24 * 60 * 60 * 1000L

    @OptIn(ExperimentalTime::class)
    fun getHumanDate(timeStamp: Long): String {
        val day = timestampToDay(timeStamp).toString().padStart(2, '0')
        val month = timestampToMonth(timeStamp).toString().padStart(2,'0')
        val year = timestampToYear(timeStamp).toString()

        return "$day/$month/$year"
    }

    fun calculateDayUntil(followUpTimeStamp: Long): Long {
        val difference = followUpTimeStamp - getCurrentTimeMillis()
        return if (difference <= 0) 0 else difference / MILLIS_IN_DAY
    }

    @OptIn(ExperimentalTime::class)
    fun timeStampToLocalDateTime(timeStamp: Long): LocalDateTime {
        return Instant.fromEpochMilliseconds(timeStamp)
            .toLocalDateTime(timeZone)
    }

    @OptIn(ExperimentalTime::class)
    fun getStartOfTodayTimeStamp(): Long {
        return todayLocalDate.atStartOfDayIn(timeZone).toEpochMilliseconds()
    }

    @OptIn(ExperimentalTime::class)
    fun getEndOfTodayTimeStamp(): Long {
        return todayLocalDate.plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone).toEpochMilliseconds()
    }

    fun getCurrentMonth(): Int = todayLocalDate.month.number

    fun getCurrentYear(): Int = todayLocalDate.year

    @OptIn(ExperimentalTime::class)
    fun timestampToMonth(timeStamp: Long): Int =
        timeStampToLocalDateTime(timeStamp)
            .month.number


    @OptIn(ExperimentalTime::class)
    fun timestampToYear(timeStamp: Long): Int =
        timeStampToLocalDateTime(timeStamp)
            .year


    @OptIn(ExperimentalTime::class)
    fun timestampToDay(timeStamp: Long): Int =
        timeStampToLocalDateTime(timeStamp)
            .day

}