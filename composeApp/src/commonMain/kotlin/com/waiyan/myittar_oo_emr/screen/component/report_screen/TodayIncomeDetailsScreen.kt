package com.waiyan.myittar_oo_emr.screen.component.report_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.waiyan.myittar_oo_emr.data.IncomeDetail
import com.waiyan.myittar_oo_emr.screen.component.ShowLoading
import com.waiyan.myittar_oo_emr.screen.component.TableBody
import com.waiyan.myittar_oo_emr.screen.component.TableHeader
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.util.LocalTime
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember

data class TodayIncomeDetailsUiState(
    val isLoading: Boolean = false,
    val incomeDetails: List<IncomeDetail> = emptyList(),
    val error: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayIncomeDetailsScreen(
    navController: NavController,
    viewModel: TodayIncomeDetailsViewModel = koinViewModel<TodayIncomeDetailsViewModel>()
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
                            IconButton(onClick = {
                                viewModel.onPreviousDay()
                            }) {
                                Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Previous day")
                            }
                            Text(LocalTime.getHumanDate(selectedDate), fontSize = 20.sp)
                            IconButton(
                                onClick = {
                                    viewModel.onNextDay()
                                },
                                enabled = !viewModel.isToday(selectedDate)
                            ) {
                                Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next day")
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
                    // Table Header
                    TableHeader(
                        title1 = "Patient Name",
                        title2 = "Time",
                        title3 = "Fee",
                        title4 = "" // Empty for spacing
                    )

                    // List of income details
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uiState.incomeDetails) { detail ->
                            TableBody(
                                data1 = detail.patientName,
                                data2 = LocalTime.getHumanTime(detail.visitTime), // Assuming getHumanTime exists
                                data3 = detail.fee.toString(),
                                data4 = "" // Empty for spacing
                            )
                        }
                    }
                }
            }
        }
    }
}