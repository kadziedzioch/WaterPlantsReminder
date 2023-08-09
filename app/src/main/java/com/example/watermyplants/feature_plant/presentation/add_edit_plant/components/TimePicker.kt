package com.example.watermyplants.feature_plant.presentation.add_edit_plant.components

import android.content.ContentValues
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import com.example.watermyplants.feature_plant.domain.utils.TimePeriod
import com.example.watermyplants.feature_plant.presentation.add_edit_plant.DropDownMenuState
import java.time.Duration

@Composable
fun TimePicker(
    modifier: Modifier = Modifier,
    duration: String,
    dropDownMenuState: DropDownMenuState,
    changeTimePeriod: (String) -> Unit,
    changeExpandedState : ()->Unit,
    onTextChange: (String) -> Unit
) {
    var mTextFieldSize by remember { mutableStateOf(Size.Zero) }

    val icon = if (dropDownMenuState.isExpanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Row(modifier = modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = duration,
            onValueChange = {onTextChange(it)},
            modifier = Modifier.weight(2f),
            maxLines = 1,
            label = { Text(text = "Time") },
            placeholder = {
                Text(text = "1")
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)

        )
        Spacer(modifier = Modifier.width(7.dp))
        Column(modifier = Modifier.weight(4f)) {
            OutlinedTextField(
                value = dropDownMenuState.selectedTimeUnit.name,
                onValueChange = { changeTimePeriod(it) },
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        mTextFieldSize = coordinates.size.toSize()
                    }
                    .clickable(onClick = {
                        changeExpandedState()
                    }),
                label = {Text("Time unit")},
                trailingIcon = {
                    Icon(icon,null)
                },
                enabled = false,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    disabledTextColor = LocalContentColor.current,
                    disabledBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    disabledLabelColor = MaterialTheme.colorScheme.onSurface
                )
            )
            DropdownMenu(
                expanded = dropDownMenuState.isExpanded,
                onDismissRequest = changeExpandedState,
                modifier = Modifier
                    .width(with(LocalDensity.current){mTextFieldSize.width.toDp()})
            ) {
                TimePeriod.values().forEach { timeUnit ->
                    DropdownMenuItem(
                        onClick = {
                            changeTimePeriod(timeUnit.name)
                        },
                        text = {
                            Text(text = timeUnit.name)
                        }
                    )
                    
                }
            }
        }

    }

}