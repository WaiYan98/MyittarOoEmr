package com.waiyan.myittar_oo_emr.screen.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyittarOoEmrAppBar(modifier: Modifier) {

    TopAppBar(
        title = {
            Row {
                Icon(
                    Icons.Filled.Star,
                    "logo"
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text("Myittar Oo Emr")
            }
        },
        actions = {
            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.Home,
                    "home"
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.History,
                    "home"
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            IconButton(onClick = {}) {
                Icon(
                    Icons.Filled.Report,
                    "home"
                )
            }
        })

}


