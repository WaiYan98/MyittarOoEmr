package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class IncomeDetailsUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var incomeDetailsUseCase: IncomeDetailsUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk()
        incomeDetailsUseCase = IncomeDetailsUseCase(emrRepository)
    }

    @Test
    fun `invoke maps PatientWithVisitAndFollowUp to IncomeDetail`() = runTest {
        // Given
        val patient = Patient(id = 1, name = "John Doe", age = 30, gender = "M", occupation = "Job", phone = "123", address = "Add")
        val visits = listOf(
            Visit(id = 1, patientId = 1, date = 1000L, fee = 100, diagnosis = "D1", prescription = "P1"),
            Visit(id = 2, patientId = 1, date = 2000L, fee = 200, diagnosis = "D2", prescription = "P2")
        )
        val data = listOf(
            PatientWithVisitAndFollowUp(patient = patient, visits = visits, followUp = emptyList())
        )

        every { emrRepository.getPatientWithVisitAndFollowUp() } returns flowOf<List<PatientWithVisitAndFollowUp>>(data)

        // When
        val result = incomeDetailsUseCase()

        // Then
        assertTrue(result.isSuccess)
        val incomeDetails = result.getOrThrow()
        assertEquals(2, incomeDetails.size)
        
        assertEquals(1L, incomeDetails[0].patientId)
        assertEquals("John Doe", incomeDetails[0].patientName)
        assertEquals(1000L, incomeDetails[0].visitTime)
        assertEquals(100, incomeDetails[0].fee)

        assertEquals(1L, incomeDetails[1].patientId)
        assertEquals("John Doe", incomeDetails[1].patientName)
        assertEquals(2000L, incomeDetails[1].visitTime)
        assertEquals(200, incomeDetails[1].fee)
    }

    @Test
    fun `invoke returns empty list when no patients or visits`() = runTest {
        // Given
        every { emrRepository.getPatientWithVisitAndFollowUp() } returns flowOf<List<PatientWithVisitAndFollowUp>>(emptyList())

        // When
        val result = incomeDetailsUseCase()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }
}
