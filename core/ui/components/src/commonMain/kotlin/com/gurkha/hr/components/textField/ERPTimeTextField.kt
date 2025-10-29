package com.gurkha.hr.components.textField

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
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
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.res.SharedRes
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
        modifier = modifier.wrapContentHeight(),
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
                    tint = if (enabled) MaterialTheme.erpColors.primaryTextColor else MaterialTheme.erpColors.disabledTextFieldBorderColor
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
                            onTimeSelected(
                                "${
                                    timeState.hour.toString().padStart(2, '0')
                                }:${timeState.minute.toString().padStart(2, '0')}"
                            )
                        },
                        text = stringResource(SharedRes.Strings.confirm)
                    )
                },
                onDismissRequest = {
                    showTimeDialog = false
                },
                dismissButton = {
                    ERPButton(
                        modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small2),
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