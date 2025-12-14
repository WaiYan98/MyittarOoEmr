package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.VisitAndFollowUpForm
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.toFollowUp
import com.waiyan.myittar_oo_emr.data.toVisit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.Validator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first

class PatientHistoryUseCase(private val emrRepository: EmrRepository) {

    suspend fun getPatientHistory(patientId: Long): Result<PatientWithDetail> {
        return runCatching {
            emrRepository.getPatientWithDetail(patientId).first()
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