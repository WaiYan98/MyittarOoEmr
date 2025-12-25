package com.waiyan.myittar_oo_emr.util

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect

@Composable
actual fun FilePicker(
    show: Boolean,
    onFileSelected: (String?) -> Unit
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
        onResult = { uri ->
            onFileSelected(uri?.toString())
        }
    )

    LaunchedEffect(show) {
        if (show) {
            launcher.launch(arrayOf("application/vnd.sqlite3", "application/x-sqlite3", "application/octet-stream"))
        }
    }
}
