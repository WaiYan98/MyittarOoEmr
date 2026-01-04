package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.coEvery
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
    fun `getPatientsSortedByRecentVisit sorts by most recent activity`() = runTest {
        // Given
        val patientA = Patient(id = 1, name = "Patient A", age = 30, phone = "123", address = "Add1", gender = "Male")
        val patientB = Patient(id = 2, name = "Patient B", age = 40, phone = "456", address = "Add2", gender = "Male")
        val patientC = Patient(id = 3, name = "Patient C", age = 50, phone = "789", address = "Add3", gender = "Female")
        val patientD = Patient(id = 4, name = "Patient D", age = 60, phone = "012", address = "Add4", gender = "Male")

        val data = listOf(
            // Patient A: Most recent activity is a visit at time 100
            PatientWithVisitAndFollowUp(
                patient = patientA,
                visits = listOf(Visit(id = 1, patientId = 1, date = 100, diagnosis = "", prescription = "", fee = 0)),
                followUp = listOf(FollowUp(id = 1, patientId = 1, date = 50, reasonForFollowUp = ""))
            ),
            // Patient B: Most recent activity is a visit at time 75
            PatientWithVisitAndFollowUp(
                patient = patientB,
                visits = listOf(Visit(id = 2, patientId = 2, date = 75, diagnosis = "", prescription = "", fee = 0)),
                followUp = listOf(FollowUp(id = 2, patientId = 2, date = 200, reasonForFollowUp = ""))
            ),
            // Patient C: Only has an old visit at time 25
            PatientWithVisitAndFollowUp(
                patient = patientC,
                visits = listOf(Visit(id = 3, patientId = 3, date = 25, diagnosis = "", prescription = "", fee = 0)),
                followUp = emptyList()
            ),
            // Patient D: No activity
            PatientWithVisitAndFollowUp(
                patient = patientD,
                visits = emptyList(),
                followUp = emptyList()
            )
        )

        coEvery { emrRepository.getPatientWithVisitAndFollowUp() } returns flowOf(data)

        // When
        val result = patientUseCase.getPatientsSortedByRecentVisit()

        // Then
        assertTrue(result.isSuccess)
        val sortedPatients = result.getOrNull()
        // Expected order: A (100), B (75), C (25), D (0)
        assertEquals(listOf(patientA, patientB, patientC, patientD), sortedPatients)
    }

    @Test
    fun `getPatientsSortedByRecentVisit returns success with empty list`() = runTest {
        // Given
        coEvery { emrRepository.getPatientWithVisitAndFollowUp() } returns flowOf(emptyList())

        // When
        val result = patientUseCase.getPatientsSortedByRecentVisit()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getPatientsSortedByRecentVisit returns failure on repository error`() = runTest {
        // Given
        val exception = Exception("Database error")
        coEvery { emrRepository.getPatientWithVisitAndFollowUp() } throws exception

        // When
        val result = patientUseCase.getPatientsSortedByRecentVisit()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }
}