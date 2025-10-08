package com.gurkha.hr.components.textField

import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.disabledTextFieldBorderColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ERPTimeTestField(
    modifier: Modifier = Modifier,
    value: String?,
    label: String,
    hint: String,
    enabled: Boolean = true,
    rules: List<Rule>,
    error: StringResource?,
    onErrorStateChange: (StringResource?) -> Unit,
    onTimeSelected: (String) -> Unit
) {
    var showTimeDialog by rememberSaveable { mutableStateOf(false) }
    val timeState = rememberTimePickerState(
        initialHour = 12,
        initialMinute = 0,
        is24Hour = false
    )
    Box(
//        modifier = modifier.wrapContentHeight(),
        contentAlignment = Alignment.TopCenter,
    ) {

        ERPTextField(
            text = value ?: "",
            label = label,
            hint = hint,
            onValueChange = {

            },
            enabled = enabled,
            rules = rules,
            showErrorMessage = error != null,
            error = error,
            onErrorStateChange = onErrorStateChange,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.LockClock,
                    contentDescription = "date",
                    tint = if (enabled) MaterialTheme.colorScheme.primaryTextColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor
                )
            },
            readOnly = true,
            onDropDown = {
                showTimeDialog = true
            }
        )

        if (showTimeDialog) {
            TimePickerDialog(
                confirmButton = {
                    ERPButton(
                        onClick = {
                            showTimeDialog = false
                            onTimeSelected("${timeState.hour}:${timeState.minute}")
                        },
                        text = stringResource(SharedRes.Strings.confirm)
                    )
                },
                onDismissRequest = {
                    showTimeDialog = false
                },
                dismissButton = {
                    ERPButton(
                        onClick = {
                            showTimeDialog = false
                        },
                        backgroundColor = MaterialTheme.colorScheme.error,
                        text = stringResource(SharedRes.Strings.cancel)
                    )
                },
                title = {
                    Text(text = hint)
                }
            ) {
                TimePicker(
                    state = timeState
                )
            }
        }
    }
}