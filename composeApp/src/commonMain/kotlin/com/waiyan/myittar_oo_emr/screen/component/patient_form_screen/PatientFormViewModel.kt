package com.waiyan.myittar_oo_emr.screen.component.patient_form_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.util.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientFormViewModel(
    private val emrRepository: EmrRepository
) : ViewModel() {

    private var _uiState: MutableStateFlow<PatientFormUiState> =
        MutableStateFlow(PatientFormUiState())
    val uiState: StateFlow<PatientFormUiState> = _uiState

    fun insertPatientInfo(
        name: String,
        age: String,
        gender: Gender,
        phone: String,
        address: String,
        allergies: String,
        chronicConditions: String,
        currentMedication: String,
        prescription: String,
        fee: String,
        diagnosis: String,
        followUpDate: String,
        reasonForFollowUp: String
    ) = viewModelScope.launch {
        try {
            _uiState.value = PatientFormUiState(isLoading = true)
            val validationResult = Validator.validatePatientInfo(
                name,
                age,
                phone,
                address
            )
            when (validationResult) {
                is ValidationResult.Success -> {
                    val patient = Patient(
                        name = name,
                        age = age.toInt(),
                        gender = gender.name,
                        phone = phone,
                        address = address
                    )

                    val patientId = emrRepository.insertPatient(patient)
                    println("patient Id is $patientId")

                    val medicalInfo = MedicalInfo(
                        patientId = patientId,
                        allergies = allergies,
                        chronicConditions = chronicConditions,
                        currentMedication = currentMedication
                    )

                    val visit = Visit(
                        patientId = patientId,
                        date = "12/05/2025",
                        diagnosis = diagnosis,
                        prescription = prescription,
                        fee = fee.toLong()
                    )

                    val followUp = FollowUp(
                        patientId = patientId,
                        date = followUpDate,
                        reasonForVisit = reasonForFollowUp
                    )

                    _uiState.value = PatientFormUiState(success = "Success")

                }

                is ValidationResult.Failure -> {
                    _uiState.value = PatientFormUiState(onError = validationResult.message)
                }
            }
        } catch (e: Exception) {
            _uiState.value = PatientFormUiState(onError = e.message)
        }
    }
}

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}