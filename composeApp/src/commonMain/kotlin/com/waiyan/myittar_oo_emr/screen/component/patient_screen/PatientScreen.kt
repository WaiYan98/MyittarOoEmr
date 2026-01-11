import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.screen.component.MyittarOoEmrAppBar
import com.waiyan.myittar_oo_emr.screen.component.PatientHistoryScreen
import com.waiyan.myittar_oo_emr.screen.component.ReportScreen
import com.waiyan.myittar_oo_emr.screen.component.ShowEmptyMessage
import com.waiyan.myittar_oo_emr.screen.component.ShowLoading
import com.waiyan.myittar_oo_emr.screen.component.patient_screen.PatientViewModel
import com.waiyan.myittar_oo_emr.util.FilePicker
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Patients") },
        text = { Text("Are you sure you want to permanently delete these patients?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContextualActionBar(
    selectedItemCount: Int,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    TopAppBar(
        title = { Text("$selectedItemCount selected") },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(Icons.Default.Close, contentDescription = "Close")
            }
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(Icons.Default.Delete, contentDescription = "Delete")
            }
        }
    )
}

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

    var showFilePicker by remember { mutableStateOf(false) }
    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedPatientIds = remember { mutableStateListOf<Long>() }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }

    if (showDeleteConfirmationDialog) {
        DeleteConfirmationDialog(
            onConfirm = {
                patientViewModel.deleteSelectedPatients(selectedPatientIds.toList())
                isSelectionMode = false
                selectedPatientIds.clear()
                showDeleteConfirmationDialog = false
            },
            onDismiss = {
                showDeleteConfirmationDialog = false
            }
        )
    }

    FilePicker(
        show = showFilePicker,
        onFileSelected = { uri ->
            showFilePicker = false
            if (uri != null) {
                patientViewModel.restoreDatabase(uri)
            }
        }
    )

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
                if (isSelectionMode) {
                    ContextualActionBar(
                        selectedItemCount = selectedPatientIds.size,
                        onCloseClick = {
                            isSelectionMode = false
                            selectedPatientIds.clear()
                        },
                        onDeleteClick = {
                            showDeleteConfirmationDialog = true
                        }
                    )
                } else {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        MyittarOoEmrAppBar(
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isBackingUp,
                            onClickHome = { selectedPageIndex = 0 },
                            onClickReport = {
                                navController.navigate(ReportScreen)
                                selectedPageIndex = 1
                            },
                            selectedPageIndex = selectedPageIndex
                        )

                        SearchBar(
                            enabled = !uiState.isBackingUp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            value = searchQuery,
                            onValueChange = patientViewModel::onSearchQueryChanged
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(
                                enabled = !uiState.isBackingUp,
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                onClick = {
                                    patientViewModel.backupDatabase()
                                }
                            ) {
                                Icon(
                                    Icons.Filled.SettingsBackupRestore,
                                    "backup_data",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Backup", fontSize = 18.sp)
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            TextButton(
                                enabled = !uiState.isBackingUp,
                                modifier = Modifier
                                    .weight(1f)
                                    .border(
                                        1.dp,
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                onClick = {
                                    showFilePicker = true
                                }
                            ) {
                                Icon(
                                    Icons.Filled.Restore,
                                    "restore_data",
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Restore", fontSize = 18.sp)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            },
            snackbarHost = {
                SnackbarHost(snackBarHostState)
            },
            floatingActionButton = {
                if (!isSelectionMode) {
                    FloatingActionButton(
                        onClick = {
                            if (!uiState.isBackingUp) {
                                onClickAdd()
                            }
                        }
                    ) {
                        Icon(
                            Icons.Filled.Add,
                            "insert_data"
                        )
                    }
                }
            }) { values ->


            if (uiState.isLoading) {
                ShowLoading()
            }

            if (uiState.success.isEmpty()) {
                ShowEmptyMessage()
            }

            LazyColumn(
                userScrollEnabled = !uiState.isBackingUp,
                modifier = Modifier
                    .fillMaxSize()
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
                    val isSelected = selectedPatientIds.contains(patient.id)
                    PatientCard(
                        enabled = !uiState.isBackingUp,
                        id = patient.id.toString(),
                        name = patient.name,
                        age = patient.age.toString(),
                        gender = patient.gender,
                        address = patient.address,
                        isSelected = isSelected,
                        onLongClick = {
                            isSelectionMode = true
                            selectedPatientIds.add(patient.id)
                        },
                        onClick = {
                            if (isSelectionMode) {
                                if (isSelected) {
                                    selectedPatientIds.remove(patient.id)
                                    if (selectedPatientIds.isEmpty()) {
                                        isSelectionMode = false
                                    }
                                } else {
                                    selectedPatientIds.add(patient.id)
                                }
                            } else {
                                navController.navigate(PatientHistoryScreen(patient.id))
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

    }
}

@Composable
fun SearchBar(
    modifier: Modifier,
    enabled: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
) {
    TextField(
        enabled = enabled,
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
    enabled: Boolean,
    id: String,
    name: String,
    age: String,
    gender: String,
    address: String,
    isSelected: Boolean,
    onLongClick: () -> Unit,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                enabled = enabled,
                onClick = onClick,
                onLongClick = onLongClick
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isSelected) {
                Checkbox(
                    checked = true,
                    onCheckedChange = {}
                )
            } else {
                Image(
                    Icons.Filled.Person,
                    "profile_img",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .clip(CircleShape)
                        .size(48.dp)
                        .background(Red)
                )
            }

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
