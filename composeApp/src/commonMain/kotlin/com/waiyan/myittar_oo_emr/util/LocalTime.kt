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

    const val DAY_IN_MILLIS = 24 * 60 * 60 * 1000L

    @OptIn(ExperimentalTime::class)
    fun getHumanDate(timeStamp: Long): String {
        val day = timestampToDay(timeStamp).toString().padStart(2, '0')
        val month = timestampToMonth(timeStamp).toString().padStart(2,'0')
        val year = timestampToYear(timeStamp).toString()

        return "$day/$month/$year"
    }

    @OptIn(ExperimentalTime::class)
    fun getHumanTime(timeStamp: Long): String {
        val localDateTime = timeStampToLocalDateTime(timeStamp)
        val hour24 = localDateTime.hour
        val amPm = if (hour24 < 12) "AM" else "PM"
        val hour12 = when {
            hour24 == 0 -> 12
            hour24 > 12 -> hour24 - 12
            else -> hour24
        }
        val hourString = hour12.toString().padStart(2, '0')
        val minute = localDateTime.minute.toString().padStart(2, '0')
        return "$hourString:$minute $amPm"
    }

    fun calculateDayUntil(followUpTimeStamp: Long): Long {
        val difference = followUpTimeStamp - getCurrentTimeMillis()
        return if (difference <= 0) 0 else difference / DAY_IN_MILLIS
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

    @OptIn(ExperimentalTime::class)
    fun asStartOfDay(timeStamp: Long): Long {
        return timeStampToLocalDateTime(timeStamp)
            .date
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds()
    }

    @OptIn(ExperimentalTime::class)
    fun asEndOfDay(timeStamp: Long): Long {
        return timeStampToLocalDateTime(timeStamp)
            .date
            .plus(1, DateTimeUnit.DAY)
            .atStartOfDayIn(timeZone)
            .toEpochMilliseconds() - 1
    }

}