package com.waiyan.myittar_oo_emr.screen.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
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

// Define this enum if you haven't already. You can place this in a common file or with this composable.
// Still used by PatientScreen.kt
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
    var yearsPart by remember { mutableStateOf("") }
    var monthsPart by remember { mutableStateOf("") }

    // --- Logic for initializing UI from initialTotalMonths ---
    LaunchedEffect(initialTotalMonths) {
        if (initialTotalMonths == 0) {
            yearsPart = ""
            monthsPart = ""
        } else {
            val years = initialTotalMonths / 12
            val months = initialTotalMonths % 12
            yearsPart = if (years > 0) years.toString() else ""
            monthsPart = if (months > 0) months.toString() else ""
        }
    }

    // --- Logic for propagating UI changes back to totalMonths ---
    LaunchedEffect(yearsPart, monthsPart) {
        val y = yearsPart.toIntOrNull() ?: 0
        val m = monthsPart.toIntOrNull() ?: 0
        onTotalMonthsChange((y * 12) + m)
    }

    Column(modifier = modifier) {
        Text(text = "Age", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputField(
                modifier = Modifier.weight(1f),
                label = "Years",
                value = yearsPart,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        yearsPart = newValue
                    }
                },
                keyboardType = KeyboardType.Number,
                placeholder = "0"
            )

            Spacer(modifier = Modifier.width(16.dp))

            InputField(
                modifier = Modifier.weight(1f),
                label = "Months",
                value = monthsPart,
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() }) {
                        val num = newValue.toIntOrNull() ?: 0
                        if (num in 0..11 || newValue.isEmpty()) {
                            monthsPart = newValue
                        }
                    }
                },
                keyboardType = KeyboardType.Number,
                placeholder = "0"
            )
        }
    }
}
