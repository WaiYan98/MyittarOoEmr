package com.waiyan.myittar_oo_emr.screen.component.patient_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.usecase.PatientUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientViewModel(
    private val patientUseCase: PatientUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<PatientScreenUiState> =
        MutableStateFlow(PatientScreenUiState())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    val uiState: StateFlow<PatientScreenUiState> =
        combine(_searchQuery, _uiState) { _searchQuery, _uiState ->
            PatientScreenUiState(
                isLoading = _uiState.isLoading,
                onError = _uiState.onError,
                success = if (_searchQuery.isBlank()) {
                    _uiState.success
                } else {
                    _uiState.success.filter {
                        it.name.contains(_searchQuery, ignoreCase = true) ||
                                it.id == (_searchQuery.toLongOrNull() ?: 0)
                    }
                }
            )
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = PatientScreenUiState()
            )

    fun getAllPatient() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            patientUseCase.getAllPatients().fold(
                onSuccess = { patients ->
                    _uiState.update { it.copy(isLoading = false, success = patients) }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, onError = exception.message)
                    }
                }
            )
        }
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }


    fun onClearError() {
        _uiState.update { it.copy(onError = null) }
    }
}