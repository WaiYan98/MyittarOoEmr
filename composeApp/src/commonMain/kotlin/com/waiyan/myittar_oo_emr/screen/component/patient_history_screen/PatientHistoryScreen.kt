package com.waiyan.myittar_oo_emr.screen.component.patient_history_screen

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.waiyan.myittar_oo_emr.data.PatientWithDetail
import com.waiyan.myittar_oo_emr.data.VisitAndFollowUpForm
import com.waiyan.myittar_oo_emr.data.entity.Patient
import com.waiyan.myittar_oo_emr.screen.component.AppBar
import com.waiyan.myittar_oo_emr.screen.component.DisplayInfoCard
import com.waiyan.myittar_oo_emr.screen.component.ShowLoading
import com.waiyan.myittar_oo_emr.screen.component.TableBody
import com.waiyan.myittar_oo_emr.screen.component.TableHeader
import com.waiyan.myittar_oo_emr.screen.component.Title
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.ShowFollowUpForm
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.VisitForm
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.util.LocalTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PatientHistoryScreen(
    viewModel: PatientHistoryViewModel = koinViewModel<PatientHistoryViewModel>(),
    onClickBack: () -> Unit
) {

    val uiState: PatientHistoryUiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    var isClickedAddNewVisit by remember { mutableStateOf(false) }
    var diagnosis by remember { mutableStateOf("") }
    var prescription by remember { mutableStateOf("") }
    var fee by remember { mutableStateOf("") }
    var followUpTimeStamp by remember { mutableStateOf(0L) }
    var followUpDate by remember { mutableStateOf("") }
    var reasonForFollowUp by remember { mutableStateOf("") }
    var isChecked by remember { mutableStateOf(false) }
    var editPatientInfoState by remember { mutableStateOf(false) }
    var editMedicalInfoState by remember { mutableStateOf(false) }
    var editContactInfoState by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var allergies by remember { mutableStateOf("") }
    var chronicConditions by remember { mutableStateOf("") }
    var currentMedication by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }


    LaunchedEffect(key1 = uiState.error) {
        uiState.error?.let {
            snackBarHostState.showSnackbar(it)
            viewModel.onClearError()
        }
    }

    LaunchedEffect(key1 = uiState.notify) {
        uiState.notify?.let {
            //Later this state will be controlled in ViewModel
            diagnosis = ""
            prescription = ""
            fee = ""
            followUpDate = ""
            reasonForFollowUp = ""
            isClickedAddNewVisit = false
            snackBarHostState.showSnackbar(it)
            viewModel.onClearNotify()
        }
    }

    LaunchedEffect(key1 = uiState.success) {
        uiState.success?.let { patientWithDetail ->
            name = patientWithDetail.patient.name
            age = patientWithDetail.patient.age.toString()
            gender = patientWithDetail.patient.gender
            phone = patientWithDetail.patient.phone
            address = patientWithDetail.patient.address
            allergies = patientWithDetail.medicalInfo.allergies
            chronicConditions = patientWithDetail.medicalInfo.chronicConditions
            currentMedication = patientWithDetail.medicalInfo.currentMedication
            phone = patientWithDetail.patient.phone
            address = patientWithDetail.patient.address
        }
    }


    MyAppTheme {
        Scaffold(
            topBar = { AppBar(title = "History", onClickBack) },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHostState)
            }
        ) { values ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        values,
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                if (uiState.isLoading) {
                    ShowLoading()
                }

                uiState.success?.let { patientWithDetail ->

                    PatientHistoryDisplay(
                        patientWithDetail = patientWithDetail,
                        isClickedAddNewVisit = isClickedAddNewVisit,
                        diagnosis = diagnosis,
                        onDiagnosisChange = { diagnosis = it },
                        prescription = prescription,
                        onPrescriptionChange = { prescription = it },
                        fee = fee,
                        onFeeChange = { fee = it },
                        followUpTimeStamp = followUpTimeStamp,
                        onFollowUpTimeStampChange = { followUpTimeStamp = it },
                        followUpDate = followUpDate,
                        onFollowUpDateChange = { followUpDate = it },
                        reasonForFollowUp = reasonForFollowUp,
                        onReasonForFollowUpChange = { reasonForFollowUp = it },
                        isChecked = isChecked,
                        onCheckedChange = { isChecked = it },
                        onClickAddNewVisit = { isClickedAddNewVisit = true },
                        onClickCancel = { isClickedAddNewVisit = false },
                        onClickSave = { patientId ->
                            viewModel.insertVisitAndFollowUp(
                                VisitAndFollowUpForm(
                                    patientId = patientId,
                                    diagnosis = diagnosis,
                                    prescription = prescription,
                                    fee = fee,
                                    followUpDate = followUpTimeStamp,
                                    reasonForFollowUp = reasonForFollowUp,
                                ),
                                isCheckedFollowUpForm = isChecked
                            )

                        },
                        onClickEditPatientInfo = {
                            if (editPatientInfoState) {
                                val editPatient = Patient(
                                    id = patientWithDetail.patient.id,
                                    name = name,
                                    age = age.toIntOrNull() ?: 0,
                                    gender = gender,
                                    phone = phone,
                                    address = address
                                )
                                viewModel.updatePatientInfo(
                                    patientWithDetail.patient,
                                    editPatient
                                )
                            }
                            editPatientInfoState = !editPatientInfoState
                        },
                        onclickEditContactInfo = {
                            editContactInfoState = !editContactInfoState
                        },
                        onClickEditMedicalInfo = {
                            editMedicalInfoState = !editMedicalInfoState
                        },
                        onEditPatientNameChange = { name = it },
                        onEditPatientAgeChange = { age = it },
                        onEditPatientGenderChange = { gender = it },
                        onEditPatientPhoneChange = { phone = it },
                        onEditPatientAddressChange = { address = it },
                        onEditPatientAllergiesChange = { allergies = it },
                        onEditPatientChronicConditionsChange = { chronicConditions = it },
                        onEditPatientCurrentMedicationChange = { currentMedication = it },
                        editPatientInfoState = editPatientInfoState,
                        editMedicalInfoState = editMedicalInfoState,
                        editContactInfoState = editContactInfoState,
                        name = name,
                        age = age,
                        gender = gender,
                        phone = phone,
                        address = address,
                        allergies = allergies,
                        chronicConditions = chronicConditions,
                        currentMedication = currentMedication
                    )
                }
            }
        }
    }

}

@Composable
fun PatientHistoryDisplay(
    patientWithDetail: PatientWithDetail,
    isClickedAddNewVisit: Boolean,
    diagnosis: String,
    onDiagnosisChange: (String) -> Unit,
    prescription: String,
    onPrescriptionChange: (String) -> Unit,
    fee: String,
    onFeeChange: (String) -> Unit,
    followUpTimeStamp: Long,
    onFollowUpTimeStampChange: (Long) -> Unit,
    followUpDate: String,
    onFollowUpDateChange: (String) -> Unit,
    reasonForFollowUp: String,
    onReasonForFollowUpChange: (String) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClickCancel: () -> Unit,
    onClickSave: (Long) -> Unit,
    onClickEditPatientInfo: () -> Unit,
    onClickEditMedicalInfo: () -> Unit,
    onclickEditContactInfo: () -> Unit,
    onClickAddNewVisit: () -> Unit,
    onEditPatientNameChange: (String) -> Unit,
    onEditPatientAgeChange: (String) -> Unit,
    onEditPatientGenderChange: (String) -> Unit,
    onEditPatientPhoneChange: (String) -> Unit,
    onEditPatientAddressChange: (String) -> Unit,
    onEditPatientAllergiesChange: (String) -> Unit,
    onEditPatientChronicConditionsChange: (String) -> Unit,
    onEditPatientCurrentMedicationChange: (String) -> Unit,
    editPatientInfoState: Boolean,
    editMedicalInfoState: Boolean,
    editContactInfoState: Boolean,
    name: String,
    age: String,
    gender: String,
    phone: String,
    address: String,
    allergies: String,
    chronicConditions: String,
    currentMedication: String
) {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalAlignment = Alignment.Start
    ) {
        items(1) {

            Row {
                TextField(
                    value = name,
                    onValueChange = onEditPatientNameChange,
                    readOnly = !editPatientInfoState,
                    enabled = editPatientInfoState,
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = if (editPatientInfoState) MaterialTheme.colorScheme.primary else Color.Transparent,
                        focusedContainerColor = if (editPatientInfoState) MaterialTheme.colorScheme.primary else Color.Transparent,
                        unfocusedContainerColor = if (editPatientInfoState) MaterialTheme.colorScheme.primary else Color.Transparent,
                        focusedIndicatorColor = if (editPatientInfoState) MaterialTheme.colorScheme.primary else Color.Transparent,
                        unfocusedIndicatorColor = if (editPatientInfoState) MaterialTheme.colorScheme.primary else Color.Transparent,
                        disabledIndicatorColor = if (editPatientInfoState) MaterialTheme.colorScheme.primary else Color.Transparent
                    )
                )

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = onClickEditPatientInfo) {
                    Icon(
                        imageVector = if (editPatientInfoState) Icons.Filled.Check else Icons.Filled.Edit,
                        contentDescription = if (editPatientInfoState) "save_icon" else "edit_icon"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Age",
                age,
                isEditing = editPatientInfoState,
                onEditValueChange = onEditPatientAgeChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Gender",
                gender,
                isEditing = editPatientInfoState,
                onEditValueChange = onEditPatientGenderChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Id",
                patientWithDetail.patient.id.toString(),
                isEditing = false,
                onEditValueChange = {})

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Title(
                    "!! CRITICAL MEDICAL INFO !!",
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = onClickEditMedicalInfo) {
                    Icon(
                        imageVector = if (editMedicalInfoState) Icons.Filled.Check else Icons.Filled.Edit,
                        contentDescription = if (editMedicalInfoState) "save_icon" else "edit_icon"
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Allergies",
                allergies,
                isEditing = editMedicalInfoState,
                onEditValueChange = onEditPatientAllergiesChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Chronic Conditions",
                chronicConditions,
                isEditing = editMedicalInfoState,
                onEditValueChange = onEditPatientChronicConditionsChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Current Medication",
                currentMedication,
                isEditing = editMedicalInfoState,
                onEditValueChange = onEditPatientCurrentMedicationChange
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row {
                Title(
                    "Contact & Address Information",
                    fontSize = 32.sp
                )

                Spacer(modifier = Modifier.width(16.dp))

                IconButton(onClick = onclickEditContactInfo) {
                    Icon(
                        imageVector = if (editContactInfoState) Icons.Filled.Check else Icons.Filled.Edit,
                        contentDescription = if (editContactInfoState) "save_icon" else "edit_icon "
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Phone", phone,
                isEditing = editContactInfoState,
                onEditValueChange = onEditPatientPhoneChange
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard(
                "Address",
                address,
                isEditing = editContactInfoState,
                onEditValueChange = onEditPatientAddressChange
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onClickAddNewVisit,
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("+ ADD NEW VISIT")
            }

            Spacer(modifier = Modifier.height(32.dp))

            if (isClickedAddNewVisit) {
                VisitForm(
                    diagnosis = diagnosis,
                    onDiagnosisChange = onDiagnosisChange,
                    prescription = prescription,
                    onPrescriptionChange = onPrescriptionChange,
                    fee = fee,
                    onFeeChange = onFeeChange
                )

                Spacer(modifier = Modifier.height(32.dp))

                ShowFollowUpForm(
                    followUpTimeStamp = followUpTimeStamp,
                    followUpTimeStampChange = onFollowUpTimeStampChange,
                    isChecked = isChecked,
                    onCheckedChange = onCheckedChange,
                    followUpDate = followUpDate,
                    onFollowUpDateChange = onFollowUpDateChange,
                    reasonForFollowUp = reasonForFollowUp,
                    onReasonForFollowUpChange = onReasonForFollowUpChange
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(onClick = onClickCancel) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(16.dp))


                    Button(onClick = { onClickSave(patientWithDetail.patient.id) }) {
                        Text("Save")
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Title(text = "Visit History", fontSize = 32.sp)

            Spacer(modifier = Modifier.height(24.dp))
        }

        item {
            TableHeader(
                title1 = "Date",
                title2 = "Diagnosis",
                title3 = "Prescription",
                title4 = "Fee"
            )
        }

        items(patientWithDetail.visits.size) {
            TableBody(
                data1 = LocalTime.getHumanDate(patientWithDetail.visits[it].date),
                data2 = patientWithDetail.visits[it].diagnosis,
                data3 = patientWithDetail.visits[it].prescription,
                data4 = patientWithDetail.visits[it].fee.toString()
            )

        }

    }

}


