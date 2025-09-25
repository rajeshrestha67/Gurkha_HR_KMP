package com.gurkha.hr.leave.leaveRequestPage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.EPRTextField
import com.gurkha.hr.components.textField.RequestFormValidate
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestPage(
    onBackClicked: () -> Unit,
    onSubmitClicked: (startDate: String, endDate: String, leaveDuration: String, leaveType: String, reason: String) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        content = {
                            Icon(Icons.Filled.ArrowBack, contentDescription = "")

                        }
                    )
                },
                title = {
                    Text(
                        text = stringResource(SharedRes.Strings.leave_request_form),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
                        )
                    )
                }
            )
        },
    ) { paddingValues ->
        LeaveRequestPageContent(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            onBackClicked = onBackClicked,
            onSubmitClicked = onSubmitClicked
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun LeaveRequestPageContent(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onSubmitClicked: (String, String, String, String, String) -> Unit
) {
    var selectedLeaveDuration by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var selectedLeaveType by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }
    var startDateActive by remember { mutableStateOf(false) }
    var endDateActive by remember { mutableStateOf(false) }

    val startDatePickerState = rememberDatePickerState()
    val endDatePickerState = rememberDatePickerState()

    var reasonError by remember { mutableStateOf<StringResource?>(null) }
    var startDateError by remember { mutableStateOf<StringResource?>(null) }
    var endDateError by remember { mutableStateOf<StringResource?>(null) }
    var leaveDurationError by remember { mutableStateOf<StringResource?>(null) }
    var leaveTypeError by remember { mutableStateOf<StringResource?>(null) }



    Column(
        modifier = modifier
            .padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small3
            )
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
//        start date
        EPRTextField(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    onClick = {
                        startDateActive = true
                    }
                ),
            rules = RequestFormValidate.dateValidationRules,
            text = startDate,
            validateOnFocusChanged = {},
            label = stringResource(SharedRes.Strings.startDate),
            hint = stringResource(SharedRes.Strings.selectStartDate),
            onValueChange = {
                startDate = it
                startDateError = RequestFormValidate.validateRules(
                    it,
                    RequestFormValidate.dateValidationRules
                )?.errorMsg
            },
            showErrorMessage = startDateError != null,
            error = startDateError?.let {
                SharedRes.Strings.required
            },
            onErrorStateChange = {},
            trailingIcon = {
                Icon(
                    Icons.Filled.CalendarMonth, contentDescription = "",
                    modifier = Modifier.clickable(
                        onClick = {
                            startDateActive = true
                        }
                    ),
                )
            },
            readOnly = true
        )
//        end date
        EPRTextField(
            text = endDate,
            label = stringResource(SharedRes.Strings.endDate),
            hint = stringResource(SharedRes.Strings.selectEndDate),
            onValueChange = {
            },
            rules = RequestFormValidate.dateValidationRules,
            showErrorMessage = endDateError != null,
            error = endDateError?.let {
                SharedRes.Strings.required
            },
            onErrorStateChange = {},
            trailingIcon = {
                Icon(
                    Icons.Filled.CalendarMonth, contentDescription = "",
                    modifier = Modifier.clickable(
                        onClick = {
                            endDateActive = true
                        }
                    ),
                )
            },
            readOnly = true
        )
//        leave duration
        DropDownText(
            label = SharedRes.Strings.leave_duration,
            hint = SharedRes.Strings.select_leave_duration,
            listOfItems = LeaveDurationList.map { stringResource(it.title) },
            selectedValue = selectedLeaveDuration,
            onError = {
               SharedRes.Strings.required
            },
            error = leaveDurationError,
            itemClicked = {
                selectedLeaveDuration = it
            }
        )
//        leave type
        DropDownText(
            label = SharedRes.Strings.leaveType,
            hint = SharedRes.Strings.selectLeaveType,
            listOfItems = LeaveTypeList.map { stringResource(it.title) },
            selectedValue = selectedLeaveType,
            onError = {
                leaveTypeError = SharedRes.Strings.required
            },
            error = leaveTypeError,
            itemClicked = {
                selectedLeaveType = it
            }
        )
//        leave reason
        EPRTextField(
            text = reason,
            label = stringResource(SharedRes.Strings.reason),
            hint = stringResource(SharedRes.Strings.enterReason),
            onValueChange = {
                reason = it
                reasonError = RequestFormValidate.validateRules(
                    it,
                    RequestFormValidate.reasonValidationRules
                )?.errorMsg
            },
            rules = RequestFormValidate.reasonValidationRules,
            showErrorMessage = reasonError != null,
            error = reasonError?.let {
                SharedRes.Strings.required
            },
            onErrorStateChange = {
            },
            height = MaterialTheme.dimens.reasonTextField
        )
        Spacer(
            modifier = Modifier.weight(1f)
        )

//        buttons for cancel and submit
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
        ) {
            ERPButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    reasonError = RequestFormValidate.validateRules(
                        reason,
                        RequestFormValidate.reasonValidationRules
                    )?.errorMsg
                    leaveTypeError = RequestFormValidate.validateRules(
                        reason,
                        RequestFormValidate.reasonValidationRules
                    )?.errorMsg
                    leaveDurationError = RequestFormValidate.validateRules(
                        reason,
                        RequestFormValidate.reasonValidationRules
                    )?.errorMsg
                    startDateError = RequestFormValidate.validateRules(
                        reason,
                        RequestFormValidate.reasonValidationRules
                    )?.errorMsg
                    endDateError = RequestFormValidate.validateRules(
                        reason,
                        RequestFormValidate.reasonValidationRules
                    )?.errorMsg
                    if (reasonError == null  && leaveTypeError == null && leaveDurationError == null && endDateError == null && startDateError == null) {
                        onSubmitClicked(
                            startDate,
                            endDate,
                            selectedLeaveDuration,
                            selectedLeaveType,
                            reason
                        )
                    }


                },
                text = stringResource(SharedRes.Strings.submit),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))
            ERPButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.error,
                onClick = onBackClicked,
                text = stringResource(SharedRes.Strings.cancel),
            )

//            for the start date
            if (startDateActive) {
                CostumeDatePicker(
                    state = startDatePickerState,
                    onDismiss = {
                        startDateActive = false
                    },
                    onDatePick = {
                        startDate = it
                        startDateActive = false
                    }
                )
            }
//for the end date
            if (endDateActive) {
                CostumeDatePicker(
                    state = endDatePickerState,
                    onDismiss = {
                        endDateActive = false
                    },
                    onDatePick = {
                        endDate = it
                        endDateActive = false
                    }
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun CostumeDatePicker(
    state: DatePickerState,
    onDismiss: () -> Unit,
    onDatePick: (String) -> Unit
) {
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

