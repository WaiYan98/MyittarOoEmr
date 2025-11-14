package com.waiyan.myittar_oo_emr.screen.component.patient_form_screen

data class PatientFormUiState(
    val success: String = "Failed",
    val isLoading: Boolean = false,
    val onError: String? = null
)
