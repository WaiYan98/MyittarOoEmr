package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.LocalTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ReportUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var reportUseCase: ReportUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk(relaxed = true)
        reportUseCase = ReportUseCase(emrRepository)
    }

    @Test
    fun `getReport returns empty report when no data`() = runTest {
        // Given
        coEvery { emrRepository.getAllVisit() } returns flowOf(emptyList())
        coEvery { emrRepository.getAllFollowUp() } returns flowOf(emptyList())

        // When
        val result = reportUseCase.getReport()

        // Then
        assertTrue(result.isSuccess)
        val report = result.getOrNull()
        assertEquals(0, report?.todayPatientsSeen)
        assertEquals(0L, report?.todayIncome)
        assertEquals(0L, report?.thisMonthIncome)
        assertEquals(0L, report?.thisYearIncome)
        assertTrue(report?.upcomingFollowUps?.isEmpty() == true)
    }

    @Test
    fun `getReport returns correct report with data`() = runTest {
        // Given
        val currentTime = 1672531200000L // 2023-01-01 00:00:00
        val patient1 = Patient(id = 1, name = "John Doe", age = 30, phone = "123", address = "Address", gender = "Male")
        val visits = listOf(
            Visit(id = 1, patientId = 1, date = currentTime, fee = 100, diagnosis = "Headache", prescription = "Paracetamol"),
            Visit(id = 2, patientId = 1, date = currentTime - 86400000, fee = 50, diagnosis = "Fever", prescription = "Ibuprofen") // Yesterday
        )
        val followUps = listOf(
            FollowUp(id = 1, patientId = 1, date = currentTime + 86400000, reasonForFollowUp = "Checkup") // Tomorrow
        )

        coEvery { emrRepository.getAllVisit() } returns flowOf(visits)
        coEvery { emrRepository.getAllFollowUp() } returns flowOf(followUps)
        coEvery { emrRepository.getPatientById(1L) } returns flowOf(patient1)

        // Mock LocalTime
        mockkObject(LocalTime)
        every { LocalTime.getCurrentTimeMillis() } returns currentTime
        every { LocalTime.getStartOfTodayTimeStamp() } returns currentTime
        every { LocalTime.getEndOfTodayTimeStamp() } returns currentTime + 86399999
        every { LocalTime.getCurrentMonth() } returns 1
        every { LocalTime.getCurrentYear() } returns 2023
        every { LocalTime.timestampToMonth(any()) } answers {
            val date = firstArg<Long>()
            if (date == currentTime) 1 else 12
        }
        every { LocalTime.timestampToYear(any()) } returns 2023
        every { LocalTime.getHumanDate(any()) } returns "2023-01-02"
        every { LocalTime.calculateDayUntil(any()) } returns 1L


        // When
        val result = reportUseCase.getReport()

        // Then
        assertTrue(result.isSuccess)
        val report = result.getOrNull()
        assertEquals(1, report?.todayPatientsSeen)
        assertEquals(100L, report?.todayIncome)
        assertEquals(100L, report?.thisMonthIncome)
        assertEquals(150L, report?.thisYearIncome)
        assertEquals(1, report?.upcomingFollowUps?.size)
        unmockkObject(LocalTime)
    }

    @Test
    fun `getReport returns failure on repository error`() = runTest {
        // Given
        val exception = Exception("Database error")
        coEvery { emrRepository.getAllVisit() } throws exception

        // When
        val result = reportUseCase.getReport()

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `getMonthlyIncomes calls repository with correct parameters`() = runTest {
        // Given
        val startDate = 1672531200000L
        val endDate = 1675209599000L
        coEvery { emrRepository.getMonthlyIncome(startDate, endDate) } returns flowOf(emptyList())

        // When
        reportUseCase.getMonthlyIncomes(startDate, endDate)

        // Then
        coVerify { emrRepository.getMonthlyIncome(startDate, endDate) }
    }
}