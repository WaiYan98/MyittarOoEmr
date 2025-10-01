package com.waiyan.myittar_oo_emr.screen.component.patient_screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.screen.component.MyittarOoEmrAppBar
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PatientScreen(
    patientViewModel: PatientViewModel = koinViewModel<PatientViewModel>(),
    onClickAdd: () -> Unit
) {

    val patients by patientViewModel.patientFlow.collectAsStateWithLifecycle()
    var searchTxt by mutableStateOf("")

    MyAppTheme {
        Scaffold(
            topBar = {
                MyittarOoEmrAppBar(Modifier.fillMaxWidth())
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onClickAdd) {
                    Icon(
                        Icons.Filled.Add,
                        "insert_data"
                    )
                }
            }) { values ->
            Column(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = values.calculateTopPadding(),
                        bottom = values.calculateBottomPadding()
                    )
            ) {

                SearchBar(searchTxt) { onValueChange ->
                    searchTxt = onValueChange
                }

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(patients) { patient ->
                        PatientCard(
                            id = patient.id.toString(),
                            name = patient.name,
                            age = patient.age.toString(),
                            gender = "Male",
                            address = patient.address
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                }
            }

        }
    }
}

@Composable
fun SearchBar(
    value: String,
    onValueChange: (String) -> Unit
) {
    TextField(
        modifier = Modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                Icons.Filled.Search,
                "search_icon"
            )
        },
        placeholder = {
            Text("Search Patients...")
        },
        shape = RoundedCornerShape(16.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.background,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.background,
        )
    )
}

@Composable
fun PatientCard(
    id: String,
    name: String,
    age: String,
    gender: String,
    address: String
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth()
//                .background(White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                Icons.Filled.Person,
                "profile_img",
                contentScale = ContentScale.Crop,
                modifier = Modifier.clip(CircleShape)
                    .size(100.dp)
                    .background(Red)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column() {
                Text(
                    text = name,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )

                Row {
                    ShowLabel("Age", age)
                    Spacer(modifier = Modifier.width(4.dp))
                    ShowLabel("Gender", gender)
                    Spacer(modifier = Modifier.width(4.dp))
                    ShowLabel("ID", id)

                }

                Text(address)
            }

            Box(
                Modifier.fillMaxSize(),
                contentAlignment = Alignment.CenterEnd
            ) {
                IconButton(onClick = {}) {
                    Icon(
                        Icons.Filled.HeartBroken,
                        "arrow_icon"
                    )
                }

            }
        }
    }
}

@Composable
fun ShowLabel(
    labelName: String,
    value: String
) {
    Row {
        Text(
            text = "$labelName: ",
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Gray
        )
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            color = Gray
        )
    }
}
