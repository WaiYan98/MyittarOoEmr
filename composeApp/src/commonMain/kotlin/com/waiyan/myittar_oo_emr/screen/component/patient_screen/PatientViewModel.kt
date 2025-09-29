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

    init {
        getAllPatient()
    }

    private var _patientFlow: MutableStateFlow<List<Patient>> = MutableStateFlow(emptyList())
    val patientFlow: StateFlow<List<Patient>> = _patientFlow


    fun insertPatient(patient: Patient) {
        viewModelScope.launch {
            emrRepository.insertPatient(patient)
        }
    }

    private fun getAllPatient() {
        viewModelScope.launch {
            emrRepository.getAllPatient()
                .collect {
                    _patientFlow.value = it
                }
        }
    }

    private fun insertFollowUp(followUp: FollowUp) = viewModelScope.launch {
        emrRepository.insertFollowUp(followUp)
    }

}