package com.waiyan.myittar_oo_emr.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
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

@Composable
fun Title(
    text: String,
    fontSize: TextUnit
) {
    Text(
        text,
        style = TextStyle(
            fontWeight = FontWeight.ExtraBold,
            fontSize = fontSize
        )
    )
}

@Composable
fun InputField(
    label: String,
    value: String,
    placeholder: String,
    onValueChange: (String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(0.5f),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(label)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            value = value,
            onValueChange = onValueChange,
            placeholder = {
                Text(placeholder)
            },
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.background,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.background
            )
        )
    }

}

@Composable
fun LargeInputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(0.5f),
        horizontalAlignment = Alignment.Start
    ) {
        Text(label)
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            modifier = Modifier.fillMaxWidth()
                .height(144.dp),
            shape = RoundedCornerShape(8.dp),
            value = value,
            onValueChange = onValueChange
        )
    }

}





