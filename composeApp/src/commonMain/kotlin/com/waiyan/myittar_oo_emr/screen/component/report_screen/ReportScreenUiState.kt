package com.waiyan.myittar_oo_emr.screen.component.report_screen

import com.waiyan.myittar_oo_emr.data.Report
import com.waiyan.myittar_oo_emr.data.VisitsAndFollowUps

data class ReportScreenUiState(
    val success: Report? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
