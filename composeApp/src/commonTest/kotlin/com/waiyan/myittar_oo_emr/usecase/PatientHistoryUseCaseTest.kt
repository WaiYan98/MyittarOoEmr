package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.VisitAndFollowUpForm
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PatientHistoryUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var patientHistoryUseCase: PatientHistoryUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk(relaxed = true)
        patientHistoryUseCase = PatientHistoryUseCase(emrRepository)
    }

    @Test
    fun `getPatientHistory returns success with patient details`() = runTest {
        // Given
        val patientId = 1L
        val patient = Patient(id = patientId, name = "John Doe", age = 30, phone = "123", address = "Address", gender = "Male")
        val patientWithDetail = PatientWithDetail(
            patient = patient,
            medicalInfo = MedicalInfo(patientId = patientId, allergies = "", chronicConditions = "", currentMedication = ""),
            visits = listOf(Visit(id = 1, patientId = patientId, date = 100, diagnosis = "", prescription = "", fee = 0))
        )
        coEvery { emrRepository.getPatientWithDetail(patientId) } returns flowOf(patientWithDetail)

        // When
        val result = patientHistoryUseCase.getPatientHistory(patientId)

        // Then
        assertTrue(result.isSuccess)
        assertEquals(patientWithDetail, result.getOrNull())
    }

    @Test
    fun `getPatientHistory returns failure on repository error`() = runTest {
        // Given
        val patientId = 1L
        val exception = Exception("Database error")
        coEvery { emrRepository.getPatientWithDetail(patientId) } throws exception

        // When
        val result = patientHistoryUseCase.getPatientHistory(patientId)

        // Then
        assertTrue(result.isFailure)
        assertEquals(exception.message, result.exceptionOrNull()?.message)
    }

    @Test
    fun `insertVisitAndFollowUp without follow-up returns success`() = runTest {
        // Given
        val form = VisitAndFollowUpForm(patientId = 1L, diagnosis = "Flu", prescription = "Meds", fee = "50", followUpDate = 0L, reasonForFollowUp = "")
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)

        // When
        val result = patientHistoryUseCase.insertVisitAndFollowUp(form, isCheckedFollowUp = false)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { emrRepository.upsertVisit(any()) }
        coVerify(exactly = 0) { emrRepository.upsertFollowUp(any()) }
    }

    @Test
    fun `insertVisitAndFollowUp with follow-up returns success`() = runTest {
        // Given
        val form = VisitAndFollowUpForm(
            patientId = 1L,
            diagnosis = "Flu",
            prescription = "Meds",
            fee = "50",
            followUpDate = 100L,
            reasonForFollowUp = "Check"
        )
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertFollowUp(any()) } returns Result.success(Unit)

        // When
        val result = patientHistoryUseCase.insertVisitAndFollowUp(form, isCheckedFollowUp = true)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { emrRepository.upsertVisit(any()) }
        coVerify(exactly = 1) { emrRepository.upsertFollowUp(any()) }
    }

    @Test
    fun `insertVisitAndFollowUp returns failure on validation error`() = runTest {
        // Given
        val form = VisitAndFollowUpForm(patientId = 1L, diagnosis = "", prescription = "", fee = "", followUpDate = 0L, reasonForFollowUp = "") // Invalid

        // When
        val result = patientHistoryUseCase.insertVisitAndFollowUp(form, isCheckedFollowUp = false)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { emrRepository.upsertVisit(any()) }
    }

    @Test
    fun `insertVisitAndFollowUp returns failure when upsertVisit fails`() = runTest {
        // Given
        val form = VisitAndFollowUpForm(patientId = 1L, diagnosis = "Flu", prescription = "Meds", fee = "50", followUpDate = 0L, reasonForFollowUp = "")
        val exception = Exception("DB error")
        coEvery { emrRepository.upsertVisit(any()) } returns Result.failure(exception)

        // When
        val result = patientHistoryUseCase.insertVisitAndFollowUp(form, isCheckedFollowUp = false)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `insertVisitAndFollowUp returns failure when upsertFollowUp fails`() = runTest {
        // Given
        val form = VisitAndFollowUpForm(
            patientId = 1L,
            diagnosis = "Flu",
            prescription = "Meds",
            fee = "50",
            followUpDate = 100L,
            reasonForFollowUp = "Check"
        )
        val exception = Exception("DB error")
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertFollowUp(any()) } returns Result.failure(exception)

        // When
        val result = patientHistoryUseCase.insertVisitAndFollowUp(form, isCheckedFollowUp = true)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `updatePatientInfo returns success for valid patient`() = runTest {
        // Given
        val patient = Patient(id = 1, name = "John Doe", age = 30, phone = "123", address = "Address", gender = "Male")
        coEvery { emrRepository.upsertPatient(patient) } returns Result.success(1L)

        // When
        val result = patientHistoryUseCase.updatePatientInfo(patient)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { emrRepository.upsertPatient(patient) }
    }

    @Test
    fun `updatePatientInfo returns failure for invalid patient`() = runTest {
        // Given
        val patient = Patient(id = 1, name = "", age = 30, phone = "123", address = "Address", gender = "Male")

        // When
        val result = patientHistoryUseCase.updatePatientInfo(patient)

        // Then
        assertTrue(result.isFailure)
        coVerify(exactly = 0) { emrRepository.upsertPatient(any()) }
    }

    @Test
    fun `updatePatientInfo returns failure on repository error`() = runTest {
        // Given
        val patient = Patient(id = 1, name = "John Doe", age = 30, phone = "123", address = "Address", gender = "Male")
        val exception = Exception("DB error")
        coEvery { emrRepository.upsertPatient(patient) } returns Result.failure(exception)

        // When
        val result = patientHistoryUseCase.updatePatientInfo(patient)

        // Then
        assertTrue(result.isFailure)
    }

    @Test
    fun `updateMedicalInfo returns success`() = runTest {
        // Given
        val medicalInfo = MedicalInfo(patientId = 1L, allergies = "peanuts", chronicConditions = "none", currentMedication = "none")
        coEvery { emrRepository.upsertMedicalInfo(medicalInfo) } returns Result.success(Unit)

        // When
        val result = patientHistoryUseCase.updateMedicalInfo(medicalInfo)

        // Then
        assertTrue(result.isSuccess)
        coVerify(exactly = 1) { emrRepository.upsertMedicalInfo(medicalInfo) }
    }

    @Test
    fun `updateMedicalInfo returns failure on repository error`() = runTest {
        // Given
        val medicalInfo = MedicalInfo(patientId = 1L, allergies = "peanuts", chronicConditions = "none", currentMedication = "none")
        val exception = Exception("DB error")
        coEvery { emrRepository.upsertMedicalInfo(medicalInfo) } returns Result.failure(exception)

        // When
        val result = patientHistoryUseCase.updateMedicalInfo(medicalInfo)

        // Then
        assertTrue(result.isFailure)
    }
}