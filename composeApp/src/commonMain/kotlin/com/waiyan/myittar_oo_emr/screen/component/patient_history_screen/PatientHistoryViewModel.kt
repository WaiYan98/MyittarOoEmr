package com.waiyan.myittar_oo_emr.screen.component.patient_history_screen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.VisitAndFollowUpForm
import com.waiyan.myittar_oo_emr.data.entity.MedicalInfo
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.usecase.PatientHistoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientHistoryViewModel(
    private val patientHistoryUseCase: PatientHistoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState: MutableStateFlow<PatientHistoryUiState> =
        MutableStateFlow(PatientHistoryUiState())
    val uiState: StateFlow<PatientHistoryUiState> = _uiState.asStateFlow()

    private val patientId = savedStateHandle.get<Long>("patientId") ?: 0

    init {
        getPatientHistory(patientId)
    }


    private fun getPatientHistory(patientId: Long) =
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            patientHistoryUseCase.getPatientHistory(patientId)
                .fold(
                    onSuccess = { patientWithDetail ->
                        _uiState.update { it.copy(isLoading = false, success = patientWithDetail) }
                    },
                    onFailure = { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    }
                )
        }

    fun insertVisitAndFollowUp(
        visitAndFollowUpForm: VisitAndFollowUpForm,
        isCheckedFollowUpForm: Boolean
    ) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        patientHistoryUseCase.insertVisitAndFollowUp(
            visitAndFollowUpForm,
            isCheckedFollowUpForm
        )
            .fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, notify = "Added New Visit") }
                    getPatientHistory(patientId)
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(isLoading = false, error = exception.message) }
                }
            )
    }

    fun updatePatientInfo(
        originalPatient: Patient,
        editedPatient: Patient
    ) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        if (editedPatient == originalPatient) {
            _uiState.update { it.copy(isLoading = false, notify = "No Changes!") }
        } else {
            patientHistoryUseCase.updatePatientInfo(editedPatient)
                .fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                notify = "Updated!"
                            )
                        }
                        getPatientHistory(patientId)
                    },
                    onFailure = { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    }
                )
        }
    }

    fun updateMedicalInfo(
        originalMedicalInfo: MedicalInfo,
        editedMedicalInfo: MedicalInfo
    ) = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }

        if (originalMedicalInfo == editedMedicalInfo) {
            _uiState.update { it.copy(isLoading = false, notify = "No Changes!") }
        } else {
            patientHistoryUseCase.updateMedicalInfo(editedMedicalInfo)
                .fold(
                    onSuccess = {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                notify = "Updated!"
                            )
                        }
                        getPatientHistory(patientId)
                    },
                    onFailure = { exception ->
                        _uiState.update { it.copy(isLoading = false, error = exception.message) }
                    }
                )
        }
    }

    fun onClearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun onClearNotify() {
        _uiState.update { it.copy(notify = null) }
    }

}