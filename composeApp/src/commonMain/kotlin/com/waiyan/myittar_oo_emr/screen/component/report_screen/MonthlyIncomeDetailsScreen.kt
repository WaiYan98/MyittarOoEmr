package com.waiyan.myittar_oo_emr.screen.component.report_screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.waiyan.myittar_oo_emr.screen.component.ShowLoading
import com.waiyan.myittar_oo_emr.screen.component.TableBody
import com.waiyan.myittar_oo_emr.screen.component.TableHeader
import com.waiyan.myittar_oo_emr.screen.component.TotalIncome
import com.waiyan.myittar_oo_emr.screen.component.formatWithCommas
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.util.LocalTime
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MonthlyIncomeDetailsScreen(
    navController: NavController,
    viewModel: MonthlyIncomeDetailsViewModel = koinViewModel<MonthlyIncomeDetailsViewModel>()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedDate by viewModel.selectedDate.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    var showDatePicker by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onClearError()
        }
    }

    if (showDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = selectedDate,
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    return utcTimeMillis <= LocalTime.getCurrentTimeMillis()
                }
            }
        )
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        viewModel.onDateSelected(it)
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    MyAppTheme {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            IconButton(onClick = { viewModel.onPreviousMonth() }) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Previous month"
                                )
                            }
                            Text(
                                text = selectedDate.toMonthYearString(),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.clickable {
                                    showDatePicker = true
                                }
                            )
                            IconButton(
                                onClick = { viewModel.onNextMonth() },
                                enabled = !viewModel.isCurrentMonth(selectedDate)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Next month"
                                )
                            }
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { values ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values)
                    .padding(16.dp)
            ) {
                if (uiState.isLoading) {
                    ShowLoading()
                } else {
                    TableHeader(
                        title1 = "No.",
                        title2 = "Date",
                        title3 = "Patients",
                        title4 = "Income"
                    )

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(uiState.monthlyIncomeDetails) { index, detail ->
                            TableBody(
                                data1 = "${index + 1}",
                                data2 = detail.date,
                                data3 = detail.patientSeenNumber.toString(),
                                data4 = detail.income.toString().formatWithCommas()
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            TotalIncome(uiState.totalMonthlyIncome.toString())
                            Spacer(modifier = Modifier.height(16.dp))

                        }
                    }

                }
            }
        }
    }
}

private fun Long.toMonthYearString(): String {
    val date = LocalTime.timeStampToLocalDateTime(this)
    val monthName = date.month.name.lowercase().replaceFirstChar { it.uppercase() }
    return "$monthName ${date.year}"
}
