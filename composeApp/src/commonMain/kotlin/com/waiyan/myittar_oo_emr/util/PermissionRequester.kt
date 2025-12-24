package com.waiyan.myittar_oo_emr.util

import androidx.compose.runtime.Composable

@Composable
expect fun PermissionRequester(
    onPermissionGranted: () -> Unit,
    onPermissionDenied: () -> Unit
)
