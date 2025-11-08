package com.waiyan.myittar_oo_emr.screen.component

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.PatientFormScreen
import com.waiyan.myittar_oo_emr.screen.component.patient_history_screen.PatientHistoryScreen
import com.waiyan.myittar_oo_emr.screen.component.patient_screen.PatientScreen
import com.waiyan.myittar_oo_emr.screen.component.report_screen.ReportScreen
import kotlinx.serialization.Serializable

@Serializable
object PatientScreen

@Serializable
object PatientHistoryScreen

@Serializable
object PatientFormScreen

@Serializable
object ReportScreen

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
            ReportScreen(navController)
        }
    }


}