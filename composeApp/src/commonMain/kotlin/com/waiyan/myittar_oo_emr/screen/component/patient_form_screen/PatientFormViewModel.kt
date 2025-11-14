package com.waiyan.myittar_oo_emr.screen.component.patient_form_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.PatientForm
import com.waiyan.myittar_oo_emr.data.ValidationResult
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.data.entity.Visit
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import com.waiyan.myittar_oo_emr.usecase.PatientFormUseCase
import com.waiyan.myittar_oo_emr.util.Validator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientFormViewModel(
    private val patientFormUseCase: PatientFormUseCase
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
        _uiState.value = PatientFormUiState(isLoading = true)
        val patientForm = PatientForm(
            name = name,
            age = age,
            gender = gender,
            phone = phone,
            address = address,
            allergies = allergies,
            chronicConditions = chronicConditions,
            currentMedication = currentMedication,
            prescription = prescription,
            fee = fee,
            diagnosis = diagnosis,
            followUpDate = followUpDate,
            reasonForFollowUp = reasonForFollowUp
        )
        patientFormUseCase.insertPatientInfo(patientForm)
            .fold(
                onSuccess = {
                    _uiState.value = PatientFormUiState(success = "Success")
                },
                onFailure = { exception ->
                    _uiState.value =
                        PatientFormUiState(onError = exception.message ?: "Unexpected Error")
                }
            )
    }

    fun onClearError() {
        _uiState.value = _uiState.value.copy(onError = null)
    }
}

enum class Gender {
    MALE,
    FEMALE,
    OTHER
}