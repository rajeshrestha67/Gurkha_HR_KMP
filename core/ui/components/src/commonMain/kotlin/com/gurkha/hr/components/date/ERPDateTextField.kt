package com.gurkha.hr.components.date

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ui.CalendarContent
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.Rule
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.disabledTextFieldBorderColor
import com.gurkha.hr.res.theme.primaryTextColor
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ERPDateTextField(
    modifier: Modifier = Modifier,
    value: DateData?,
    label: String,
    hint: String,
    enabled: Boolean = true,
    rules: List<Rule>,
    error: StringResource?,
    selectableDates: SelectableDates = DatePickerDefaults.AllDates,
    onErrorStateChange: (StringResource?) -> Unit,
    onDateSelected: (DateData) -> Unit
) {

    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    var displayInAd by rememberSaveable { mutableStateOf(false) }

    Box(
        modifier = modifier.wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {

        ERPTextField(
            text = if (displayInAd) value?.displayValueAD ?: "" else value?.displayValueBS ?: "",
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
                Row(
                    modifier = Modifier.padding(end = MaterialTheme.dimens.small3),
                    horizontalArrangement = Arrangement.spacedBy(
                        space = MaterialTheme.dimens.small1,
                        alignment = Alignment.CenterHorizontally
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        enabled = enabled && value != null,
                        onClick = {
                            displayInAd = !displayInAd
                        }
                    ) {
                        value?.let {
                            Text(
                                text = stringResource(
                                    resource =
                                        if (displayInAd) SharedRes.Strings.ad else SharedRes.Strings.bs
                                ),
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    color = MaterialTheme.colorScheme.primaryTextColor.copy(
                                        alpha = if (enabled) 1f else 0.5f
                                    )
                                )
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "date",
                        tint = if (enabled) MaterialTheme.colorScheme.primaryTextColor else MaterialTheme.colorScheme.disabledTextFieldBorderColor
                    )
                }

            },
            readOnly = true,
            onDropDown = {
                showDateDialog = true
            }
        )

        if (showDateDialog) {
//            DatePickerDialog(
//                onDismiss = {
//                    showDateDialog = false
//                },
//                initialSelectedDateMillis = value?.actualValue,
//                selectableDates = selectableDates,
//                onDatePick = {
//                    onDateSelected(it)
//                    showDateDialog = false
//                }
//            )
            DatePickerModalBottomSheet(
                onDismiss = {
                    showDateDialog = false
                },
                onDatePick = {
                    onDateSelected(it)
                    showDateDialog = false
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
private fun DatePickerDialog(
    onDismiss: () -> Unit,
    initialSelectedDateMillis: Long? = null,
    selectableDates: SelectableDates,
    onDatePick: (DateData) -> Unit
) {
    val state = rememberDatePickerState(
        initialSelectedDateMillis = initialSelectedDateMillis,
        selectableDates = selectableDates
    )
    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {

        },
        dismissButton = {
            ERPButton(
                onClick = onDismiss,
                backgroundColor = MaterialTheme.colorScheme.error,
                text = stringResource(SharedRes.Strings.cancel)
            )
        }
    ) {

    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalBottomSheet(
    onDismiss: () -> Unit,
    onDatePick: (DateData) -> Unit
) {

    var selectedDate by rememberSaveable { mutableStateOf<CalendarDate?>(null) }

    val sheet = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    ModalBottomSheet(
        sheetState = sheet,
        modifier = Modifier
            .fillMaxWidth()
            .statusBarsPadding(),
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background,
    ) {

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            CalendarContent(
                onDateSelected = {
                    selectedDate = it
                }
            )

            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    space = MaterialTheme.dimens.small2,
                    alignment = Alignment.End
                ),
                modifier = Modifier.fillMaxWidth().padding(all = MaterialTheme.dimens.small2)
            ) {

                ERPButton(
                    onClick = onDismiss,
                    backgroundColor = MaterialTheme.colorScheme.error,
                    text = stringResource(SharedRes.Strings.cancel)
                )
                ERPButton(
                    onClick = {
                        onDismiss()
                        selectedDate?.let { dateInBS ->
                            onDatePick(
                                DateData.fromDisplayBS(
                                    displayValue = "${dateInBS.year}-${
                                        dateInBS.month.toString().padStart(2, '0')
                                    }-${dateInBS.dayOfMonth.toString().padStart(2, '0')}"
                                )
                            )
                        }
                    },
                    text = stringResource(SharedRes.Strings.confirm)
                )
            }
        }

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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
val FutureAndTodayDate: RangeSelectableDates =
    object : RangeSelectableDates(minDateMillis = Clock.System.now().toEpochMilliseconds()) {}

@OptIn(ExperimentalMaterial3Api::class)
open class RangeSelectableDates(
    private val minDateMillis: Long? = null,
    private val maxDateMillis: Long? = null
) : SelectableDates {

    override fun isSelectableDate(utcTimeMillis: Long): Boolean {
        val afterMin = minDateMillis?.let { utcTimeMillis >= it } ?: true
        val beforeMax = maxDateMillis?.let { utcTimeMillis <= it } ?: true
        return afterMin && beforeMax
    }
}
