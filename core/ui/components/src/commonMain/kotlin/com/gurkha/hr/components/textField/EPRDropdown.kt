package com.gurkha.hr.components.textField

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.primaryTextColor
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@Composable
fun EPRDateTextField(
    modifier: Modifier = Modifier,
    text: String,
    label: String,
    hint: String,
    onValueChange: (String) -> Unit,
    rules: List<Rule>,
    error: StringResource?,
    onErrorStateChange: (ErrorStatus?) -> Unit
) {

    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = modifier.wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {

        EPRTextField(
            text = text,
            label = label,
            hint = hint,
            onValueChange = onValueChange,
            rules = rules,
            showErrorMessage = error != null,
            error = error,
            onErrorStateChange = onErrorStateChange,
            trailingIcon = {
                Icon(
                    imageVector = Icons.Filled.CalendarMonth,
                    contentDescription = "date",
                    tint = MaterialTheme.colorScheme.primaryTextColor
                )
            },
            readOnly = true,
            enabled = false,
            onDropDown = {
                showDateDialog = true
            }
        )

        if (showDateDialog) {
            DatePickerDialog(
                onDismiss = {
                    showDateDialog = false
                },
                onDatePick = {
                    onValueChange(it)
                    showDateDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDatePick: (String) -> Unit
) {
    val state = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            ERPButton(
                onClick = {
                    state.selectedDateMillis?.let { millis ->
                        onDatePick(millis.toFormattedDate()) // simply call the utility
                    }
                },
                text = stringResource(SharedRes.Strings.confirm)
            )
        },
        dismissButton = {
            ERPButton(
                onClick = onDismiss,
                backgroundColor = MaterialTheme.colorScheme.error,
                text = stringResource(SharedRes.Strings.cancel)
            )
        }
    ) {
        DatePicker(
            state = state
        )
    }
}


//date formater
@OptIn(ExperimentalTime::class)
fun Long.toFormattedDate(pattern: String = "yyyy-MM-dd"): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date

    return when (pattern) {
        "MM/dd/yyyy" -> "${
            localDate.month.number.toString().padStart(2, '0')
        }/${localDate.day.toString().padStart(2, '0')}/${localDate.year}"

        else -> "${localDate.year}-${
            localDate.month.number.toString().padStart(2, '0')
        }-${localDate.day.toString().padStart(2, '0')}"
    }
}