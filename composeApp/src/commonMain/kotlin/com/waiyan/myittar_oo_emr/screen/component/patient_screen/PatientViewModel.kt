package com.waiyan.myittar_oo_emr.screen.component.patient_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.PatientWithVisitAndFollowUp
import com.waiyan.myittar_oo_emr.usecase.BackupUseCase
import com.waiyan.myittar_oo_emr.usecase.PatientUseCase
import com.waiyan.myittar_oo_emr.usecase.RestoreUseCase
import com.waiyan.myittar_oo_emr.util.LocalTime
import com.waiyan.myittar_oo_emr.util.triggerAppRestart
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class PatientViewModel(
    private val patientUseCase: PatientUseCase,
    private val backupUseCase: BackupUseCase,
    private val restoreUseCase: RestoreUseCase
) : ViewModel() {

    private var _uiState: MutableStateFlow<PatientScreenUiState> =
        MutableStateFlow(PatientScreenUiState())

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _genderQuery = MutableStateFlow<String?>(null)
    val genderQuery: StateFlow<String?> = _genderQuery

    private val _ageRangeQuery = MutableStateFlow<Pair<String, String>>(Pair("", ""))
    val ageRangeQuery: StateFlow<Pair<String, String>> = _ageRangeQuery

    private val _dateRangeQuery = MutableStateFlow<Pair<Long?, Long?>>(Pair(null, null))
    val dateRangeQuery: StateFlow<Pair<Long?, Long?>> = _dateRangeQuery

    val uiState: StateFlow<PatientScreenUiState> =
        combine(
            _searchQuery,
            _genderQuery,
            _ageRangeQuery,
            _dateRangeQuery,
            _uiState
        ) { searchQuery, genderQuery, ageRangeQuery, dateRangeQuery, uiState ->
            val filteredPatients = uiState.success
                .filter { patientWithVisit ->
                    patientWithVisit.patient.name.contains(searchQuery, ignoreCase = true) ||
                            patientWithVisit.patient.id == (searchQuery.toLongOrNull() ?: -1)
                }
                .filter { patientWithVisit ->
                    genderQuery?.let { patientWithVisit.patient.gender == it } ?: true
                }
                .filter { patientWithVisit ->
                    val minAge = ageRangeQuery.first.toIntOrNull()
                    val maxAge = ageRangeQuery.second.toIntOrNull()
                    if (minAge != null && maxAge != null) {
                        patientWithVisit.patient.age in minAge..maxAge
                    } else if (minAge != null) {
                        patientWithVisit.patient.age >= minAge
                    } else if (maxAge != null) {
                        patientWithVisit.patient.age <= maxAge
                    } else {
                        true
                    }
                }
                .filter { patientWithVisit ->
                    val fromDate = dateRangeQuery.first
                    val toDate = dateRangeQuery.second

                    if (fromDate == null && toDate == null) {
                        true
                    } else {
                        patientWithVisit.visits.any { visit ->
                            val visitDate = LocalTime.timeStampToLocalDateTime(visit.date).date
                            val startOfDayFrom = fromDate?.let { LocalTime.timeStampToLocalDateTime(it).date }
                            val startOfDayTo = toDate?.let { LocalTime.timeStampToLocalDateTime(it).date }

                            (startOfDayFrom == null || visitDate >= startOfDayFrom) &&
                                    (startOfDayTo == null || visitDate <= startOfDayTo)
                        }
                    }
                }


            PatientScreenUiState(
                isLoading = uiState.isLoading,
                isBackingUp = uiState.isBackingUp,
                onError = uiState.onError,
                success = filteredPatients
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
            patientUseCase.getPatientsSortedByRecentVisit().fold(
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

    fun onGenderQueryChanged(gender: String?) {
        _genderQuery.value = gender
    }

    fun onAgeRangeQueryChanged(min: String, max: String) {
        _ageRangeQuery.value = Pair(min, max)
    }

    fun onDateRangeQueryChanged(from: Long?, to: Long?) {
        _dateRangeQuery.value = Pair(from, to)
    }

    fun backupDatabase() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    isBackingUp = true
                )
            }
            backupUseCase().fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isBackingUp = false,
                            onError = "Backup successful!"
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isBackingUp = false,
                            onError = "Backup failed: ${exception.message}"
                        )
                    }
                }
            )
        }
    }

    fun restoreDatabase(uriString: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            restoreUseCase(uriString).fold(
                onSuccess = {
                    triggerAppRestart()
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            onError = "Restore failed: ${exception.message}"
                        )
                    }
                }
            )
        }
    }

    fun onClearError() {
        _uiState.update { it.copy(onError = null) }
    }

    fun deleteSelectedPatients(patientIds: List<Long>) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            patientUseCase.deletePatients(patientIds).fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            onError = "Deleted ${patientIds.size} patients successfully!"
                        )
                    }
                    getAllPatient()
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, onError = exception.message)
                    }
                }
            )
        }
    }
}