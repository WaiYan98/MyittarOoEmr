package com.waiyan.myittar_oo_emr.screen.component.report_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
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

    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.onClearError()
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
                                fontSize = 20.sp
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
                        title1 = "",
                        title2 = "Date",
                        title3 = "Patients",
                        title4 = "Income"
                    )

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.monthlyIncomeDetails) { detail ->
                            TableBody(
                                data1 ="",
                                data2 = detail.date,
                                data3 = detail.patientSeenNumber.toString(),
                                data4 = detail.income.toString()
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(thickness = 2.dp)

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Total Income:",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${uiState.totalMonthlyIncome} MMK",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
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
