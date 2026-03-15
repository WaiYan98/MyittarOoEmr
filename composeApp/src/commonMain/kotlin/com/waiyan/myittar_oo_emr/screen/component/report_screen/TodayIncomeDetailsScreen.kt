import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft // Modified import
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight // Modified import
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
import com.waiyan.myittar_oo_emr.screen.component.PatientHistoryScreen
import com.waiyan.myittar_oo_emr.screen.component.ShowLoading
import com.waiyan.myittar_oo_emr.screen.component.TableBody
import com.waiyan.myittar_oo_emr.screen.component.TableHeader
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.util.LocalTime
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import com.waiyan.myittar_oo_emr.screen.component.TotalIncome
import com.waiyan.myittar_oo_emr.screen.component.report_screen.TodayIncomeDetailsViewModel

data class TodayIncomeDetailsUiState(
    val isLoading: Boolean = false,
    val incomeDetails: List<IncomeDetail> = emptyList(),
    val totalDailyIncome: Long = 0L,
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
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                                    contentDescription = "Previous day"
                                ) // Modified usage
                            }
                            Text(LocalTime.getHumanDate(selectedDate), fontSize = 20.sp)
                            IconButton(
                                onClick = {
                                    viewModel.onNextDay()
                                },
                                enabled = !viewModel.isToday(selectedDate)
                            ) {
                                Icon(
                                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                    contentDescription = "Next day"
                                ) // Modified usage
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
                    .padding(24.dp)
            ) {
                if (uiState.isLoading) {
                    ShowLoading()
                } else {
                    // Table Header
                    TableHeader(
                        title1 = "No.",
                        title2 = "Patient Name",
                        title3 = "Time",
                        title4 = "Fee" // Empty for spacing
                    )

                    // List of income details
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        itemsIndexed(uiState.incomeDetails) { index, detail -> // Changed to itemsIndexed
                            TableBody(
                                data1 = "${index + 1}", // Use index for numbering
                                data2 = detail.patientName,
                                onData2Click = {
                                    navController.navigate(PatientHistoryScreen(detail.patientId))
                                },
                                data3 = LocalTime.getHumanTime(detail.visitTime),
                                data4 = detail.fee.toString()
                            )
                        }

                        item {
                            Spacer(modifier = Modifier.height(16.dp))
                            TotalIncome(uiState.totalDailyIncome)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}