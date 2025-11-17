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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.model.DatePickerDefaults
import com.gurkha.hr.components.date.ui.CalendarContent
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.Rule
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

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

    val a: androidx.compose.material3.SelectableDates
    var showDateDialog by rememberSaveable { mutableStateOf(false) }
    var displayInAd by rememberSaveable { mutableStateOf(false) }
    val defaultDate = remember(value) {
        value?.let {
            DateData.getCalendarDateBS(it.displayValueBS)
        }
    }

    Box(
        modifier = modifier.wrapContentHeight(),
        contentAlignment = Alignment.TopCenter
    ) {

        ERPTextField(
            text = if (displayInAd) value?.displayValueAD ?: "" else value?.displayValueBS ?: "",
            label = label,
            hint = hint,
            onValueChange = {},
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
                                    color = MaterialTheme.erpColors.primaryTextColor.copy(
                                        alpha = if (enabled) 1f else 0.5f
                                    )
                                )
                            )
                        }
                    }
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = "date",
                        tint = if (enabled) MaterialTheme.erpColors.primaryTextColor else MaterialTheme.erpColors.disabledTextFieldBorderColor
                    )
                }

            },
            readOnly = true,
            onDropDown = {
                showDateDialog = true
            }
        )

        if (showDateDialog) {
            DatePickerModalBottomSheet(
                defaultDate = defaultDate,
                selectableDates = selectableDates,
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModalBottomSheet(
    defaultDate: CalendarDate? = null,
    selectableDates: SelectableDates,
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
                selectedDate = defaultDate,
                selectableDates = selectableDates,
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