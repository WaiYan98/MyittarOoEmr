package com.waiyan.myittar_oo_emr.screen.component

import PatientScreen
import ReportScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.PatientFormScreen
import com.waiyan.myittar_oo_emr.screen.component.patient_history_screen.PatientHistoryScreen
import com.waiyan.myittar_oo_emr.screen.component.report_screen.MonthlyIncomeDetailsScreen
import com.waiyan.myittar_oo_emr.screen.component.report_screen.TodayIncomeDetailsScreen
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.waiyan.myittar_oo_emr.screen.component.lock_screen.PinSettingsViewModel
import com.waiyan.myittar_oo_emr.screen.component.lock_screen.PinLockScreen

@Serializable
object PatientScreen

@Serializable
data class PatientHistoryScreen(val patientId: Long)

@Serializable
object PatientFormScreen

@Serializable
object ReportScreen // Renamed from ReportScreen

@Serializable
object TodayIncomeDetailsScreen

@Serializable
object MonthlyIncomeDetailsScreen


@Composable
fun MyittarOoEmrApp() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = PatientScreen
    ) {
        composable<PatientScreen> {
            PatientScreen(navController) { navController.navigate(PatientFormScreen) }
        }

        composable<PatientHistoryScreen> {
            PatientHistoryScreen { navController.navigateUp() }
        }

        composable<PatientFormScreen> {
            PatientFormScreen(navController = navController)
        }

        composable<ReportScreen> {
            val entryPinViewModel: PinSettingsViewModel = koinViewModel() // Get ViewModel for this entry
//            Log.d("ViewModelInstance", "MyittarOoEmrApp (entry) ViewModel: ${entryPinViewModel.hashCode()}")
            val entryIsUnlocked by entryPinViewModel.isUnlocked.collectAsStateWithLifecycle()
            if (!entryIsUnlocked) {
                PinLockScreen(
                    navController = navController, // Pass navController here
                    onPinCorrect = { /* This callback is not strictly necessary anymore as we observe entryIsUnlocked */ },
                    viewModel = entryPinViewModel // Pass the ViewModel down
                )
            } else {
                ReportScreen(navController)
            }
        }

        composable<TodayIncomeDetailsScreen> {
            TodayIncomeDetailsScreen(navController)
        }

        composable<MonthlyIncomeDetailsScreen> {
            MonthlyIncomeDetailsScreen(navController)
        }

    }


}