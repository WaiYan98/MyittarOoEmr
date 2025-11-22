package com.waiyan.myittar_oo_emr.screen.component.patient_form_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.waiyan.myittar_oo_emr.screen.component.AppBar
import com.waiyan.myittar_oo_emr.screen.component.InputField
import com.waiyan.myittar_oo_emr.screen.component.LargeInputField
import com.waiyan.myittar_oo_emr.screen.component.Title
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.util.LocalTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PatientFormScreen(
    viewModel: PatientFormViewModel = koinViewModel<PatientFormViewModel>(),
    navController: NavController
) {

    var name: String by remember { mutableStateOf("") }
    var age: String by remember { mutableStateOf("") }
    var phone: String by remember { mutableStateOf("") }
    var address: String by remember { mutableStateOf("") }
    var allergies: String by remember { mutableStateOf("") }
    var chronicConditions: String by remember { mutableStateOf("") }
    var currentMedication: String by remember { mutableStateOf("") }
    var prescription: String by remember { mutableStateOf("") }
    var fee: String by remember { mutableStateOf("") }
    var diagnosis: String by remember { mutableStateOf("") }
    var followUpDate: String by remember { mutableStateOf("") }
    var reasonForFollowUp: String by remember { mutableStateOf("") }
    val snackBarHotState = remember { SnackbarHostState() }
    var isChecked by remember { mutableStateOf(false) }
    var followUpTimeStamp by remember { mutableStateOf(0L) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var selectedGender by remember { mutableStateOf(Gender.MALE) }

    LaunchedEffect(key1 = uiState.success) {
        if (uiState.success == "Success") {
            navController.navigateUp()
        }
    }

    LaunchedEffect(key1 = uiState.onError) {
        uiState.onError?.let { error ->
            snackBarHotState.showSnackbar(error)
            viewModel.onClearError()
        }
    }

    MyAppTheme {
        Scaffold(
            topBar = {
                AppBar(title = "Form", onClickBack = { navController.navigateUp() })
            },
            snackbarHost = {
                SnackbarHost(hostState = snackBarHotState)
            }
        ) { values ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Form(
                    Modifier
                        .fillMaxWidth(0.75f)
                        .padding(values),
                    name = name,
                    onNameChange = { name = it },
                    age = age,
                    onAgeChange = { age = it },
                    phone = phone,
                    onPhoneChange = { phone = it },
                    address = address,
                    onAddressChange = { address = it },
                    allergies = allergies,
                    onAllergiesChange = { allergies = it },
                    chronicConditions = chronicConditions,
                    onChronicConditionsChange = { chronicConditions = it },
                    currentMedication = currentMedication,
                    onCurrentMedicationChange = { currentMedication = it },
                    prescription = prescription,
                    onPrescriptionChange = { prescription = it },
                    fee = fee,
                    onFeeChange = { fee = it },
                    diagnosis = diagnosis,
                    onDiagnosisChange = { diagnosis = it },
                    followUpDate = followUpDate,
                    onFollowUpDateChange = { followUpDate = it },
                    reasonForFollowUp = reasonForFollowUp,
                    onReasonForFollowUpChange = { reasonForFollowUp = it },
                    selectedOption = selectedGender,
                    onSelectGender = { selectedGender = it },
                    onClickSave = {
                        viewModel.insertPatientInfo(
                            name = name,
                            age = age,
                            gender = selectedGender,
                            phone = phone,
                            address = address,
                            allergies = allergies,
                            chronicConditions = chronicConditions,
                            currentMedication = currentMedication,
                            prescription = prescription,
                            fee = fee,
                            diagnosis = diagnosis,
                            followUpDate = followUpTimeStamp,
                            reasonForFollowUp = reasonForFollowUp,
                            isChecked = isChecked
                        )
                    },
                    onClickCancel = { navController.navigateUp() },
                    isChecked = isChecked,
                    onCheckedChange = { isChecked = it },
                    followUpTimeStamp = followUpTimeStamp,
                    followUpTimeStampChange = { followUpTimeStamp = it }
                )
            }
        }
    }
}

@Composable
fun Form(
    modifier: Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    selectedOption: Gender,
    onSelectGender: (Gender) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    address: String,
    onAddressChange: (String) -> Unit,
    allergies: String,
    onAllergiesChange: (String) -> Unit,
    chronicConditions: String,
    onChronicConditionsChange: (String) -> Unit,
    currentMedication: String,
    onCurrentMedicationChange: (String) -> Unit,
    prescription: String,
    onPrescriptionChange: (String) -> Unit,
    fee: String,
    onFeeChange: (String) -> Unit,
    diagnosis: String,
    onDiagnosisChange: (String) -> Unit,
    followUpDate: String,
    onFollowUpDateChange: (String) -> Unit,
    reasonForFollowUp: String,
    onReasonForFollowUpChange: (String) -> Unit,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    followUpTimeStamp: Long,
    followUpTimeStampChange: (Long) -> Unit,
    onClickSave: () -> Unit,
    onClickCancel: () -> Unit
) {

    LazyColumn(Modifier.padding(bottom = 24.dp)) {
        items(count = 1) {

            Column(
                modifier = modifier,
                horizontalAlignment = Alignment.Start
            ) {

                Title(
                    "ADD NEW PATIENT",
                    fontSize = 34.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Title(
                    "Basic Information",
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                InputField(
                    modifier = Modifier,
                    label = "Full Name",
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = "Enter full name"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    modifier = Modifier,
                    label = "Age",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "36 years",
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(24.dp))

                GenderFilterChip(
                    selectedOption = selectedOption,
                    onSelectItem = onSelectGender
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    modifier = Modifier,
                    label = "Phone Number",
                    value = phone,
                    onValueChange = onPhoneChange,
                    placeholder = "Enter phone number",
                    keyboardType = KeyboardType.Number
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    modifier = Modifier,
                    label = "Address",
                    value = address,
                    onValueChange = onAddressChange,
                    placeholder = "Enter address"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Title(
                    "CRITICAL Medical History",
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text("(This is the most important section!)")

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    modifier = Modifier,
                    label = "Known Allergies",
                    value = allergies,
                    onValueChange = onAllergiesChange,
                    placeholder = "e.g.,  Penicillin, None _________"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    modifier = Modifier,
                    label = "Chronic Conditions",
                    value = chronicConditions,
                    onValueChange = onChronicConditionsChange,
                    placeholder = "e.g.,Diabetes, High Blood Pressure"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    modifier = Modifier,
                    label = "Current Medications: What medicines they take every day.",
                    value = currentMedication,
                    onValueChange = onCurrentMedicationChange,
                    placeholder = "e.g.,Aspirin, Metformin"
                )

                Spacer(modifier = Modifier.height(24.dp))

                VisitForm(
                    diagnosis = diagnosis,
                    onDiagnosisChange = onDiagnosisChange,
                    prescription = prescription,
                    onPrescriptionChange = onPrescriptionChange,
                    fee = fee,
                    onFeeChange = onFeeChange
                )

                Spacer(modifier = Modifier.height(24.dp))

                ShowFollowUpForm(
                    followUpTimeStamp = followUpTimeStamp,
                    isChecked = isChecked,
                    onCheckedChange = onCheckedChange,
                    followUpTimeStampChange = followUpTimeStampChange,
                    followUpDate = followUpDate,
                    onFollowUpDateChange = onFollowUpDateChange,
                    reasonForFollowUp = reasonForFollowUp,
                    onReasonForFollowUpChange = onReasonForFollowUpChange
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(modifier = Modifier.align(Alignment.End)) {
                    Button(
                        onClick = onClickCancel,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = onClickSave,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Save Patient")
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                }

            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowFollowUpForm(
    followUpTimeStamp: Long,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    followUpTimeStampChange: (Long) -> Unit,
    followUpDate: String,
    onFollowUpDateChange: (String) -> Unit,
    reasonForFollowUp: String,
    onReasonForFollowUpChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Title(
            "Needs Follow-Up?",
            fontSize = 24.sp
        )

        Checkbox(
            checked = isChecked,
            onCheckedChange = onCheckedChange
        )
    }

    if (isChecked) {
        var showDatePicker by remember { mutableStateOf(false) }

        if (showDatePicker) {
            val datePickerState = rememberDatePickerState()
            DatePickerDialog(
                confirmButton = {
                    Button(
                        content = { Text("Confirm") },
                        onClick = {
                            datePickerState.selectedDateMillis?.let { timeStamp ->
                                val followUpDate = LocalTime.getHumanDate(timeStamp)
                                onFollowUpDateChange(followUpDate)
                                followUpTimeStampChange(timeStamp)
                                showDatePicker = false
                            }
                        }
                    )
                },
                dismissButton = {
                    Button(
                        content = { Text("Cancle") },
                        onClick = {
                            showDatePicker = false
                        }
                    )
                },
                onDismissRequest = {
                    showDatePicker = false
                },
                content = {
                    DatePicker(state = datePickerState)
                }
            )
        }

        Box {
            InputField(
                modifier = Modifier.clickable { showDatePicker = true },
                readOnly = true,
                label = "Follow-Up Date",
                value = followUpDate,
                onValueChange = {},
                placeholder = "Pick a Date",
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Filled.DateRange,
                        contentDescription = "Date Icon"
                    )
                }
            )

            Spacer(
                modifier = Modifier.matchParentSize()
                    .background(Color.Transparent)
                    .clickable { showDatePicker = true })
        }

        Spacer(modifier = Modifier.height(24.dp))

        InputField(
            modifier = Modifier,
            label = "Reason for Follow-Up",
            value = reasonForFollowUp,
            onValueChange = onReasonForFollowUpChange,
            placeholder = "General check-up in 1 year"
        )
    }
}

@Composable
fun VisitForm(
    diagnosis: String,
    onDiagnosisChange: (String) -> Unit,
    prescription: String,
    onPrescriptionChange: (String) -> Unit,
    fee: String,
    onFeeChange: (String) -> Unit
) {

    Title(
        "Details for Today's Visit",
        fontSize = 24.sp
    )

    Spacer(modifier = Modifier.height(16.dp))

    LargeInputField(
        label = "Diagnosis / Reason for Visit",
        value = diagnosis,
        onValueChange = onDiagnosisChange
    )

    Spacer(modifier = Modifier.height(24.dp))

    LargeInputField(
        label = "Prescription",
        value = prescription,
        onValueChange = onPrescriptionChange
    )

    Spacer(modifier = Modifier.height(24.dp))

    InputField(
        modifier = Modifier,
        label = "Consultation Fee (MMK)",
        value = fee,
        onValueChange = onFeeChange,
        placeholder = "Enter  consultation fee",
        keyboardType = KeyboardType.Number
    )
}


@Composable
fun GenderFilterChip(
    selectedOption: Gender,
    onSelectItem: (Gender) -> Unit
) {
    val genderOptionList = listOf(Gender.MALE, Gender.FEMALE, Gender.OTHER)

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        genderOptionList.forEach { option ->
            FilterChip(
                selected = option == selectedOption,
                label = {
                    Text(
                        option.name,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                },
                onClick = { onSelectItem(option) }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

