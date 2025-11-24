package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.VisitAndFollowUpForm
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
    ): Result<Unit> {

        val isValidVisitAndFollowUp = Validator.validateVisitAndFollowUp(
            diagnosis = visitAndFollowUpForm.diagnosis,
            prescription = visitAndFollowUpForm.prescription,
            fee = visitAndFollowUpForm.fee,
            followUpDate = visitAndFollowUpForm.followUpDate,
            reasonForFollowUp = visitAndFollowUpForm.reasonForFollowUp,
            isCheckedFollowUP = isCheckedFollowUp
        )

        if (isValidVisitAndFollowUp is ValidationResult.Failure) return Result.failure(
            Exception(
                isValidVisitAndFollowUp.message
            )
        )

        return runCatching {
            coroutineScope {
                val taskDeferred = mutableListOf<Deferred<Unit>>()

                val visit = visitAndFollowUpForm.toVisit()
                val patientDeferred = async { emrRepository.insertVisit(visit).getOrThrow() }
                taskDeferred.add(patientDeferred)

                if (isCheckedFollowUp) {
                    val followUp = visitAndFollowUpForm.toFollowUp()
                    val followUpDeferred =
                        async { emrRepository.insertFollowUp(followUp).getOrThrow() }
                    taskDeferred.add(followUpDeferred)
                }
                taskDeferred.awaitAll()
            }
        }

    }


}