package com.waiyan.myittar_oo_emr

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.waiyan.myittar_oo_emr.data.Patient
import com.waiyan.myittar_oo_emr.screen.PatientViewModel
import kotlinx.coroutines.DelicateCoroutinesApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(DelicateCoroutinesApi::class)
@Composable
@Preview
fun App(patientViewModel: PatientViewModel = koinViewModel<PatientViewModel>()) {

    val patients by patientViewModel.patientFlow.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = Unit) {
        patientViewModel.insertPatient(Patient(name = "waiyan", age = 27))
        patientViewModel.insertPatient(Patient(name = "john", age = 29))
    }

    println("Patient list size ${patients.size}")

    MaterialTheme {

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(patients) {
                Text(
                    it.name,
                    fontSize = 32.sp
                )
            }
        }
    }
}