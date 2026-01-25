package com.waiyan.myittar_oo_emr.screen.component.report_screen

import TodayIncomeDetailsUiState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.usecase.IncomeDetailsUseCase
import com.waiyan.myittar_oo_emr.util.LocalTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlin.time.ExperimentalTime

class TodayIncomeDetailsViewModel(
    private val incomeDetailsUseCase: IncomeDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodayIncomeDetailsUiState())

    private val _selectedDate = MutableStateFlow(LocalTime.getCurrentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    @OptIn(ExperimentalTime::class)
    val uiState: StateFlow<TodayIncomeDetailsUiState> =
        combine(
            _uiState,
            _selectedDate
        ) { uiState, date ->
            val localDate = LocalTime.timeStampToLocalDateTime(date).date
            val startOfDay =
                localDate.atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            val endOfDay =
                localDate.plus(1, DateTimeUnit.DAY).atStartOfDayIn(TimeZone.currentSystemDefault())
                    .toEpochMilliseconds()

            val filteredDetails = uiState.incomeDetails.filter { incomeDetail ->
                incomeDetail.visitTime in startOfDay until endOfDay
            }

            val totalIncome = filteredDetails.sumOf { it.fee } // Calculate total income

            TodayIncomeDetailsUiState(
                isLoading = uiState.isLoading,
                incomeDetails = filteredDetails,
                totalDailyIncome = totalIncome, // Pass total income
                error = uiState.error
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = TodayIncomeDetailsUiState()
        )

    init {
        getAllIncomeDetails()
    }

    private fun getAllIncomeDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            incomeDetailsUseCase().fold(
                onSuccess = { details ->
                    _uiState.update {
                        it.copy(isLoading = false, incomeDetails = details)
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, error = exception.message)
                    }
                }
            )
        }
    }

    fun onPreviousDay() {
        _selectedDate.update { it - LocalTime.DAY_IN_MILLIS }
    }

    fun onNextDay() {
        _selectedDate.update {
            val nextDay = it + LocalTime.DAY_IN_MILLIS
            if (nextDay < LocalTime.getStartOfTodayTimeStamp()) {
                nextDay
            } else {
                LocalTime.getStartOfTodayTimeStamp()
            }
        }
    }

    fun isToday(date: Long): Boolean {
        val selectedLocalDate = LocalTime.timeStampToLocalDateTime(date).date
        val todayLocalDate =
            LocalTime.timeStampToLocalDateTime(LocalTime.getCurrentTimeMillis()).date
        return selectedLocalDate == todayLocalDate
    }

    fun onClearError() {
        _uiState.update { it.copy(error = null) }
    }
}
