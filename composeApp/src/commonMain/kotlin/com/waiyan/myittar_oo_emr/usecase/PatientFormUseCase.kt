package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientForm
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.patientFormToFollowUp
import com.waiyan.myittar_oo_emr.data.patientFormToMedicalInfo
import com.waiyan.myittar_oo_emr.data.patientFormToPatient
import com.waiyan.myittar_oo_emr.data.patientFormToVisit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.Validator
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PatientFormUseCase(
    private val emrRepository: EmrRepository,
) {
    suspend fun insertPatientInfo(
        patientForm: PatientForm,
        isFollowUpCheckBoxChecked: Boolean
    ): Result<Unit> = runCatching {
        val isValidatePatient = Validator.validatePatientInfo(
            patientForm.name,
            patientForm.age,
            patientForm.phone,
            patientForm.address,
        )

        if (isValidatePatient is ValidationResult.Failure) {
            throw Exception(isValidatePatient.message)
        }

        val isValidateVisitAndFollowUp = Validator.validateVisitAndFollowUp(
            diagnosis = patientForm.diagnosis,
            prescription = patientForm.prescription,
            fee = patientForm.fee,
            followUpDate = patientForm.followUpDate,
            reasonForFollowUp = patientForm.reasonForFollowUp,
            isCheckedFollowUP = isFollowUpCheckBoxChecked
        )

        if (isValidateVisitAndFollowUp is ValidationResult.Failure) {
            throw Exception(isValidateVisitAndFollowUp.message)
        }

        val patient = patientForm.patientFormToPatient()
        val patientId = emrRepository.upsertPatient(patient).getOrThrow()


        coroutineScope {
            val medicalInfo = patientForm.patientFormToMedicalInfo(patientId)
            val visit = patientForm.patientFormToVisit(patientId)

            val deferredTasks = mutableListOf(
                async { emrRepository.upsertMedicalInfo(medicalInfo).getOrThrow() },
                async { emrRepository.upsertVisit(visit).getOrThrow() }
            )

            if (isFollowUpCheckBoxChecked) {
                val followUp = patientForm.patientFormToFollowUp(patientId)
                val followUpDeferred =
                    async { emrRepository.upsertFollowUp(followUp).getOrThrow() }
                deferredTasks.add(followUpDeferred)
            }
            deferredTasks.awaitAll()
        }

    }

}
