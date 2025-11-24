package com.waiyan.myittar_oo_emr.screen.component.report_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.usecase.ReportUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class ReportScreenViewModel(
    private val reportUseCase: ReportUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportScreenUiState())
    val uiState = _uiState.asStateFlow()

    init {
        getAllVisitAndFollowUp()
    }

    fun getAllVisitAndFollowUp() = viewModelScope.launch {
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

    fun onClearError() {
        _uiState.update { it.copy(error = null) }
    }

}