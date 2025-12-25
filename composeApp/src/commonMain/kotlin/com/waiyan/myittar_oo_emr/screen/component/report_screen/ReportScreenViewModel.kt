package com.waiyan.myittar_oo_emr.screen.component.report_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.MonthlyIncome
import com.waiyan.myittar_oo_emr.usecase.ReportUseCase
import com.waiyan.myittar_oo_emr.util.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ReportScreenViewModel(
    private val reportUseCase: ReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportScreenUiState())
    val uiState = _uiState.asStateFlow()

    private val _monthlyIncomes = MutableStateFlow<List<MonthlyIncome>>(emptyList())
    val monthlyIncomes = _monthlyIncomes.asStateFlow()

    init {
        getSummaryReport()
        val endDate = LocalTime.getCurrentTimeMillis()
        val startDate = endDate - 31536000000L // 365 days
        getMonthlyIncomes(startDate, endDate)
    }

    private fun getSummaryReport() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true) }
        reportUseCase.getReport()
            .fold(
                onSuccess = { report ->
                    _uiState.update {
                        it.copy(
                            success = report,
                            isLoading = false
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update { it.copy(error = exception.message, isLoading = false) }
                }
            )
    }

    private fun getMonthlyIncomes(startDate: Long, endDate: Long) = viewModelScope.launch {
        reportUseCase.getMonthlyIncomes(startDate, endDate).collectLatest { monthlyIncomeList ->
            _monthlyIncomes.value = monthlyIncomeList
        }
    }

    fun onDateRangeChanged(startDate: Long, endDate: Long) {
        getMonthlyIncomes(startDate, endDate)
    }

    fun onClearError() {
        _uiState.update { it.copy(error = null) }
    }

}