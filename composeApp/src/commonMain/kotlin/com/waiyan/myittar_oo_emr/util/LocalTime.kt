package com.waiyan.myittar_oo_emr.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

object LocalTime {

    @OptIn(ExperimentalTime::class)
    fun getCurrentTimeMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    @OptIn(ExperimentalTime::class)
    fun getHumanDate(timeStamp: Long): String {
        val localDateTime = Instant.fromEpochMilliseconds(timeStamp)
            .toLocalDateTime(TimeZone.currentSystemDefault())
        val day = localDateTime.day
        val month = localDateTime.month.number
        val year = localDateTime.year

        return "$day/$month/$year"
    }

}