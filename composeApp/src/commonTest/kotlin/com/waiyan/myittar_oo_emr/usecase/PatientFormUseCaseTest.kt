package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientForm
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.Gender
import com.waiyan.myittar_oo_emr.util.LocalTime
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkObject
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class PatientFormUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var patientFormUseCase: PatientFormUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk(relaxed = true)
        patientFormUseCase = PatientFormUseCase(emrRepository)
        mockkObject(LocalTime)
        every { LocalTime.getCurrentTimeMillis() } returns 1000L
    }

    @AfterTest
    fun teardown() {
        unmockkObject(LocalTime)
    }

    @Test
    fun `insertPatientInfo inserts patient, medicalInfo, and visit`() = runTest {
        // Given
        val patientForm = PatientForm(
            name = "John", age = 30, gender = Gender.MALE, occupation = "Job",
            phone = "123", address = "Add", allergies = "None", chronicConditions = "None",
            currentMedication = "None", prescription = "P1", fee = "100", diagnosis = "D1",
            followUpDate = 0L, reasonForFollowUp = ""
        )
        coEvery { emrRepository.upsertPatient(any()) } returns Result.success(1L)
        coEvery { emrRepository.upsertMedicalInfo(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)

        // When
        val result = patientFormUseCase.insertPatientInfo(patientForm, isFollowUpCheckBoxChecked = false)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.upsertPatient(any()) }
        coVerify { emrRepository.upsertMedicalInfo(any()) }
        coVerify { emrRepository.upsertVisit(any()) }
        coVerify(exactly = 0) { emrRepository.upsertFollowUp(any()) }
    }

    @Test
    fun `insertPatientInfo inserts followUp when checkbox is checked`() = runTest {
        // Given
        val patientForm = PatientForm(
            name = "John", age = 30, gender = Gender.MALE, occupation = "Job",
            phone = "123", address = "Add", allergies = "None", chronicConditions = "None",
            currentMedication = "None", prescription = "P1", fee = "100", diagnosis = "D1",
            followUpDate = 2000L, reasonForFollowUp = "Check"
        )
        coEvery { emrRepository.upsertPatient(any()) } returns Result.success(1L)
        coEvery { emrRepository.upsertMedicalInfo(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertFollowUp(any()) } returns Result.success(Unit)

        // When
        val result = patientFormUseCase.insertPatientInfo(patientForm, isFollowUpCheckBoxChecked = true)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.upsertFollowUp(any()) }
    }

    @Test
    fun `insertPatientInfo returns failure on invalid data`() = runTest {
        // Given
        val invalidForm = PatientForm(
            name = "", age = 0, gender = Gender.MALE, occupation = "",
            phone = "", address = "", allergies = "", chronicConditions = "",
            currentMedication = "", prescription = "", fee = "", diagnosis = "",
            followUpDate = 0L, reasonForFollowUp = ""
        )

        // When
        val result = patientFormUseCase.insertPatientInfo(invalidForm, isFollowUpCheckBoxChecked = false)

        // Then
        assertTrue(result.isFailure)
    }
}
