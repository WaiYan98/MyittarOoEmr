package com.waiyan.myittar_oo_emr.screen.component.lock_screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.waiyan.myittar_oo_emr.screen.component.lock_screen.PinSettingsViewModel
import androidx.navigation.NavController // New import
import androidx.compose.material.icons.filled.Close // New import
import androidx.compose.material.icons.filled.ArrowBack // Alternative for close/back

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinLockScreen(
    navController: NavController, // New parameter
    onPinCorrect: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PinSettingsViewModel // No default value, it must be passed
) {
//    Log.d("ViewModelInstance", "PinLockScreen ViewModel: ${viewModel.hashCode()}") // Log hashcode
    val isPinSet by viewModel.isPinSet.collectAsStateWithLifecycle()
    val isUnlocked by viewModel.isUnlocked.collectAsStateWithLifecycle()

    var pinInput by remember { mutableStateOf("") }
    var confirmPinInput by remember { mutableStateOf("") } // For setting PIN
    var showError by remember { mutableStateOf(false) }
    var showMismatchError by remember { mutableStateOf(false) } // For setting PIN

    LaunchedEffect(isUnlocked) {
        if (isUnlocked) {
            pinInput = "" // Reset pinInput after successful unlock
            onPinCorrect() // Invoke the callback
            viewModel.lockScreen() // Lock it again after the callback, for subsequent entries
        }
    }

    Box(modifier = modifier.fillMaxSize()) { // Wrap content in a Box
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Lock Icon",
                modifier = Modifier.size(160.dp)
            )
            Spacer(Modifier.height(48.dp))

            if (!isPinSet) {
                Text(
                    text = "Set a new PIN",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(32.dp))
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = {
                        if (it.length <= 4) {
                            pinInput = it
                            showError = false
                            showMismatchError = false
                        }
                    },
                    label = { Text("New PIN", fontSize = 20.sp) },
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = showError || showMismatchError,
                    singleLine = true,
                    modifier = Modifier.width(360.dp)
                )
                Spacer(Modifier.height(16.dp))
                OutlinedTextField(
                    value = confirmPinInput,
                    onValueChange = {
                        if (it.length <= 4) {
                            confirmPinInput = it
                            showError = false
                            showMismatchError = false
                        }
                    },
                    label = { Text("Confirm PIN", fontSize = 20.sp) },
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = showError || showMismatchError,
                    singleLine = true,
                    modifier = Modifier.width(360.dp)
                )
                if (showMismatchError) {
                    Text(
                        text = "PINs do not match",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    modifier = Modifier
                        .height(64.dp)
                        .width(360.dp),
                    onClick = {
                        if (pinInput.length == 4 && pinInput == confirmPinInput) {
                            viewModel.setPin(pinInput)
                            viewModel.verifyPin(pinInput) // Immediately unlock after setting
                        } else if (pinInput != confirmPinInput) {
                            showMismatchError = true
                        } else {
                            showError = true // For length < 4
                        }
                    },
                    enabled = pinInput.length == 4 && confirmPinInput.length == 4,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Set PIN", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            } else {
                Text(
                    text = "Enter PIN to access Report",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.height(32.dp))
                OutlinedTextField(
                    value = pinInput,
                    onValueChange = {
                        if (it.length <= 4) {
                            pinInput = it
                            showError = false
                        }
                    },
                    label = { Text("PIN", fontSize = 20.sp) },
                    textStyle = androidx.compose.ui.text.TextStyle(fontSize = 24.sp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = PasswordVisualTransformation(),
                    isError = showError,
                    singleLine = true,
                    modifier = Modifier.width(360.dp)
                )
                if (showError) {
                    Text(
                        text = "Incorrect PIN",
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 18.sp,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
                Spacer(Modifier.height(32.dp))
                Button(
                    modifier = Modifier
                        .height(64.dp)
                        .width(360.dp),
                    onClick = {
                        if (!viewModel.verifyPin(pinInput)) {
                            showError = true
                        }
                    },
                    enabled = pinInput.length == 4,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Unlock", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(24.dp)
                .size(64.dp)
        ) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}
