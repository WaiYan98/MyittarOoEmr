package com.waiyan.myittar_oo_emr.screen.component.report_screen

import com.waiyan.myittar_oo_emr.data.Report
import com.waiyan.myittar_oo_emr.util.LocalTime

data class ReportScreenUiState(
    val success: Report? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val startDate: Long = LocalTime.getCurrentTimeMillis() - 31536000000L, // 365 days
    val endDate: Long = LocalTime.getCurrentTimeMillis()
)
