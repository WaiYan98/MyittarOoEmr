package com.waiyan.myittar_oo_emr.screen.component.patient_screen

import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp

data class PatientScreenUiState(
    val success: List<PatientWithVisitAndFollowUp> = emptyList(),
    val isLoading: Boolean = false,
    val isBackingUp: Boolean = false,
    val onError: String? = null
)

