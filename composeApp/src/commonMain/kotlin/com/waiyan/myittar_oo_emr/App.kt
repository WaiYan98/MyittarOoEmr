package com.waiyan.myittar_oo_emr

import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.waiyan.myittar_oo_emr.screen.component.patient_history_screen.PatientHistoryScreen
import com.waiyan.myittar_oo_emr.screen.component.patient_screen.PatientViewModel
import com.waiyan.myittar_oo_emr.screen.component.report_screen.ReportScreen
import kotlinx.coroutines.DelicateCoroutinesApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(DelicateCoroutinesApi::class)
@Composable
@Preview
fun App(patientViewModel: PatientViewModel = koinViewModel<PatientViewModel>()) {

    val patients by patientViewModel.patientFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {

    }

    ReportScreen()
}