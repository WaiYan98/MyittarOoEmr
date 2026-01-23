package com.waiyan.myittar_oo_emr.screen.component.report_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.waiyan.myittar_oo_emr.data.IncomeDetail
import com.waiyan.myittar_oo_emr.data.MonthlyIncomeDetail
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
import kotlinx.datetime.*
import kotlin.time.ExperimentalTime

data class MonthlyIncomeDetailsUiState(
    val isLoading: Boolean = false,
    val monthlyIncomeDetails: List<MonthlyIncomeDetail> = emptyList(),
    val error: String? = null
)

class MonthlyIncomeDetailsViewModel(
    private val incomeDetailsUseCase: IncomeDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonthlyIncomeDetailsUiState())
    private val _allIncomeDetails = MutableStateFlow<List<IncomeDetail>>(emptyList())

    private val _selectedDate = MutableStateFlow(LocalTime.getCurrentTimeMillis())
    val selectedDate: StateFlow<Long> = _selectedDate.asStateFlow()

    val uiState: StateFlow<MonthlyIncomeDetailsUiState> =
        combine(
            _uiState,
            _selectedDate,
            _allIncomeDetails
        ) { uiState, date, allDetails ->
            val localDate = LocalTime.timeStampToLocalDateTime(date).date
            val startOfMonth = localDate.atStartOfMonth()
            val endOfMonth = localDate.atEndOfMonth()

            val detailsForMonth = allDetails.filter {
                val detailDate = LocalTime.timeStampToLocalDateTime(it.visitTime).date
                detailDate >= startOfMonth && detailDate <= endOfMonth
            }

            val groupedByDay = detailsForMonth.groupBy {
                LocalTime.timeStampToLocalDateTime(it.visitTime).date
            }

            val monthlyDetails = groupedByDay.map { (date, details) ->
                MonthlyIncomeDetail(
                    date = "${date.dayOfMonth.toString().padStart(2,'0')}/${date.monthNumber.toString().padStart(2,'0')}/${date.year}",
                    patientSeenNumber = details.distinctBy { it.patientName }.size,
                    income = details.sumOf { it.fee }
                )
            }.sortedBy { it.date }

            MonthlyIncomeDetailsUiState(
                isLoading = uiState.isLoading,
                monthlyIncomeDetails = monthlyDetails,
                error = uiState.error
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MonthlyIncomeDetailsUiState()
        )

    init {
        getAllIncomeDetails()
    }

    private fun getAllIncomeDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            incomeDetailsUseCase().fold(
                onSuccess = { details ->
                    _allIncomeDetails.value = details
                    _uiState.update { it.copy(isLoading = false) }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(isLoading = false, error = exception.message)
                    }
                }
            )
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onPreviousMonth() {
        _selectedDate.update {
            val currentLocalDate = LocalTime.timeStampToLocalDateTime(it).date
            currentLocalDate.minus(1, DateTimeUnit.MONTH).atStartOfMonth().atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
        }
    }

    @OptIn(ExperimentalTime::class)
    fun onNextMonth() {
        _selectedDate.update {
            val currentLocalDate = LocalTime.timeStampToLocalDateTime(it).date
            val nextMonthDate = currentLocalDate.plus(1, DateTimeUnit.MONTH)
            val today = LocalTime.timeStampToLocalDateTime(LocalTime.getCurrentTimeMillis()).date

            if (nextMonthDate.year == today.year && nextMonthDate.month == today.month) {
                // If next month is the current month, set to today's timestamp
                LocalTime.getCurrentTimeMillis()
            } else if (nextMonthDate.year > today.year || (nextMonthDate.year == today.year && nextMonthDate.month > today.month)) {
                // If next month is in the future, do nothing
                it
            }
            else {
                // Otherwise, go to the beginning of next month
                nextMonthDate.atStartOfMonth().atStartOfDayIn(TimeZone.currentSystemDefault()).toEpochMilliseconds()
            }
        }
    }
    
    fun onClearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun isCurrentMonth(date: Long): Boolean {
        val selectedLocalDate = LocalTime.timeStampToLocalDateTime(date).date
        val todayLocalDate =
            LocalTime.timeStampToLocalDateTime(LocalTime.getCurrentTimeMillis()).date
        return selectedLocalDate.year == todayLocalDate.year && selectedLocalDate.month == todayLocalDate.month
    }

    private fun LocalDate.atStartOfMonth(): LocalDate {
        return LocalDate(this.year, this.month, 1)
    }

    private fun LocalDate.atEndOfMonth(): LocalDate {
        val nextMonth = this.plus(1, DateTimeUnit.MONTH)
        val firstDayOfNextMonth = LocalDate(nextMonth.year, nextMonth.month, 1)
        return firstDayOfNextMonth.minus(1, DateTimeUnit.DAY)
    }
}
