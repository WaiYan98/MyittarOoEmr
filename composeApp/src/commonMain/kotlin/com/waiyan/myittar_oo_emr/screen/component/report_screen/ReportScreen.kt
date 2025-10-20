package com.waiyan.myittar_oo_emr.screen.component.report_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.waiyan.myittar_oo_emr.screen.component.MyittarOoEmrAppBar
import com.waiyan.myittar_oo_emr.screen.component.ReportCard
import com.waiyan.myittar_oo_emr.screen.component.TableBody
import com.waiyan.myittar_oo_emr.screen.component.TableHeader
import com.waiyan.myittar_oo_emr.screen.component.Title
import com.waiyan.myittar_oo_emr.screen.component.TitleCard
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme


@Composable
fun ReportScreen(navController: NavController) {

    var selectedPageIndex by remember { mutableStateOf(1) }

    MyAppTheme {
        Scaffold(
            topBar = {
                MyittarOoEmrAppBar(
                    modifier = Modifier,
                    onClickReport = { selectedPageIndex = 1 },
                    onClickHome = {
                        navController.navigateUp()
                        selectedPageIndex = 0
                    },
                    selectedPageIndex = selectedPageIndex
                )
            }
        ) { values ->

            Box(
                modifier = Modifier.fillMaxSize()
                    .padding(values),
                contentAlignment = Alignment.TopCenter
            ) {
                ReportDisplay()
            }
        }
    }
}

@Composable
fun ReportDisplay() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth(0.75f),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top
    ) {
        item {

            Title("REPORTS & FOLLOW-UPS", fontSize = 32.sp)

            Spacer(modifier = Modifier.height(16.dp))

            TitleCard(
                title1 = "Today's Summary",
                title2 = "Income Overview"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReportCard(
                title1 = "Patients Seen",
                value1 = "12",
                title2 = "Today's Income",
                value2 = "650,000 MMK"
            )

            Spacer(modifier = Modifier.height(16.dp))

            ReportCard(
                title1 = "This Month",
                value1 = "870,000 MMK",
                title2 = "This Year",
                value2 = "9,450,000 MMK"
            )

            Spacer(Modifier.height(32.dp))

            Title("Upcoming Follow-Ups", fontSize = 24.sp)

            Spacer(Modifier.height(16.dp))

        }

        item {
            TableHeader(
                title1 = "Date",
                title2 = "Patient Name",
                title3 = "Reason for Visit",
                title4 = "Time Until"
            )
        }

        items(count = 10) {
            TableBody(
                data1 = "2025-09-23",
                data2 = "Olivia Bennett",
                data3 = "Routine Checkup",
                data4 = "2 days"
            )
        }
    }
}
