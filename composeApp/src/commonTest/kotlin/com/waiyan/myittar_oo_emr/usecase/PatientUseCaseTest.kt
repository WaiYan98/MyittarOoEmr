package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.entity.Patient
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
    fun `getAllPatients returns success with list of patients`() = runTest {
        // Given
        val patients = listOf(
            Patient(id = 1, name = "Patient One", age = 30, phone = "123", address = "Add1", gender = "Male"),
            Patient(id = 2, name = "Patient Two", age = 40, phone = "456", address = "Add2", gender = "Male")
        )
        coEvery { emrRepository.getAllPatient() } returns flowOf(patients)

        // When
        val result = patientUseCase.getAllPatients()

        // Then
        assertTrue(result.isSuccess)
        assertEquals(patients, result.getOrNull())
    }

    @Test
    fun `getAllPatients returns success with empty list when no patients exist`() = runTest {
        // Given
        val emptyPatients = emptyList<Patient>()
        coEvery { emrRepository.getAllPatient() } returns flowOf(emptyPatients)

        // When
        val result = patientUseCase.getAllPatients()

        // Then
        assertTrue(result.isSuccess)
        assertTrue(result.getOrNull()?.isEmpty() == true)
    }

    @Test
    fun `getAllPatients returns failure when repository throws exception`() = runTest {
        // Given
        val exception = Exception("Database error")
        coEvery { emrRepository.getAllPatient() } throws exception

        // When
        val result = patientUseCase.getAllPatients()

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }
}