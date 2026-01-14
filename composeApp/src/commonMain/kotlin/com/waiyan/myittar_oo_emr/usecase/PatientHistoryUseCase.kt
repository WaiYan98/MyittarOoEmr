package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.VisitAndFollowUpForm
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.data.toFollowUp
import com.waiyan.myittar_oo_emr.data.toVisit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.Validator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class PatientHistoryUseCase(private val emrRepository: EmrRepository) {

    suspend fun getPatientHistory(patientId: Long): Result<PatientWithDetail> {
        return runCatching {
            val patientWithDetail = emrRepository.getPatientWithDetail(patientId).first()
            val sortedVisits = patientWithDetail.visits.sortedByDescending { it.date }
            patientWithDetail.copy(visits = sortedVisits)
        }
    }

    suspend fun insertVisitAndFollowUp(
        visitAndFollowUpForm: VisitAndFollowUpForm,
        isCheckedFollowUp: Boolean
    ): Result<Unit> = runCatching {

        val isValidVisitAndFollowUp = Validator.validateVisitAndFollowUp(
            diagnosis = visitAndFollowUpForm.diagnosis,
            prescription = visitAndFollowUpForm.prescription,
            fee = visitAndFollowUpForm.fee,
            followUpDate = visitAndFollowUpForm.followUpDate,
            reasonForFollowUp = visitAndFollowUpForm.reasonForFollowUp,
            isCheckedFollowUP = isCheckedFollowUp
        )

        if (isValidVisitAndFollowUp is ValidationResult.Failure) throw Exception(
            isValidVisitAndFollowUp.message
        )

        coroutineScope {
            val taskDeferred = mutableListOf<Deferred<Unit>>()

            val visit = visitAndFollowUpForm.toVisit()
            val patientDeferred = async { emrRepository.upsertVisit(visit).getOrThrow() }
            taskDeferred.add(patientDeferred)

            if (isCheckedFollowUp) {
                val followUp = visitAndFollowUpForm.toFollowUp()
                val followUpDeferred =
                    async { emrRepository.upsertFollowUp(followUp).getOrThrow() }
                taskDeferred.add(followUpDeferred)
            }
            taskDeferred.awaitAll()
        }
    }

    suspend fun updateVisit(visit: Visit): Result<Unit> = runCatching {
        val isValidVisit = Validator.validateVisitAndFollowUp(
            diagnosis = visit.diagnosis,
            prescription = visit.prescription,
            fee = visit.fee.toString(), // Convert Long fee to String for validation
            followUpDate = 0L, // Not relevant for visit update without follow-up
            reasonForFollowUp = "", // Not relevant
            isCheckedFollowUP = false // Always false for visit update
        )

        if (isValidVisit is ValidationResult.Failure) throw Exception(
            isValidVisit.message
        )
        emrRepository.upsertVisit(visit).getOrThrow()
    }

    suspend fun updatePatientInfo(patient: Patient): Result<Unit> = runCatching {
        val isValidPatientInfo = Validator.validatePatientInfo(
            patient.name,
            patient.age.toString(),
            patient.phone,
            patient.address
        )

        if (isValidPatientInfo is ValidationResult.Failure)
            throw Exception(isValidPatientInfo.message)

        emrRepository.upsertPatient(patient).getOrThrow()
    }

    suspend fun updateMedicalInfo(medicalInfo: MedicalInfo): Result<Unit> = runCatching {
        emrRepository.upsertMedicalInfo(medicalInfo).getOrThrow()
    }

}