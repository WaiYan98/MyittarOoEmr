package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientForm
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.patientFormToFollowUp
import com.waiyan.myittar_oo_emr.data.patientFormToMedicalInfo
import com.waiyan.myittar_oo_emr.data.patientFormToPatient
import com.waiyan.myittar_oo_emr.data.patientFormToVisit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.Validator
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class PatientFormUseCase(
    private val emrRepository: EmrRepository,
) {

    suspend fun insertPatientInfo(
        patientForm: PatientForm,
        isFollowUpCheckBoxChecked: Boolean
    ): Result<Unit> {
        val isValidate = Validator.validatePatientInfo(
            patientForm.name,
            patientForm.age,
            patientForm.phone,
            patientForm.address,
            patientForm.fee
        )

        if (isValidate is ValidationResult.Failure) {
            return Result.failure(Exception(isValidate.message))
        }

        val patient = patientForm.patientFormToPatient()
        val patientId = emrRepository.insertPatient(patient).getOrElse { exception ->
            return Result.failure(Exception(exception.message))
        }

        return runCatching {
            coroutineScope {
                val medicalInfo = patientForm.patientFormToMedicalInfo(patientId)
                val visit = patientForm.patientFormToVisit(patientId)

                val deferredTasks = mutableListOf(
                    async { emrRepository.insertMedicalInfo(medicalInfo).getOrThrow() },
                    async { emrRepository.insertVisit(visit).getOrThrow() }
                )

                if (isFollowUpCheckBoxChecked) {
                    val followUp = patientForm.patientFormToFollowUp(patientId)
                    val followUpDeferred =
                        async { emrRepository.insertFollowUp(followUp).getOrThrow() }
                    deferredTasks.add(followUpDeferred)
                }
                deferredTasks.awaitAll()
            }
        }

    }

}
