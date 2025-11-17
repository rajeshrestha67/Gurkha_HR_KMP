package com.gurkha.hr.leave.leaveRequestPage

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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.date.model.DatePickerDefaults
import com.gurkha.hr.components.date.model.DatePickerDefaults.FutureAndTodayDate
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.hideKeyboardOnTap
import com.gurkha.hr.components.isKeyboardVisible
import com.gurkha.hr.components.loadingScreen.LoadingScreen
import com.gurkha.hr.components.prompts.PromptModalBottomSheet
import com.gurkha.hr.components.prompts.PromptType
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.todayInBS
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenAction
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenState
import com.gurkha.hr.res.SharedRes
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestScreen(
    navController: NavHostController,
    onBackPressed: () -> Unit
) {

    val viewModel: LeaveRequestScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var sendData by remember { mutableStateOf(false) }
    var showFailedDialogue by remember { mutableStateOf(false) }
    var showSuccessDialogue by remember { mutableStateOf(false) }
    var messageToShow by remember { mutableStateOf("") }

    LaunchedEffect(sendData) {
        if (sendData) {
            val data = state.leaveRequestData
            data?.let {
                val stringData = Json.encodeToString(data)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("data", stringData)
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.dataChannel.collect { data ->
            data?.let {
                viewModel.onAction(LeaveRequestScreenAction.UpdateLeaveRequestData(data))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.successChannel.collect { it ->
            it.let {
                showSuccessDialogue = true
                messageToShow = it
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.errorChannel.collect { it ->
            it.let {
                showFailedDialogue = true
                messageToShow = it
            }
        }
    }



    LeaveRequestPageContent(
        onBackPressed = onBackPressed,
        state = state,
        onAction = viewModel::onAction,
        showSuccessDialogue = showSuccessDialogue,
        showFailedDialogue = showFailedDialogue,
        messageToShow = messageToShow,
        onSendData = {
            sendData = true
        },
        onDismiss = {
            showFailedDialogue = false
        }
    )


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun LeaveRequestPageContent(
    onBackPressed: () -> Unit,
    state: LeaveRequestScreenState,
    onAction: (LeaveRequestScreenAction) -> Unit,
    showSuccessDialogue: Boolean,
    showFailedDialogue: Boolean,
    messageToShow: String,
    onSendData: () -> Unit,
    onDismiss: () -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val isKeyboardOpen by isKeyboardVisible()
    val focusManager = LocalFocusManager.current

    Scaffold(
        modifier = Modifier.fillMaxSize()
            .hideKeyboardOnTap(
                focusManager = focusManager,
                keyboardController = keyboardController
            ),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(),
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
                title = {
                    Text(
                        text = stringResource(SharedRes.Strings.leave_request_form),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.erpColors.primaryTextColor
                        )
                    )
                }
            )
        },
    ) { paddingValues ->

        Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues = paddingValues)
                .hideKeyboardOnTap(
                    focusManager = focusManager,
                    keyboardController = keyboardController
                ).imePadding(),
        ) {
            if (state.isRequestingLeave) {
                LoadingScreen()
            }
            LeaveRequestScreenForm(
                modifier = Modifier.fillMaxSize(),
                onBackPressed = onBackPressed,
                state = state,
                onAction = onAction,
                showSuccessDialogue = showSuccessDialogue,
                showFailedDialogue = showFailedDialogue,
                messageToShow = messageToShow,
                onSendData = onSendData,
                keyboardController = keyboardController,
                onDismiss = onDismiss
            )
        }

    }


}

@Composable
fun LeaveRequestScreenForm(
    modifier: Modifier = Modifier,
    state: LeaveRequestScreenState,
    onBackPressed: () -> Unit,
    onAction: (LeaveRequestScreenAction) -> Unit,
    showSuccessDialogue: Boolean,
    showFailedDialogue: Boolean,
    messageToShow: String,
    onSendData: () -> Unit,
    keyboardController: SoftwareKeyboardController?,
    onDismiss: () -> Unit
) {

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
//start date
        ERPDateTextField(
            modifier = Modifier
                .fillMaxWidth(),
            rules = FormValidate.requiredValidationRules,
            value = state.startDate,
            label = stringResource(SharedRes.Strings.startDate),
            hint = stringResource(SharedRes.Strings.selectStartDate),
            error = state.startDateError,
            onErrorStateChange = {
            },
            selectableDates = FutureAndTodayDate,
            onDateSelected = {
                onAction(LeaveRequestScreenAction.OnStartDateChange(it))
            }
        )
//        end date
        ERPDateTextField(
            enabled = state.startDate != null,
            value = state.endDate,
            label = stringResource(SharedRes.Strings.endDate),
            hint = stringResource(SharedRes.Strings.selectEndDate),
            rules = FormValidate.requiredValidationRules,
            error = state.endDateError,
            onErrorStateChange = {
            },
            selectableDates = DatePickerDefaults.RangeSelectableDates(
                minDate = CalendarDate.todayInBS()
            ),
            onDateSelected = {
                onAction(LeaveRequestScreenAction.OnEndDateChange(it))
            }
        )

        //        assignee
        DropDownText(
            label = SharedRes.Strings.assignee,
            hint = stringResource(SharedRes.Strings.select_assignee),
            rules = FormValidate.requiredValidationRules,
            isFetching = state.isAssigneeLoading,
            isFetchingError = state.isAssigneeFetchingError,
            listOfItems = state.leaveAssigneeList ?: emptyList(),
            selectedValue = state.assignee?.name ?: "",
            onError = {
            },
            error = state.assigneeError,
            itemClicked = {
                onAction(LeaveRequestScreenAction.OnAssigneeChange(assignee = it))
            },
            onRetry = {
                onAction(LeaveRequestScreenAction.OnRefetchAssignee)
            }
        )


//        leave duration
        DropDownText(
            label = SharedRes.Strings.leave_duration,
            hint = stringResource(SharedRes.Strings.select_leave_duration),
            rules = FormValidate.requiredValidationRules,
            listOfItems = state.leaveDurationList,
            selectedValue = state.leaveDuration?.name ?: "",
            onError = {
            },
            error = state.leaveDurationError,
            itemClicked = {
                onAction(LeaveRequestScreenAction.OnLeaveDurationChange(it))
            }
        )
//        leave type
        DropDownText(
            label = SharedRes.Strings.leaveType,
            hint = stringResource(SharedRes.Strings.selectLeaveType),
            rules = FormValidate.requiredValidationRules,
            isFetching = state.isLeaveTypeLoading,
            isFetchingError = state.isLeaveTypeFetchingError,
            listOfItems = state.leaveTypeList ?: emptyList(),
            selectedValue = state.leaveType?.name ?: "",
            onError = {
            },
            error = state.leaveTypeError,
            itemClicked = {
                onAction(LeaveRequestScreenAction.OnLeaveTypeChange(it))
            },
            onRetry = {
                onAction(LeaveRequestScreenAction.OnRefetchLeaveType)
            }
        )

//        leave reason
        ERPTextField(
            text = state.reason,
            label = stringResource(SharedRes.Strings.reason),
            hint = stringResource(SharedRes.Strings.enterReason),
            onValueChange = {
                onAction(LeaveRequestScreenAction.OnReasonChange(it))
            },
            rules = FormValidate.requiredValidationRules,
            error = state.reasonError,
            onErrorStateChange = {
                onAction(LeaveRequestScreenAction.OnReasonError(it))
            },
            imeAction = ImeAction.Send,
            keyboardActions = KeyboardActions(
                onSend = {
                    keyboardController?.hide()
                    onAction(LeaveRequestScreenAction.Submit)
                }
            ),
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
                    onAction(LeaveRequestScreenAction.Submit)
                },
                isLoading = state.isRequestingLeave,
                text = stringResource(SharedRes.Strings.submit),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.dimens.small3))
            ERPButton(
                modifier = Modifier.fillMaxWidth(),
                backgroundColor = MaterialTheme.colorScheme.error,
                onClick = onBackPressed,
                text = stringResource(SharedRes.Strings.cancel),
            )
        }

        if (showSuccessDialogue) {
            PromptModalBottomSheet(
                text = messageToShow,
                onBackPressed = {
                    onSendData()
//                    onBackPressed
                }
            )
        }

        if (showFailedDialogue) {
            PromptModalBottomSheet(
                promptType = PromptType.FAILED,
                text = messageToShow,
                onBackPressed = onDismiss
            )
        }
    }
}

