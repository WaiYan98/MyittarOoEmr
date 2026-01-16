import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.waiyan.myittar_oo_emr.screen.component.InputField

// Define this enum if you haven't already. You can place this in a common file or with this composable.
enum class AgeUnit {
    MONTHS,
    YEARS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeInput(
    modifier: Modifier = Modifier,
    initialTotalMonths: Int = 0, // Age from database, in total months
    onTotalMonthsChange: (Int) -> Unit // Callback to update the ViewModel/database
) {
    // Internal state for the UI elements
    var displayAge by remember { mutableStateOf("") } // The text shown in the input field
    var displayUnit by remember { mutableStateOf(AgeUnit.YEARS) } // The currently selected unit

    // --- Logic for initializing UI from initialTotalMonths ---
    // This LaunchedEffect will run when the composable first appears or when initialTotalMonths changes.
    LaunchedEffect(initialTotalMonths) {
        if (initialTotalMonths == 0) {
            displayAge = ""
            displayUnit = AgeUnit.YEARS // Default to Years
        } else {
            if (initialTotalMonths <= 12) {
                // If 12 months or less, display in months
                displayAge = initialTotalMonths.toString()
                displayUnit = AgeUnit.MONTHS
            } else {
                // If more than 12 months, display in years
                displayAge = (initialTotalMonths / 12).toString()
                displayUnit = AgeUnit.YEARS
            }
        }
    }

    // --- Logic for propagating UI changes back to totalMonths ---
    // This effect runs whenever the user changes displayAge or displayUnit,
    // and calls the callback to update the ViewModel's totalMonths state.
    LaunchedEffect(displayAge, displayUnit) {
        val ageNum = displayAge.toIntOrNull() ?: 0
        val totalMonthsCalculated = if (displayUnit == AgeUnit.YEARS) {
            ageNum * 12
        } else {
            ageNum
        }
        onTotalMonthsChange(totalMonthsCalculated)
    }

    Column(modifier = modifier) {
        Text("Age", style = MaterialTheme.typography.labelLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            InputField(
                modifier = Modifier.weight(1f),
                label = "", // Pass an empty label to satisfy the requirement
                value = displayAge,
                onValueChange = { newAgeText ->
                    // Only allow digits
                    if (newAgeText.all { it.isDigit() }) {
                        val newAgeNum = newAgeText.toIntOrNull() ?: 0

                        if (displayUnit == AgeUnit.MONTHS) {
                            // If unit is Months, restrict input to 1-12
                            if (newAgeNum in 1..12 || newAgeText.isEmpty()) { // Allow empty string for clearing
                                displayAge = newAgeText
                            }
                            // If user tries to type >12, do nothing, preventing the input
                        } else { // displayUnit == AgeUnit.YEARS
                            // For years, no specific numeric restriction on input value
                            displayAge = newAgeText
                        }
                    } else if (newAgeText.isEmpty()) {
                        // Allow clearing the input field regardless of unit
                        displayAge = newAgeText
                    }
                },
                keyboardType = KeyboardType.Number,
                placeholder = "Enter age"
            )

            Spacer(modifier = Modifier.width(16.dp))

            Row {
                FilterChip(
                    selected = displayUnit == AgeUnit.MONTHS,
                    onClick = {
                        displayUnit = AgeUnit.MONTHS
                        // If current displayAge is invalid for months, clear it
                        val currentAgeNum = displayAge.toIntOrNull() ?: 0
                        if (currentAgeNum > 12) {
                            displayAge = "" // Clear invalid age when switching to months
                        }
                    },
                    label = { Text("Months") }
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    selected = displayUnit == AgeUnit.YEARS,
                    onClick = {
                        displayUnit = AgeUnit.YEARS
                        // No specific clearing needed for years, as any number is valid
                    },
                    label = { Text("Years") }
                )
            }
        }
    }
}
