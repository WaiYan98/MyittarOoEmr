package com.waiyan.myittar_oo_emr.screen.component.patient_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.entity.FollowUp
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.local_service.EmrRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientViewModel(
    private val emrRepository: EmrRepository
) : ViewModel() {

    private var _uiState: MutableStateFlow<PatientScreenUiState> = MutableStateFlow(
        PatientScreenUiState()
    )
    val uiState: StateFlow<PatientScreenUiState> = _uiState


    fun insertPatient(patient: Patient) {
        viewModelScope.launch {
            emrRepository.insertPatient(patient)
        }
    }

    fun getAllPatient() {
        viewModelScope.launch {
            _uiState.value = PatientScreenUiState(isLoading = true)
            emrRepository.getAllPatient()
                .collect {
                    _uiState.value = PatientScreenUiState(success = it)
                }
        }
    }

    private fun insertFollowUp(followUp: FollowUp) = viewModelScope.launch {
        emrRepository.insertFollowUp(followUp)
    }

}