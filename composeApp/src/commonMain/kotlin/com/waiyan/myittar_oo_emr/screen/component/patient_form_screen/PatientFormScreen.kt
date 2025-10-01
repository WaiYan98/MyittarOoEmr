package com.waiyan.myittar_oo_emr.screen.component.patient_form_screen

import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waiyan.myittar_oo_emr.screen.component.InputField
import com.waiyan.myittar_oo_emr.screen.component.LargeInputField
import com.waiyan.myittar_oo_emr.screen.component.Title
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme

@Composable
fun PatientFormScreen() {

    var selectedOption by remember { mutableStateOf("Male") }

    MyAppTheme {
        Scaffold(
            topBar = { AppBar() }
        ) { values ->
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                Form(
                    Modifier
                        .fillMaxWidth(0.75f)
                        .padding(values),
                    "",
                    {},
                    "",
                    {},
                    selectedOption,
                    onSelectItem = { selectedOption = it })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar() {
    TopAppBar(
        title = {
            Row {

                Image(
                    Icons.Filled.Star,
                    contentDescription = "logo"
                )
                Text("MyittarOoEmr")
            }
        }
    )
}

@Composable
fun Form(
    modifier: Modifier,
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    selectedOption: String,
    onSelectItem: (String) -> Unit
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
                    label = "Full Name",
                    value = name,
                    onValueChange = onNameChange,
                    placeholder = "Enter full name"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Age",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "36 years"
                )

                Spacer(modifier = Modifier.height(24.dp))

                GenderFilterChip(
                    selectedOption = selectedOption,
                    onSelectItem = onSelectItem
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Phone Number",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "Enter phone number"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Address",
                    value = age,
                    onValueChange = onAgeChange,
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
                    label = "Known Allergies",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "e.g.,  Penicillin, None _________"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Chronic Conditions",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "e.g.,Diabetes, High Blood Pressure"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Current Medications: What medicines they take every day.",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "e.g.,Aspirin, Metformin"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Title(
                    "Details for Today's Visit",
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                LargeInputField(
                    label = "Diagnosis / Reason for Visit",
                    value = "",
                    onValueChange = {})

                Spacer(modifier = Modifier.height(24.dp))

                LargeInputField(
                    label = "Prescription",
                    value = "",
                    onValueChange = {})

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Consultation Fee (MMK)",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "Enter  consultation fee"
                )

                Spacer(modifier = Modifier.height(24.dp))

                Title(
                    "Needs Follow-Up?",
                    fontSize = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Follow-Up Date",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "Pick a Date"
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField(
                    label = "Reason for Follow-Up",
                    value = age,
                    onValueChange = onAgeChange,
                    placeholder = "General check-up in 1 year"
                )

                Spacer(modifier = Modifier.height(16.dp))


                Row(modifier = Modifier.align(Alignment.End)) {
                    Button(
                        onClick = {},
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancel")
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(onClick = {},
                        shape = RoundedCornerShape(8.dp)) {
                        Text("Save Patient")
                    }

                    Spacer(modifier = Modifier.width(16.dp))
                }

            }
        }

    }

}


@Composable
fun GenderFilterChip(
    selectedOption: String,
    onSelectItem: (String) -> Unit
) {
    val genderOptionList = listOf("Male", "Female", "Other")

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
                        option,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                },
                onClick = { onSelectItem(option) }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}

