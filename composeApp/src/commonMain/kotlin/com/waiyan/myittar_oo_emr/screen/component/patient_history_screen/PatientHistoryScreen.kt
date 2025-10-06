package com.waiyan.myittar_oo_emr.screen.component.patient_history_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waiyan.myittar_oo_emr.screen.component.DisplayInfoCard
import com.waiyan.myittar_oo_emr.screen.component.MyittarOoEmrAppBar
import com.waiyan.myittar_oo_emr.screen.component.TableBody
import com.waiyan.myittar_oo_emr.screen.component.TableHeader
import com.waiyan.myittar_oo_emr.screen.component.Title
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme

@Composable
fun PatientHistoryScreen() {

    MyAppTheme {
        Scaffold(
            topBar = { MyittarOoEmrAppBar(Modifier) }
        ) { values ->
            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        values,
                    ),
                contentAlignment = Alignment.TopCenter
            ) {
                PatientHistoryDisplay()
            }
        }
    }
}


@Composable
fun PatientHistoryDisplay() {

    LazyColumn(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalAlignment = Alignment.Start
    ) {
        items(1) {

            Title(
                "Aung Aung",
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Age", "42")

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Gender", "Male")

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Id", "007")

            Spacer(modifier = Modifier.height(32.dp))

            Title(
                "!! CRITICAL MEDICAL INFO !!",
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Allergies", "Penicillin")

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Chronic Conditions", "High Blood Pressure")

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Current Medication", "Metformin,Aspirin")

            Spacer(modifier = Modifier.height(32.dp))

            Title(
                "Contact & Address Information",
                fontSize = 32.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Phone", "09-123-45678")

            Spacer(modifier = Modifier.height(24.dp))

            DisplayInfoCard("Address", "Myawaddy")

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {},
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("+ ADD NEW VISIT")
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

        items(count = 10) {
            TableBody(
                data1 = "",
                data2 = "",
                data3 = "",
                data4 = ""
            )

        }

    }

}

