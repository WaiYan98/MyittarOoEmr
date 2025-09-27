package com.waiyan.myittar_oo_emr.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.Patient
import com.waiyan.myittar_oo_emr.local_service.PatientRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toCollection
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class PatientViewModel(
    private val patientRepository: PatientRepository
) : ViewModel() {

    init {
        getAllPatient()
    }

    private var _patientFlow: MutableStateFlow<List<Patient>> = MutableStateFlow(emptyList())
    val patientFlow: StateFlow<List<Patient>> = _patientFlow


    fun insertPatient(patient: Patient) {
        viewModelScope.launch {
            patientRepository.insertPatient(patient)
        }
    }

    private fun getAllPatient() {
        viewModelScope.launch {
            patientRepository.getAllPatient()
                .collect {
                    _patientFlow.value = it
                }

        }
    }

}