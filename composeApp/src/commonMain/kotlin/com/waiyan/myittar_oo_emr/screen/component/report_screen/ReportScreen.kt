import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.waiyan.myittar_oo_emr.data.MonthlyIncome
import com.waiyan.myittar_oo_emr.data.UpcomingFollowUp
import com.waiyan.myittar_oo_emr.screen.component.InputField
import com.waiyan.myittar_oo_emr.screen.component.MonthlyIncomeDetailsScreen
import com.waiyan.myittar_oo_emr.screen.component.MyittarOoEmrAppBar
import com.waiyan.myittar_oo_emr.screen.component.ReportCard
import com.waiyan.myittar_oo_emr.screen.component.TableBody
import com.waiyan.myittar_oo_emr.screen.component.TableHeader
import com.waiyan.myittar_oo_emr.screen.component.Title
import com.waiyan.myittar_oo_emr.screen.component.TitleCard
import com.waiyan.myittar_oo_emr.screen.component.TodayIncomeDetailsScreen
import com.waiyan.myittar_oo_emr.screen.component.formatWithCommas
import com.waiyan.myittar_oo_emr.screen.component.report_screen.ReportScreenViewModel
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.util.LocalTime
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportScreenViewModel = koinViewModel<ReportScreenViewModel>()
) {

    var selectedPageIndex by remember { mutableStateOf(1) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    val monthlyIncome by viewModel.monthlyIncomes.collectAsStateWithLifecycle()

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            confirmButton = {
                Button(
                    content = { Text("Confirm") },
                    onClick = {
                        datePickerState.selectedDateMillis?.let { timeStamp ->
                            viewModel.onDateRangeChanged(timeStamp, uiState.endDate)
                            showStartDatePicker = false
                        }
                    }
                )
            },
            dismissButton = {
                Button(
                    content = { Text("Cancel") },
                    onClick = {
                        showStartDatePicker = false
                    }
                )
            },
            onDismissRequest = {
                showStartDatePicker = false
            },
            content = {
                DatePicker(state = datePickerState)
            }
        )
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            confirmButton = {
                Button(
                    content = { Text("Confirm") },
                    onClick = {
                        datePickerState.selectedDateMillis?.let { timeStamp ->
                            viewModel.onDateRangeChanged(uiState.startDate, timeStamp)
                            showEndDatePicker = false
                        }
                    }
                )
            },
            dismissButton = {
                Button(
                    content = { Text("Cancel") },
                    onClick = {
                        showEndDatePicker = false
                    }
                )
            },
            onDismissRequest = {
                showEndDatePicker = false
            },
            content = {
                DatePicker(state = datePickerState)
            }
        )
    }

    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            snackBarHostState.showSnackbar(it)
        }
        viewModel.onClearError()
    }

    MyAppTheme {
        Scaffold(
            topBar = {
                MyittarOoEmrAppBar(
                    modifier = Modifier,
                    onClickReport = { selectedPageIndex = 1 },
                    onClickHome = {
                        navController.navigateUp()
                        selectedPageIndex = 0
                    },
                    selectedPageIndex = selectedPageIndex,
                    enabled = true
                )
            },
            snackbarHost = {
                SnackbarHost(snackBarHostState)
            }
        ) { values ->

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(values),
                contentAlignment = Alignment.TopCenter
            ) {

                uiState.success?.let { report ->
                    ReportDisplay(
                        navController = navController,
                        patientSeen = report.todayPatientsSeen.toString(),
                        todayIncome = report.todayIncome.toString(),
                        thisMonthIncome = report.thisMonthIncome.toString(),
                        thisYearIncome = report.thisYearIncome.toString(),
                        upcomingFollowUps = report.upcomingFollowUps,
                        monthlyIncomes = monthlyIncome,
                        startDate = LocalTime.getHumanDate(uiState.startDate),
                        endDate = LocalTime.getHumanDate(uiState.endDate),
                        onStartDateClick = { showStartDatePicker = true },
                        onEndDateClick = { showEndDatePicker = true }
                    )
                }
            }
        }
    }
}

@Composable
fun ReportDisplay(
    navController: NavController,
    patientSeen: String,
    todayIncome: String,
    thisMonthIncome: String,
    thisYearIncome: String,
    monthlyIncomes: List<MonthlyIncome>,
    upcomingFollowUps: List<UpcomingFollowUp>,
    startDate: String,
    endDate: String,
    onStartDateClick: () -> Unit,
    onEndDateClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        item {

            Title("REPORTS & FOLLOW-UPS", fontSize = 32.sp)

            Spacer(modifier = Modifier.height(16.dp))

            TitleCard(
                title1 = "Today's Summary",
                title2 = "Income Overview"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReportCard(
                title1 = "Patients Seen",
                value1 = patientSeen.formatWithCommas(),
                title2 = "Today's Income",
                value2 = "${todayIncome.formatWithCommas()} MMK",
                onclick1 = {},
                onClick2 = { navController.navigate(TodayIncomeDetailsScreen) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReportCard(
                title1 = "This Month",
                value1 = "${thisMonthIncome.formatWithCommas()} MMK",
                title2 = "",
                value2 = "",
                onclick1 = {navController.navigate(MonthlyIncomeDetailsScreen)},
                onClick2 = {}
            )

            Spacer(Modifier.height(32.dp))

            Title("Monthly Income", fontSize = 24.sp)

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier.weight(1f)) {
                    InputField(
                        modifier = Modifier.fillMaxWidth(),
                        value = startDate,
                        onValueChange = {},
                        label = "Start Date",
                        placeholder = "Pick Start Date",
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = "Start Date Icon"
                            )
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Transparent)
                            .clickable { onStartDateClick() }
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Box(modifier = Modifier.weight(1f)) {
                    InputField(
                        modifier = Modifier.fillMaxWidth(),
                        value = endDate,
                        onValueChange = {},
                        label = "End Date",
                        placeholder = "Pick End Date",
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Filled.DateRange,
                                contentDescription = "End Date Icon"
                            )
                        }
                    )
                    Spacer(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Transparent)
                            .clickable { onEndDateClick() }
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
        }

        item {
            TableHeader(
                title1 = "Month",
                title2 = "",
                title3 = "",
                title4 = "Total Income"
            )
        }

        items(monthlyIncomes) { monthlyIncome ->
            // Placeholder for the list
            TableBody(
                data1 = monthlyIncome.month,
                data2 = "",
                data3 = "",
                data4 = monthlyIncome.income.toString().formatWithCommas()
            )
        }

        item {
            Spacer(Modifier.height(32.dp))
        }

        item {
            Title("Upcoming Follow-Ups", fontSize = 24.sp)

            Spacer(Modifier.height(16.dp))

        }

        item {
            TableHeader(
                title1 = "Date",
                title2 = "Patient Name",
                title3 = "Reason for Visit",
                title4 = "Time Until"
            )
        }

        items(upcomingFollowUps) {
            TableBody(
                data1 = it.followUpDate,
                data2 = it.patientName,
                data3 = it.reasonForFollowUp,
                data4 = it.timeUntil.formatWithCommas()
            )
        }
    }
}
