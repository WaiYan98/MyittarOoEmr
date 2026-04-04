package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PatientUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var patientUseCase: PatientUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk()
        patientUseCase = PatientUseCase(emrRepository)
    }

    @Test
    fun `getPatientsSortedByRecentVisit sorts by most recent visit date`() = runTest {
        // Given
        val patientA = Patient(id = 1, name = "A", age = 20, gender = "M", occupation = "Dev", phone = "1", address = "Add")
        val patientB = Patient(id = 2, name = "B", age = 30, gender = "F", occupation = "Doc", phone = "2", address = "Add")
        
        val data = listOf(
            PatientWithVisitAndFollowUp(
                patient = patientA,
                visits = listOf(Visit(id = 1, patientId = 1, date = 1000L, diagnosis = "D1", prescription = "P1", fee = 100)),
                followUp = emptyList()
            ),
            PatientWithVisitAndFollowUp(
                patient = patientB,
                visits = listOf(Visit(id = 2, patientId = 2, date = 2000L, diagnosis = "D2", prescription = "P2", fee = 200)),
                followUp = emptyList()
            )
        )

        // Repository returns Flow, so use 'every'
        every { emrRepository.getPatientWithVisitAndFollowUp() } returns flowOf<List<PatientWithVisitAndFollowUp>>(data)

        // When
        val result = patientUseCase.getPatientsSortedByRecentVisit()

        // Then
        assertTrue(result.isSuccess)
        val sortedList = result.getOrThrow()
        assertEquals(2L, sortedList[0].patient.id) // Patient B (date 2000) should be first
        assertEquals(1L, sortedList[1].patient.id) // Patient A (date 1000) should be second
    }

    @Test
    fun `getPatientsSortedByRecentVisit returns empty list when no patients`() = runTest {
        // Given
        every { emrRepository.getPatientWithVisitAndFollowUp() } returns flowOf<List<PatientWithVisitAndFollowUp>>(emptyList())

        // When
        val result = patientUseCase.getPatientsSortedByRecentVisit()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `deletePatients calls repository and returns success`() = runTest {
        // Given
        val ids = listOf(1L, 2L)
        coEvery { emrRepository.deletePatients(ids) } returns Result.success(Unit)

        // When
        val result = patientUseCase.deletePatients(ids)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.deletePatients(ids) }
    }

    @Test
    fun `deletePatients returns failure when repository fails`() = runTest {
        // Given
        val ids = listOf(1L)
        val error = Exception("Delete failed")
        coEvery { emrRepository.deletePatients(ids) } throws error

        // When
        val result = patientUseCase.deletePatients(ids)

        // Then
        assertTrue(result.isFailure)
        assertEquals(error.message, result.exceptionOrNull()?.message)
    }
}
