package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientForm
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.Gender
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
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
    }

    @Test
    fun `insertPatientInfo with valid data without follow-up returns success`() = runTest {
        // Given
        val patientForm = PatientForm(
            name = "John Doe",
            age = "30",
            phone = "1234567890",
            address = "123 Main St",
            gender = Gender.MALE, // Corrected casing
            allergies = "None",
            chronicConditions = "None",
            currentMedication = "None",
            diagnosis = "Headache",
            prescription = "Paracetamol",
            fee = "100",
            followUpDate = 0L,
            reasonForFollowUp = ""
        )
        val patientId = 1L

        coEvery { emrRepository.upsertPatient(any()) } returns Result.success(patientId)
        coEvery { emrRepository.upsertMedicalInfo(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)

        // When
        val result = patientFormUseCase.insertPatientInfo(patientForm, isFollowUpCheckBoxChecked = false)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { emrRepository.upsertPatient(any<Patient>()) }
        coVerify(exactly = 1) { emrRepository.upsertMedicalInfo(any<MedicalInfo>()) }
        coVerify(exactly = 1) { emrRepository.upsertVisit(any<Visit>()) }
        coVerify(exactly = 0) { emrRepository.upsertFollowUp(any<FollowUp>()) }
    }

    @Test
    fun `insertPatientInfo with valid data with follow-up returns success`() = runTest {
        // Given
        val patientForm = PatientForm(
            name = "Jane Doe",
            age = "25",
            phone = "0987654321",
            address = "456 Oak Ave",
            gender = Gender.FEMALE, // Corrected casing
            allergies = "None",
            chronicConditions = "None",
            currentMedication = "None",
            diagnosis = "Fever",
            prescription = "Ibuprofen",
            fee = "150",
            followUpDate = 1673318400000L, // 2023-01-10
            reasonForFollowUp = "Check-up"
        )
        val patientId = 2L

        coEvery { emrRepository.upsertPatient(any()) } returns Result.success(patientId)
        coEvery { emrRepository.upsertMedicalInfo(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertFollowUp(any()) } returns Result.success(Unit)

        // When
        val result = patientFormUseCase.insertPatientInfo(patientForm, isFollowUpCheckBoxChecked = true)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { emrRepository.upsertPatient(any<Patient>()) }
        coVerify(exactly = 1) { emrRepository.upsertMedicalInfo(any<MedicalInfo>()) }
        coVerify(exactly = 1) { emrRepository.upsertVisit(any<Visit>()) }
        coVerify(exactly = 1) { emrRepository.upsertFollowUp(any<FollowUp>()) }
    }

    @Test
    fun `insertPatientInfo with invalid patient data returns failure`() = runTest {
        // Given
        val patientForm = PatientForm(
            name = "",
            age = "30",
            phone = "123",
            address = "Address",
            gender = Gender.MALE, // Corrected casing
            allergies = "",
            chronicConditions = "",
            currentMedication = "",
            diagnosis = "Headache",
            prescription = "Panadol",
            fee = "100",
            followUpDate = 0L,
            reasonForFollowUp = ""
        )

        // When
        val result = patientFormUseCase.insertPatientInfo(patientForm, isFollowUpCheckBoxChecked = false)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { emrRepository.upsertPatient(any()) }
        coVerify(exactly = 0) { emrRepository.upsertMedicalInfo(any()) }
        coVerify(exactly = 0) { emrRepository.upsertVisit(any()) }
        coVerify(exactly = 0) { emrRepository.upsertFollowUp(any()) }
    }

    @Test
    fun `insertPatientInfo with invalid visit data returns failure`() = runTest {
        // Given
        val patientForm = PatientForm(
            name = "John Doe",
            age = "30",
            phone = "1234567890",
            address = "123 Main St",
            gender = Gender.MALE, // Corrected casing
            allergies = "None",
            chronicConditions = "None",
            currentMedication = "None",
            diagnosis = "", // Invalid diagnosis
            prescription = "Paracetamol",
            fee = "100",
            followUpDate = 0L,
            reasonForFollowUp = ""
        )

        // When
        val result = patientFormUseCase.insertPatientInfo(patientForm, isFollowUpCheckBoxChecked = false)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { emrRepository.upsertPatient(any()) }
        coVerify(exactly = 0) { emrRepository.upsertMedicalInfo(any()) }
        coVerify(exactly = 0) { emrRepository.upsertVisit(any()) }
        coVerify(exactly = 0) { emrRepository.upsertFollowUp(any()) }
    }

    @Test
    fun `insertPatientInfo when repository fails to upsert patient returns failure`() = runTest {
        // Given
        val patientForm = PatientForm(
            name = "John Doe",
            age = "30",
            phone = "1234567890",
            address = "123 Main St",
            gender = Gender.MALE, // Corrected casing
            allergies = "None",
            chronicConditions = "None",
            currentMedication = "None",
            diagnosis = "Headache",
            prescription = "Paracetamol",
            fee = "100",
            followUpDate = 0L,
            reasonForFollowUp = ""
        )
        val exception = Exception("Database error")

        coEvery { emrRepository.upsertPatient(any()) } returns Result.failure(exception)

        // When
        val result = patientFormUseCase.insertPatientInfo(patientForm, isFollowUpCheckBoxChecked = false)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 1) { emrRepository.upsertPatient(any()) }
        coVerify(exactly = 0) { emrRepository.upsertMedicalInfo(any()) }
        coVerify(exactly = 0) { emrRepository.upsertVisit(any()) }
        coVerify(exactly = 0) { emrRepository.upsertFollowUp(any()) }
    }
}