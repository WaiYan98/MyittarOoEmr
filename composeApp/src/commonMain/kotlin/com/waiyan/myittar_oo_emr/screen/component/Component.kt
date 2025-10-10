package com.waiyan.myittar_oo_emr.screen.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Report
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.unit.sp

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

@Composable
fun DisplayInfoCard(
    label: String,
    value: String

) {
    HorizontalDivider(
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.primary
    )
    Box(
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Start
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(0.25f),
                text = label,
                color = MaterialTheme.colorScheme.primary
            )
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopStart
            ) {
                Text(
                    value
                )
            }
        }

    }
}

@Composable
fun TableHeader(
    title1: String,
    title2: String,
    title3: String,
    title4: String
) {


    Row(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp
                )
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Title(text = title1, fontSize = 16.sp)
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Title(text = title2, fontSize = 16.sp)
        }


        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Title(text = title3, fontSize = 16.sp)
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Title(text = title4, fontSize = 16.sp)
        }
    }
}

@Composable
fun TableBody(
    data1: String,
    data2: String,
    data3: String,
    data4: String
) {

    Row(
        modifier = Modifier.fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("10/05/2025")
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text("Common cold, slight fever.")
        }


        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = "Paracetamol, rest.")
        }

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = " 5,000 MMK")
        }
    }

}

@Composable
fun TitleCard(
    title1: String,
    title2: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Card(
            modifier = Modifier.wrapContentWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier.wrapContentWidth()
                    .padding(16.dp),
            ) {
                Title(text = title1, fontSize = 24.sp)
            }
        }

        Card(
            modifier = Modifier.wrapContentWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Box(
                modifier = Modifier.wrapContentWidth()
                    .padding(16.dp),
            ) {
                Title(text = title2, fontSize = 24.sp)
            }
        }
    }
}

@Composable
fun ReportCard(
    title1: String,
    value1: String,
    title2: String,
    value2: String
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Title(title1, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Title(value1, fontSize = 24.sp)
            }
        }

        Spacer(Modifier.width(16.dp))

        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(32.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start
            ) {
                Title(title2, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Title(value2, fontSize = 24.sp)
            }
        }
    }
}








