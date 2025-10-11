package com.gurkha.hr.attendanceRequestScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.date.FutureAndTodayDate
import com.gurkha.hr.components.prompts.PromptModalBottomSheet
import com.gurkha.hr.components.prompts.PromptType
import com.gurkha.hr.components.textField.DropDownText

import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.ERPTimeTestField
import com.gurkha.hr.components.textField.FormValidate

import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestAction
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreen(
    navController: NavHostController,
    onBackClicked: () -> Unit
) {
    val viewModel: AttendanceRequestViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showSuccessDialogue by remember { mutableStateOf(false) }
    var showFailedDialogue by remember { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var sendData by remember { mutableStateOf(false) }

    LaunchedEffect(sendData) {
        if (sendData) {
            val data = AttendanceRequestData(
                assigneeId = state.attendanceRequestData?.assigneeId ?: "",
                date = state.attendanceRequestData?.date ?: "",
                clockInTime = state.attendanceRequestData?.clockInTime ?: "",
                clockOutTime = state.attendanceRequestData?.clockOutTime ?: "",
                remarks = state.attendanceRequestData?.remarks ?: "",
            )
            data.let {
                val stringData = Json.encodeToString(data)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("data", stringData)
//                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dataChannel.collect { it ->
            it?.let {
                viewModel.onAction(AttendanceRequestAction.OnUpdateAttendanceRequestData(it))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.successChannel.collect {
            it?.let {
                showSuccessDialogue = true
                message = it
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errorChannel.collect {
            it?.let {
                showFailedDialogue = true
                message = it
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            contentWindowInsets = WindowInsets(0.dp),
            topBar = {
                TopAppBar(
                    windowInsets = WindowInsets(0.dp),
                    title = {
                        Text(text = stringResource(SharedRes.Strings.attendance_request_form))
                    },
                    navigationIcon = {
                        IconButton(
                            onClick = onBackClicked,
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = ""
                                )
                            }
                        )
                    },
                )
            },
        ) { contentPadding ->
            AttendanceRequestScreenContent(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                onAction = viewModel::onAction,
                onBackClicked = onBackClicked,
                state = state,
                showSuccessDialogue = showSuccessDialogue,
                showFailedDialogue = showFailedDialogue,
                message = message,
                onSendData = {
                    sendData = true
                }
            )
        }

        if (state.isRequestingAttendance) {
            Box(
                modifier = Modifier.fillMaxSize().background(color = Color(0x80000000)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(MaterialTheme.dimens.medium3),
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreenContent(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onAction: (AttendanceRequestAction) -> Unit,
    state: AttendanceRequestState,
    showSuccessDialogue: Boolean,
    showFailedDialogue: Boolean,
    message: String,
    onSendData: () -> Unit
) {
    val radioOptions = listOf("Clock In", "Clock Out")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(
        modifier = modifier.fillMaxWidth()
            .padding(
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.bottomBar,
                end = MaterialTheme.dimens.small3,
                start = MaterialTheme.dimens.small3
            )
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
    ) {
        ERPDateTextField(
            modifier = Modifier
                .fillMaxWidth(),
            rules = FormValidate.requiredValidationRules,
            value = state.date,
            label = stringResource(SharedRes.Strings.select_date),
            hint = stringResource(SharedRes.Strings.select_date),
            error = state.dateError,
            onErrorStateChange = {
            },
            selectableDates = FutureAndTodayDate,
            onDateSelected = {
                onAction(AttendanceRequestAction.OnDateChange(it))
            }
        )
//radio option for the clock in time or clock out time selection
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            radioOptions.forEach { text ->
                Row(
                    Modifier
                        .weight(1f)
                        .selectable(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                            }
                        ),
                    verticalAlignment = Alignment.CenterVertically

                ) {
                    RadioButton(
                        selected = (text == selectedOption),
                        onClick = { onOptionSelected(text) }
                    )
                    Text(
                        text = text,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = if (text == selectedOption) MaterialTheme.colorScheme.primaryTextColor else MaterialTheme.colorScheme.secondaryTextColor
                        ),
                    )
                }
            }
        }

        if (selectedOption == radioOptions[0]) {
            //        clock in time
            ERPTimeTestField(
                modifier = Modifier.fillMaxWidth(),
                rules = FormValidate.requiredValidationRules,
                value = state.clockInTime,
                label = stringResource(SharedRes.Strings.clock_in_time),
                hint = stringResource(SharedRes.Strings.clock_in_time),
                enabled = true,
                error = state.clockInOutError,
                onErrorStateChange = {
                },
                onTimeSelected = {
                    onAction(AttendanceRequestAction.OnClockInTimeChange(it))
                }
            )
        } else {
            //        clock out time
            ERPTimeTestField(
                modifier = Modifier.fillMaxWidth(),
                value = state.clockOutTime,
                label = stringResource(SharedRes.Strings.clock_out_time),
                hint = stringResource(SharedRes.Strings.clock_out_time),
                enabled = true,
                rules = FormValidate.requiredValidationRules,
                error = state.clockInOutError,
                onErrorStateChange = {
                },
                onTimeSelected = {
                    onAction(AttendanceRequestAction.OnClockOutTimeChange(it))
                }
            )
        }


//        select assignee
        DropDownText(
            label = SharedRes.Strings.assignee,
            hint = SharedRes.Strings.select_assignee,
            rules = FormValidate.requiredValidationRules,
            isFetching = state.isAssigneeLoading,
            isFetchingError = state.isAssigneeFetchingError,
            listOfItems = state.assigneeList ?: emptyList(),
            selectedValue = state.assignee?.name ?: "",
            onError = {
            },
            error = state.assigneeError,
            itemClicked = {
                onAction(AttendanceRequestAction.OnAssigneeChange(it))
            },
            onRetry = {
            }
        )

        //        leave reason
        ERPTextField(
            text = state.reason ?: "",
            label = stringResource(SharedRes.Strings.reason),
            hint = stringResource(SharedRes.Strings.enterReason),
            onValueChange = {
                onAction(AttendanceRequestAction.OnReasonChange(it))

            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            rules = FormValidate.requiredValidationRules,
            error = state.reasonError,
            onErrorStateChange = {
            },
            height = MaterialTheme.dimens.reasonTextField
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
                    onAction(AttendanceRequestAction.OnSubmit)
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
        }

//        show the success modal
        if (showSuccessDialogue) {
            PromptModalBottomSheet(
                text = message,
                onBackClicked = {
//                    trigger the send data back launched effect
                    onSendData()
//                    go to prev screen
                    onBackClicked()
                }
            )
        }
//        show error modal
        if (showFailedDialogue) {
            PromptModalBottomSheet(
                text = message,
                promptType = PromptType.FAILED,
                buttonText = SharedRes.Strings.cancel,
                onBackClicked = onBackClicked
            )
        }
    }
}

