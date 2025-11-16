package com.waiyan.myittar_oo_emr.usecase

import com.waiyan.myittar_oo_emr.data.PatientForm
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.LocalTime
import com.waiyan.myittar_oo_emr.util.Validator
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.datetime.LocalDate

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
        return when (isValidate) {
            is ValidationResult.Success -> {
                val patient = patientForm.patientFormToPatient()
                val result = emrRepository.insertPatient(patient)
                result.fold(
                    onSuccess = { id ->
                        return runCatching {
                            coroutineScope {
                                val medicalInfo = patientForm.patientFormToMedicalInfo(id)
                                val visit = patientForm.patientFormToVisit(id)

                                coroutineScope {
                                    val medicalInfoDeferred =
                                        async { emrRepository.insertMedicalInfo(medicalInfo) }
                                    val visitDeferred = async { emrRepository.insertVisit(visit) }

                                    if (isFollowUpCheckBoxChecked) {
                                        val followUp = patientForm.patientFormToFollowUp(id)
                                        val followUpDeferred =
                                            async { emrRepository.insertFollowUp(followUp) }
                                        followUpDeferred.await()
                                    }

                                    awaitAll(medicalInfoDeferred, visitDeferred)
                                    Result.success(Unit)
                                }
                            }
                        }
                    },
                    onFailure = { exception ->
                        Result.failure(exception)
                    })
            }

            is ValidationResult.Failure -> {
                Result.failure(exception = Exception(isValidate.message))
            }
        }
    }

    private fun PatientForm.patientFormToPatient(): Patient {
        return Patient(
            name = this.name,
            age = this.age.toInt(),
            gender = this.gender.name,
            phone = this.phone,
            address = this.address
        )
    }

    private fun PatientForm.patientFormToMedicalInfo(patientId: Long): MedicalInfo {
        return MedicalInfo(
            patientId = patientId,
            allergies = this.allergies,
            chronicConditions = this.chronicConditions,
            currentMedication = this.currentMedication
        )
    }

    private fun PatientForm.patientFormToVisit(patientId: Long): Visit {
        return Visit(
            patientId = patientId,
            date = LocalTime.getCurrentTimeMillis(),
            diagnosis = this.diagnosis,
            prescription = this.prescription,
            fee = this.fee.toLong()
        )
    }

    private fun PatientForm.patientFormToFollowUp(patientId: Long): FollowUp {
        return FollowUp(
            patientId = patientId,
            date = this.followUpDate,
            reasonForVisit = this.reasonForFollowUp
        )
    }
}
