package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.VisitAndFollowUpForm
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
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

class PatientHistoryUseCaseTest {

    private lateinit var emrRepository: EmrRepository
    private lateinit var patientHistoryUseCase: PatientHistoryUseCase

    @BeforeTest
    fun setup() {
        emrRepository = mockk(relaxed = true)
        patientHistoryUseCase = PatientHistoryUseCase(emrRepository)
    }

    @Test
    fun `getPatientHistory returns sorted visits`() = runTest {
        // Given
        val patient = Patient(id = 1, name = "John", age = 30, gender = "M", occupation = "Job", phone = "123", address = "Add")
        val visits = listOf(
            Visit(id = 1, patientId = 1, date = 1000L, fee = 100, diagnosis = "D1", prescription = "P1"),
            Visit(id = 2, patientId = 1, date = 2000L, fee = 200, diagnosis = "D2", prescription = "P2")
        )
        val medicalInfo = MedicalInfo(id = 1, patientId = 1, allergies = "", chronicConditions = "", currentMedication = "")
        val patientWithDetail = PatientWithDetail(patient = patient, medicalInfo = medicalInfo, visits = visits)
        
        every { emrRepository.getPatientWithDetail(1L) } returns flowOf(patientWithDetail)

        // When
        val result = patientHistoryUseCase.getPatientHistory(1L)

        // Then
        assertTrue(result.isSuccess)
        val sortedVisits = result.getOrThrow().visits
        assertEquals(2000L, sortedVisits[0].date)
        assertEquals(1000L, sortedVisits[1].date)
    }

    @Test
    fun `insertVisitAndFollowUp inserts visit and optionally followUp`() = runTest {
        // Given
        val form = VisitAndFollowUpForm(patientId = 1L, diagnosis = "D", prescription = "P", fee = "100", followUpDate = 2000L, reasonForFollowUp = "R")
        coEvery { emrRepository.upsertVisit(any()) } returns Result.success(Unit)
        coEvery { emrRepository.upsertFollowUp(any()) } returns Result.success(Unit)

        // When
        val result = patientHistoryUseCase.insertVisitAndFollowUp(form, isCheckedFollowUp = true)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.upsertVisit(any()) }
        coVerify { emrRepository.upsertFollowUp(any()) }
    }

    @Test
    fun `updateVisit updates visit after validation`() = runTest {
        // Given
        val visit = Visit(id = 1, patientId = 1, date = 1000L, fee = 100, diagnosis = "D", prescription = "P")
        coEvery { emrRepository.upsertVisit(visit) } returns Result.success(Unit)

        // When
        val result = patientHistoryUseCase.updateVisit(visit)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.upsertVisit(visit) }
    }

    @Test
    fun `updatePatientInfo updates patient info after validation`() = runTest {
        // Given
        val patient = Patient(id = 1, name = "John", age = 30, gender = "M", occupation = "Job", phone = "123", address = "Add")
        coEvery { emrRepository.upsertPatient(patient) } returns Result.success(1L)

        // When
        val result = patientHistoryUseCase.updatePatientInfo(patient)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.upsertPatient(patient) }
    }

    @Test
    fun `updateMedicalInfo updates medical info`() = runTest {
        // Given
        val medicalInfo = MedicalInfo(id = 1, patientId = 1, allergies = "A", chronicConditions = "C", currentMedication = "M")
        coEvery { emrRepository.upsertMedicalInfo(medicalInfo) } returns Result.success(Unit)

        // When
        val result = patientHistoryUseCase.updateMedicalInfo(medicalInfo)

        // Then
        assertTrue(result.isSuccess)
        coVerify { emrRepository.upsertMedicalInfo(medicalInfo) }
    }
}
