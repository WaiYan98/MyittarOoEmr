import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
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
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Restore
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SettingsBackupRestore
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RangeSlider
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
import com.waiyan.myittar_oo_emr.screen.component.AgeUnit
import com.waiyan.myittar_oo_emr.screen.component.MyittarOoEmrAppBar
import com.waiyan.myittar_oo_emr.screen.component.PatientHistoryScreen
import com.waiyan.myittar_oo_emr.screen.component.ReportScreen
import com.waiyan.myittar_oo_emr.screen.component.ShowEmptyMessage
import com.waiyan.myittar_oo_emr.screen.component.ShowLoading
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.Gender
import com.waiyan.myittar_oo_emr.screen.component.patient_screen.PatientViewModel
import com.waiyan.myittar_oo_emr.screen.component.readableAge
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme
import com.waiyan.myittar_oo_emr.util.FilePicker
import com.waiyan.myittar_oo_emr.util.LocalTime
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun DeleteConfirmationDialog(
    patientCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Patients") },
        text = { Text("Are you sure you want to permanently delete these $patientCount patients?") },
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
    val genderQuery by patientViewModel.genderQuery.collectAsStateWithLifecycle()
    val ageRangeQuery by patientViewModel.ageRangeQuery.collectAsStateWithLifecycle()
    val dateRangeQuery by patientViewModel.dateRangeQuery.collectAsStateWithLifecycle()

    var selectedPageIndex by remember { mutableStateOf(0) }
    val snackBarHostState = remember { SnackbarHostState() }
    val lifeCycleOwner = LocalLifecycleOwner.current

    var showFilePicker by remember { mutableStateOf(false) }
    var isSelectionMode by remember { mutableStateOf(false) }
    val selectedPatientIds = remember { mutableStateListOf<Long>() }
    var showDeleteConfirmationDialog by remember { mutableStateOf(false) }
    var showFilterChipBar by remember { mutableStateOf(false) }

    if (showDeleteConfirmationDialog) {
        DeleteConfirmationDialog(
            patientCount = selectedPatientIds.size,
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


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 16.dp, end = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SearchBar(
                                enabled = !uiState.isBackingUp,
                                modifier = Modifier.weight(1f), // Make SearchBar take up most space
                                value = searchQuery,
                                onValueChange = patientViewModel::onSearchQueryChanged
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            IconButton(
                                onClick = { showFilterChipBar = !showFilterChipBar },
                                enabled = !uiState.isBackingUp
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FilterList,
                                    contentDescription = "Filter"
                                )
                            }
                        }

                        if (showFilterChipBar) {
                            Spacer(modifier = Modifier.height(8.dp))

                            FilterChipBar(
                                modifier = Modifier.fillMaxWidth(),
                                selectedGender = genderQuery,
                                onGenderSelected = patientViewModel::onGenderQueryChanged,
                                minAge = ageRangeQuery.first,
                                maxAge = ageRangeQuery.second,
                                onAgeRangeSelected = patientViewModel::onAgeRangeQueryChanged,
                                fromDate = dateRangeQuery.first,
                                toDate = dateRangeQuery.second,
                                onDateRangeSelected = patientViewModel::onDateRangeQueryChanged,
                                onClearFilters = {
                                    patientViewModel.onGenderQueryChanged(null)
                                    patientViewModel.onAgeRangeQueryChanged("", "")
                                    patientViewModel.onDateRangeQueryChanged(null, null)
                                }
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }

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
                    key = { patientWithVisit -> patientWithVisit.patient.id }
                ) { patientWithVisit ->
                    val isSelected = selectedPatientIds.contains(patientWithVisit.patient.id)
                    PatientCard(
                        enabled = !uiState.isBackingUp,
                        id = patientWithVisit.patient.id.toString(),
                        name = patientWithVisit.patient.name,
                        age = patientWithVisit.patient.age.readableAge(),
                        gender = patientWithVisit.patient.gender,
                        address = patientWithVisit.patient.address,
                        isSelected = isSelected,
                        onLongClick = {
                            isSelectionMode = true
                            selectedPatientIds.add(patientWithVisit.patient.id)
                        },
                        onClick = {
                            if (isSelectionMode) {
                                if (isSelected) {
                                    selectedPatientIds.remove(patientWithVisit.patient.id)
                                    if (selectedPatientIds.isEmpty()) {
                                        isSelectionMode = false
                                    }
                                } else {
                                    selectedPatientIds.add(patientWithVisit.patient.id)
                                }
                            } else {
                                navController.navigate(PatientHistoryScreen(patientWithVisit.patient.id))
                            }
                        }
                    )
                    Spacer(Modifier.height(16.dp))
                }
            }
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FilterChipBar(
    modifier: Modifier = Modifier,
    selectedGender: String?,
    onGenderSelected: (String?) -> Unit,
    minAge: String,
    maxAge: String,
    onAgeRangeSelected: (String, String) -> Unit,
    fromDate: Long?,
    toDate: Long?,
    onDateRangeSelected: (Long?, Long?) -> Unit,
    onClearFilters: () -> Unit
) {
    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        GenderFilterButton(
            selectedGender = selectedGender,
            onGenderSelected = onGenderSelected
        )

        AgeFilterButton(
            minAge = minAge,
            maxAge = maxAge,
            onAgeRangeSelected = onAgeRangeSelected
        )

        DateFilterButton(
            fromDate = fromDate,
            toDate = toDate,
            onDateRangeSelected = onDateRangeSelected
        )

        // Clear Button
        IconButton(onClick = onClearFilters) {
            Icon(Icons.Filled.Clear, contentDescription = "Clear Filters")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GenderFilterButton(
    selectedGender: String?,
    onGenderSelected: (String?) -> Unit
) {
    var genderMenuExpanded by remember { mutableStateOf(false) }
    val isGenderFilterActive = selectedGender != null

    Box {
        FilterChip(
            selected = isGenderFilterActive,
            onClick = { genderMenuExpanded = true },
            label = { Text(selectedGender?.let { "Gender: $it" } ?: "Gender") },
            leadingIcon = if (isGenderFilterActive) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            }
        )
        DropdownMenu(
            expanded = genderMenuExpanded,
            onDismissRequest = { genderMenuExpanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Male") },
                onClick = {
                    onGenderSelected(Gender.MALE.name)
                    genderMenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Female") },
                onClick = {
                    onGenderSelected(Gender.FEMALE.name)
                    genderMenuExpanded = false
                }
            )
            DropdownMenuItem(
                text = { Text("Other") },
                onClick = {
                    onGenderSelected(Gender.OTHER.name)
                    genderMenuExpanded = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AgeFilterButton(
    minAge: String, // in months
    maxAge: String, // in months
    onAgeRangeSelected: (String, String) -> Unit
) {
    var ageMenuExpanded by remember { mutableStateOf(false) }
    val isAgeFilterActive = minAge.isNotEmpty() || maxAge.isNotEmpty()

    var selectedUnit by remember { mutableStateOf(AgeUnit.YEARS) }

    // This state will now hold the range for the *current* unit.
    var sliderPosition by remember(selectedUnit) {
        mutableStateOf(if (selectedUnit == AgeUnit.MONTHS) 1f..11f else 1f..100f)
    }

    // This is for displaying the current slider range in a readable format.
    fun formatSliderLabel(value: Float, unit: AgeUnit): String {
        return when (unit) {
            AgeUnit.MONTHS -> "${value.toInt()} m"
            AgeUnit.YEARS -> "${value.toInt()} y"
        }
    }

    Box {
        FilterChip(
            selected = isAgeFilterActive,
            onClick = { ageMenuExpanded = true },
            label = { Text(getAgeFilterLabel(minAge, maxAge, selectedUnit)) },
            leadingIcon = if (isAgeFilterActive) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            }
        )
        DropdownMenu(
            expanded = ageMenuExpanded,
            onDismissRequest = { ageMenuExpanded = false }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(300.dp)
            ) {
                Text(
                    "Age Range: ${formatSliderLabel(sliderPosition.start, selectedUnit)} - ${
                        formatSliderLabel(
                            sliderPosition.endInclusive,
                            selectedUnit
                        )
                    }",
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Chips to select the unit
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedUnit == AgeUnit.MONTHS,
                        onClick = { selectedUnit = AgeUnit.MONTHS },
                        label = { Text("Months") }
                    )
                    FilterChip(
                        selected = selectedUnit == AgeUnit.YEARS,
                        onClick = { selectedUnit = AgeUnit.YEARS },
                        label = { Text("Years") }
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                // The slider's range and steps depend on the selected unit.
                val valueRange = if (selectedUnit == AgeUnit.MONTHS) 1f..11f else 1f..100f

                RangeSlider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it },
                    valueRange = valueRange,
                    steps = 0
                )

                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        val min = sliderPosition.start.toInt()
                        val max = sliderPosition.endInclusive.toInt()

                        // Convert to months before calling the callback
                        if (selectedUnit == AgeUnit.YEARS) {
                            onAgeRangeSelected((min * 12).toString(), (max * 12).toString())
                        } else {
                            onAgeRangeSelected(min.toString(), max.toString())
                        }
                        ageMenuExpanded = false
                    }
                ) {
                    Text("Apply")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DateFilterButton(
    fromDate: Long?,
    toDate: Long?,
    onDateRangeSelected: (Long?, Long?) -> Unit
) {
    var dateMenuExpanded by remember { mutableStateOf(false) }
    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }

    var currentFromDate by remember(fromDate) { mutableStateOf(fromDate) }
    var currentToDate by remember(toDate) { mutableStateOf(toDate) }

    val isDateFilterActive = fromDate != null || toDate != null

    Box {
        FilterChip(
            selected = isDateFilterActive,
            onClick = { dateMenuExpanded = true },
            label = { Text(getDateFilterLabel(fromDate, toDate)) },
            leadingIcon = if (isDateFilterActive) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        contentDescription = "Done icon",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            } else {
                null
            }
        )
        DropdownMenu(
            expanded = dateMenuExpanded,
            onDismissRequest = { dateMenuExpanded = false }
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .width(260.dp)
            ) {
                Text("Date Range", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { showStartDatePicker = true }
                    ) {
                        Text(currentFromDate?.let { LocalTime.getHumanDate(it) } ?: "Start Date")
                    }
                    Button(
                        onClick = { showEndDatePicker = true }
                    ) {
                        Text(currentToDate?.let { LocalTime.getHumanDate(it) } ?: "End Date")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))
                TextButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        onDateRangeSelected(currentFromDate, currentToDate)
                        dateMenuExpanded = false
                    }
                ) {
                    Text("Apply")
                }
            }
        }
    }

    if (showStartDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showStartDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        currentFromDate = datePickerState.selectedDateMillis
                        showStartDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showStartDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    if (showEndDatePicker) {
        val datePickerState = rememberDatePickerState()
        DatePickerDialog(
            onDismissRequest = { showEndDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        currentToDate = datePickerState.selectedDateMillis
                        showEndDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEndDatePicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}

private fun getAgeFilterLabel(minAge: String, maxAge: String, selectedUnit: AgeUnit): String {
    return when {
        minAge.isNotEmpty() && maxAge.isNotEmpty() -> if (selectedUnit == AgeUnit.MONTHS) "Age: $minAge M-$maxAge M" else "Age: ${minAge.toInt() / 12} Y-${maxAge.toInt() / 12} Y"
        minAge.isNotEmpty() -> if (selectedUnit == AgeUnit.MONTHS) "Age: $minAge M+" else "Age: ${minAge.toInt() / 12} Y+"
        maxAge.isNotEmpty() -> if (selectedUnit == AgeUnit.MONTHS) "Age: -$maxAge M" else "Age: -${maxAge.toInt() / 12} Y"
        else -> "Age"
    }
}

private fun getDateFilterLabel(fromDate: Long?, toDate: Long?): String {
    return when {
        fromDate != null && toDate != null -> "Date: ${LocalTime.getHumanDate(fromDate)} - ${LocalTime.getHumanDate(toDate)}"
        fromDate != null -> "Date: From ${LocalTime.getHumanDate(fromDate)}"
        toDate != null -> "Date: To ${LocalTime.getHumanDate(toDate)}"
        else -> "Date"
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
