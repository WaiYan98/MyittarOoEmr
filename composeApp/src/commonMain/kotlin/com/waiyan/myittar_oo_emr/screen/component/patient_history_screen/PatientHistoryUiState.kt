package com.waiyan.myittar_oo_emr.screen.component.patient_history_screen

import com.waiyan.myittar_oo_emr.data.PatientWithDetail

data class PatientHistoryUiState(
    val success: PatientWithDetail? = null,
    val isLoading: Boolean = false,
    val notify: String? = null,
    val error: String? = null
)