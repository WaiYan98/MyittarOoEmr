package com.waiyan.myittar_oo_emr.screen.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Diamond
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waiyan.myittar_oo_emr.screen.component.patient_form_screen.Gender
import com.waiyan.myittar_oo_emr.ui.theme.MyAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyittarOoEmrAppBar(
    modifier: Modifier,
    enabled: Boolean,
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
                enabled = enabled,
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
                enabled = enabled,
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
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        if (label.isNotBlank()) {
            Text(label)
            Spacer(modifier = Modifier.height(8.dp))
        }
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
            modifier = Modifier
                .fillMaxWidth()
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
    value: String,
    isEditing: Boolean,
    isChronic: Boolean = false,
    isAllergies: Boolean = false,
    onEditValueChange: (String) -> Unit
) {
        HorizontalDivider(
            thickness = 1.dp,
            color = MaterialTheme.colorScheme.primary
        )

    Box(
        contentAlignment = Alignment.TopStart
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
                Text(
                    modifier = Modifier.fillMaxWidth(0.25f),
                    text = label,
                    color = MaterialTheme.colorScheme.primary
                )

            TextField(
                value = value,
                readOnly = !isEditing,
                onValueChange = onEditValueChange,
                enabled = isEditing,
                colors = TextFieldDefaults.colors(
                    disabledContainerColor = if (isEditing) TextFieldDefaults.colors().disabledContainerColor else Color.Transparent,
                    focusedContainerColor = if (isEditing) TextFieldDefaults.colors().focusedContainerColor else Color.Transparent,
                    unfocusedContainerColor = if (isEditing) TextFieldDefaults.colors().unfocusedContainerColor else Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    disabledTextColor = when (true) {
                        isChronic -> Green
                        isAllergies -> Red
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                )
            )
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
        modifier = Modifier
            .fillMaxWidth()
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
    isEditing: Boolean = false,
    data1: String,
    onData1Change: (String) -> Unit = {},
    data2: String,
    onData2Change: (String) -> Unit = {},
    data3: String,
    onData3Change: (String) -> Unit = {},
    data4: String,
    onData4Change: (String) -> Unit = {},
    content: @Composable () -> Unit = {}
) {
    val textFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = Color.Transparent,
        unfocusedContainerColor = Color.Transparent,
        disabledContainerColor = Color.Transparent,
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        // Data 1 (Date) is not editable
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(data1)
        }

        // Editable cells
        @Composable
        fun EditableTableCell(
            text: String,
            onValueChange: (String) -> Unit,
            modifier: Modifier
        ) {
            Box(
                modifier = modifier,
                contentAlignment = Alignment.CenterStart
            ) {
                if (isEditing) {
                    TextField(
                        value = text,
                        onValueChange = onValueChange,
                        colors = textFieldColors,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    Text(text)
                }
            }
        }

        EditableTableCell(
            text = data2,
            onValueChange = onData2Change,
            modifier = Modifier.weight(1f)
        )
        EditableTableCell(
            text = data3,
            onValueChange = onData3Change,
            modifier = Modifier.weight(1f)
        )

        // Cell 4 with content
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            EditableTableCell(
                text = data4,
                onValueChange = onData4Change,
                modifier = Modifier.weight(0.7f)
            )
            content()
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
                modifier = Modifier
                    .wrapContentWidth()
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
                modifier = Modifier
                    .wrapContentWidth()
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
    value2: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Card(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
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
                modifier = Modifier
                    .fillMaxWidth()
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
        modifier = Modifier
            .fillMaxSize()
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

@Composable
fun ShowEmptyMessage() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text("Empty Patient")
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

@Composable
fun GenderFilterChip(
    selectedOption: Gender,
    onSelectItem: (Gender) -> Unit
) {
    val genderOptionList = listOf(Gender.MALE, Gender.FEMALE, Gender.OTHER)

    Row(
        modifier = Modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically
    ) {
        genderOptionList.forEach { option ->
            FilterChip(
                selected = option == selectedOption,
                label = {
                    Text(
                        option.name,
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                },
                onClick = { onSelectItem(option) }
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}


fun Int.readableAge(): String {
    return if (this in 1..11) {
        "$this Months"
    } else {
        "${this / 12} Years"
    }
}





