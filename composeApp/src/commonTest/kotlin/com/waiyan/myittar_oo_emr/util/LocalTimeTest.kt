package com.waiyan.myittar_oo_emr.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.number
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class LocalTimeTest {

    // A fixed timestamp for a known date: 2023-10-27 10:00:00 UTC
    private val testTimestamp = 1698397200000L

    @Test
    fun `getCurrentTimeMillis returns a valid timestamp`() {
        val currentTime = LocalTime.getCurrentTimeMillis()
        assertTrue(currentTime > 0)
    }

    @Test
    fun `getHumanDate returns correct date string`() {
        val expectedDate = "27/10/2023"
        val actualDate = LocalTime.getHumanDate(testTimestamp)
        assertEquals(expectedDate, actualDate)
    }

    @Test
    fun `calculateDayUntil returns correct number of days`() {
        val futureTimestamp = LocalTime.getCurrentTimeMillis() + 3 * 24 * 60 * 60 * 1000L
        val daysUntil = LocalTime.calculateDayUntil(futureTimestamp)
        assertTrue(daysUntil in 2..3) // Check for a range to account for execution time

        val pastTimestamp = LocalTime.getCurrentTimeMillis() - 24 * 60 * 60 * 1000L
        val daysUntilPast = LocalTime.calculateDayUntil(pastTimestamp)
        assertEquals(0, daysUntilPast)
    }

    @Test
    fun `timeStampToLocalDateTime returns correct LocalDateTime`() {
        val localDateTime = LocalTime.timeStampToLocalDateTime(testTimestamp)
        // Note: The assertion depends on the system's timezone. This might need adjustment.
        // Assuming UTC for this test's purpose, but it will convert to the local timezone.
        val expectedLocalDateTime = LocalDateTime(2023, 10, 27, 10, 0, 0)
        val actualLocalDateTime = LocalTime.timeStampToLocalDateTime(testTimestamp)

        assertEquals(expectedLocalDateTime.year, actualLocalDateTime.year)
        assertEquals(expectedLocalDateTime.month.number, actualLocalDateTime.month.number)
        assertEquals(expectedLocalDateTime.day, actualLocalDateTime.day)
    }

    @Test
    fun `getStartOfTodayTimeStamp returns a timestamp at the beginning of the day`() {
        val startOfToday = LocalTime.getStartOfTodayTimeStamp()
        val localDateTime = LocalTime.timeStampToLocalDateTime(startOfToday)
        assertEquals(0, localDateTime.hour)
        assertEquals(0, localDateTime.minute)
        assertEquals(0, localDateTime.second)
    }

    @Test
    fun `getEndOfTodayTimeStamp returns a timestamp at the beginning of the next day`() {
        val endOfToday = LocalTime.getEndOfTodayTimeStamp()
        val localDateTime = LocalTime.timeStampToLocalDateTime(endOfToday)
        assertEquals(0, localDateTime.hour)
        assertEquals(0, localDateTime.minute)
        assertEquals(0, localDateTime.second)
    }
    
    @Test
    fun `timestampToMonth returns correct month`() {
        val expectedMonth = 10
        val actualMonth = LocalTime.timestampToMonth(testTimestamp)
        assertEquals(expectedMonth, actualMonth)
    }

    @Test
    fun `timestampToYear returns correct year`() {
        val expectedYear = 2023
        val actualYear = LocalTime.timestampToYear(testTimestamp)
        assertEquals(expectedYear, actualYear)
    }

    @Test
    fun `timestampToDay returns correct day`() {
        val expectedDay = 27
        val actualDay = LocalTime.timestampToDay(testTimestamp)
        assertEquals(expectedDay, actualDay)
    }
}
