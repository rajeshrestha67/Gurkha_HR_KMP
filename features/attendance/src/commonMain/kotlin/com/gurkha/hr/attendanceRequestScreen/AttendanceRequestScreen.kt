package com.gurkha.hr.attendanceRequestScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.date.FutureAndTodayDate
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.hideKeyboardOnTap
import com.gurkha.hr.components.isKeyboardVisible
import com.gurkha.hr.components.loadingScreen.LoadingScreen
import com.gurkha.hr.components.prompts.PromptModalBottomSheet
import com.gurkha.hr.components.prompts.PromptType
import com.gurkha.hr.components.radioButton.RadioGroup
import com.gurkha.hr.components.radioButton.RadioGroupOrientation
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.ERPTimeTestField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestAction
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestState
import com.gurkha.hr.res.SharedRes
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestData
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit,
    date: String?,
    clockStatus: String?
) {
    val viewModel: AttendanceRequestViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showSuccessDialogue by remember { mutableStateOf(false) }
    var showFailedDialogue by remember { mutableStateOf(false) }
    var message by rememberSaveable { mutableStateOf("") }
    var sendData by remember { mutableStateOf(false) }

    LaunchedEffect(date) {
        if (date != null && clockStatus != null) {
            viewModel.onAction(
                AttendanceRequestAction.OnReceivedDataChange(
                    date = date,
                    clockStatus = clockStatus
                )
            )
        }
    }

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
    AttendanceRequestScreenContent(
        onBackPressed = onBackPressed,
        onAction = viewModel::onAction,
        state = state,
        showSuccessDialogue = showSuccessDialogue,
        showFailedDialogue = showFailedDialogue,
        message = message,
        sendData = {
            sendData = true
        },
        onDismiss = {
            showFailedDialogue = false
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreenContent(
    onBackPressed: () -> Unit,
    onAction: (AttendanceRequestAction) -> Unit,
    state: AttendanceRequestState,
    showSuccessDialogue: Boolean,
    showFailedDialogue: Boolean,
    message: String,
    sendData: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {

    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by isKeyboardVisible()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize().imePadding()
            .hideKeyboardOnTap(
                focusManager = focusManager,
                keyboardController = keyboardController
            ),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(),
                title = {
                    Text(text = stringResource(SharedRes.Strings.attendance_request_form))
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (isKeyboardOpen) {
                                keyboardController?.hide()
                            } else {
                                onBackPressed()
                            }
                        },
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
        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues = contentPadding)
                .hideKeyboardOnTap(
                    focusManager = focusManager,
                    keyboardController = keyboardController
                ),
        ) {
            if (state.isRequestingAttendance) {
                LoadingScreen()
            }
            AttendanceRequestScreenForm(
                modifier = Modifier
                    .fillMaxSize(),
                onAction = onAction,
                onBackClicked = onBackPressed,
                state = state,
                showSuccessDialogue = showSuccessDialogue,
                showFailedDialogue = showFailedDialogue,
                message = message,
                onSendData = {
                    sendData(true)
                },
                keyboardController = keyboardController,
                onDismiss = onDismiss
            )
        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreenForm(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onAction: (AttendanceRequestAction) -> Unit,
    state: AttendanceRequestState,
    showSuccessDialogue: Boolean,
    showFailedDialogue: Boolean,
    message: String,
    onSendData: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onDismiss: () -> Unit
) {

    Column(
        modifier = modifier.fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(
                top = MaterialTheme.dimens.small2,
                bottom = 0.dp,
                end = MaterialTheme.dimens.small3,
                start = MaterialTheme.dimens.small3
            )

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

        RadioGroup(
            modifier = Modifier
                .fillMaxWidth(),
            options = state.radioOptions,
            selectedOption = state.selectedOption,
            onOptionSelected = {
                onAction(AttendanceRequestAction.OnRadioOptionChange(it))
            },
            optionLabel = { text, isSelected ->
                Text(
                    text = text.value,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = if (isSelected) MaterialTheme.erpColors.primaryTextColor else MaterialTheme.erpColors.secondaryTextColor
                    ),
                )
            },
            orientation = RadioGroupOrientation.Horizontal()
        )

        if (state.selectedOption == state.radioOptions.first()) {
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
            hint = stringResource(SharedRes.Strings.select_assignee),
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
            imeAction = ImeAction.Send,
            keyboardActions = KeyboardActions(
                onSend = {
                    keyboardController?.hide()
                    onAction(AttendanceRequestAction.OnSubmit)
                }
            ),
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
                    keyboardController?.hide()
                    onAction(AttendanceRequestAction.OnSubmit)
                },
                isLoading = state.isRequestingAttendance,
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
                onBackPressed = {
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
                onBackPressed = onDismiss
            )
        }
    }
}

