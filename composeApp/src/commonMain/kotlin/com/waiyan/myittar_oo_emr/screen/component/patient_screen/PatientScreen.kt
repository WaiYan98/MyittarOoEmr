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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.screen.component.MyittarOoEmrAppBar
import com.waiyan.myittar_oo_emr.screen.component.PatientHistoryScreen
import com.waiyan.myittar_oo_emr.screen.component.ReportScreen
import com.waiyan.myittar_oo_emr.screen.component.ShowLoading
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun PatientScreen(
    navController: NavController,
    patientViewModel: PatientViewModel = koinViewModel<PatientViewModel>(),
    onClickAdd: () -> Unit
) {

    val uiState by patientViewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by patientViewModel.searchQuery.collectAsStateWithLifecycle()
    var selectedPageIndex by remember { mutableStateOf(0) }
    val snackBarHostState = remember { SnackbarHostState() }
    val lifeCycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(key1 = uiState.onError) {
        uiState.onError?.let {
            snackBarHostState.showSnackbar(it)
            patientViewModel.onClearError()
        }
    }

    DisposableEffect(lifeCycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                patientViewModel.getAllPatient()
            }
        }
        lifeCycleOwner.lifecycle.addObserver(observer)

        onDispose { lifeCycleOwner.lifecycle.removeObserver(observer) }
    }

    MyAppTheme {
        Scaffold(
            topBar = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    MyittarOoEmrAppBar(
                        Modifier.fillMaxWidth(),
                        onClickHome = { selectedPageIndex = 0 },
                        onClickReport = {
                            navController.navigate(ReportScreen)
                            selectedPageIndex = 1
                        },
                        selectedPageIndex = selectedPageIndex
                    )

                    SearchBar(
                        modifier = Modifier.fillMaxWidth()
                            .padding(start = 16.dp, end = 16.dp),
                        value = searchQuery,
                        onValueChange = patientViewModel::onSearchQueryChanged
                    )

                }
            },
            snackbarHost = {
                SnackbarHost(snackBarHostState)
            },
            floatingActionButton = {
                FloatingActionButton(onClick = onClickAdd) {
                    Icon(
                        Icons.Filled.Add,
                        "insert_data"
                    )
                }
            }) { values ->


            if (uiState.isLoading) {
                ShowLoading()
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
                    .padding(
                        start = 16.dp,
                        end = 16.dp,
                        top = values.calculateTopPadding(),
                        bottom = values.calculateBottomPadding()
                    )
            ) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                items(
                    uiState.success,
                    key = { patient -> patient.id }
                ) { patient ->
                    PatientCard(
                        id = patient.id.toString(),
                        name = patient.name,
                        age = patient.age.toString(),
                        gender = patient.gender,
                        address = patient.address
                    ) { patientId ->
                        navController.navigate(PatientHistoryScreen(patientId))
                    }
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

    }
}

@Composable
fun SearchBar(
    modifier: Modifier,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        modifier = modifier,
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
    address: String,
    onclickPatient: (Long) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onclickPatient(id.toLong()) }
    ) {
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
                    .size(48.dp)
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
