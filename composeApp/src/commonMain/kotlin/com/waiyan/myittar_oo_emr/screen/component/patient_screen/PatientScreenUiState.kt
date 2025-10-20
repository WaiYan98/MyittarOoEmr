package com.waiyan.myittar_oo_emr.screen.component.patient_screen

import com.waiyan.myittar_oo_emr.data.entity.Patient

data class PatientScreenUiState(
    val success: List<Patient> = emptyList(),
    val isLoading: Boolean = false,
    val onError: String? = null
)
