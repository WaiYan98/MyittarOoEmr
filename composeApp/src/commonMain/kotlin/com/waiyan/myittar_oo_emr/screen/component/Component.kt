package com.waiyan.myittar_oo_emr.screen.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyittarOoEmrAppBar(
    modifier: Modifier,
    onClickHome: () -> Unit,
    onClickReport: () -> Unit,
    selectedPageIndex: Int
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically

            ) {

                IconButton(
                    onClick = {},
                ) {
                    Icon(
                        Icons.Filled.Diamond,
                        "logo"
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))

                Text("Myittar Oo Emr")
            }
        },
        actions = {
            TextButton(
                modifier = Modifier.border(
                    1.dp,
                    color = if (selectedPageIndex == 0) MaterialTheme.colorScheme.primary
                    else Color(0x00000000),
                    shape = RoundedCornerShape(16.dp)
                ),
                onClick = onClickHome,
            ) {
                Title("Home", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(8.dp))

            TextButton(
                modifier = Modifier.border(
                    1.dp,
                    color = if (selectedPageIndex == 1) MaterialTheme.colorScheme.primary
                    else Color(0x00000000),
                    shape = RoundedCornerShape(16.dp)
                ),
                onClick = onClickReport,
            ) {
                Title("Report", fontSize = 24.sp)
            }
        }
    )
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
    modifier: Modifier,
    label: String,
    value: String,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    trailingIcon: @Composable (() -> Unit)? = null,
    readOnly: Boolean = false,
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
            ),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            trailingIcon = trailingIcon,
            readOnly = readOnly
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

@Composable
fun ShowLoading() {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(
                Color(0x00000000)
            ),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            color = Red
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    onClickBack: () -> Unit
) {
    TopAppBar(
        navigationIcon = {
            IconButton(
                onClick = onClickBack,
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack,
                    "back_arrow"
                )
            }
        },
        title = {
            Title(title, fontSize = 24.sp)
        }
    )
}







