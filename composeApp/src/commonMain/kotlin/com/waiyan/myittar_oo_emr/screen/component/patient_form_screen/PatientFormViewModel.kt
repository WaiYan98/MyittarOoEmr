package com.waiyan.myittar_oo_emr.screen.component.patient_form_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.PatientForm
import com.waiyan.myittar_oo_emr.usecase.PatientFormUseCase
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
        age: Int,
        gender: Gender,
        occupation: String,
        phone: String,
        address: String,
        allergies: String,
        chronicConditions: String,
        currentMedication: String,
        prescription: String,
        fee: String,
        diagnosis: String,
        followUpDate: Long,
        reasonForFollowUp: String,
        isChecked: Boolean
    ) = viewModelScope.launch {
        _uiState.value =
            PatientFormUiState(isLoading = true, isInTheProcessOfInsertingPatient = true)
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
            reasonForFollowUp = reasonForFollowUp,
            occupation = occupation
        )
        patientFormUseCase.insertPatientInfo(patientForm, isChecked)
            .fold(
                onSuccess = {
                    _uiState.value = PatientFormUiState(
                        success = "Success",
                        isInTheProcessOfInsertingPatient = true
                    )
                },
                onFailure = { exception ->
                    _uiState.value =
                        PatientFormUiState(
                            onError = exception.message ?: "Unexpected Error",
                            isInTheProcessOfInsertingPatient = false
                        )
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