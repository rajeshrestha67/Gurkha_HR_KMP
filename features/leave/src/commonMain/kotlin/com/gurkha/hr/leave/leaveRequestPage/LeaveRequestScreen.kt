package com.gurkha.hr.leave.leaveRequestPage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.date.FutureAndTodayDate
import com.gurkha.hr.components.date.RangeSelectableDates
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenAction
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.model.leave.leave_request.LeaveRequestData
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestScreen(
    navController: NavHostController,
    json: String?,
    onBackClicked: () -> Unit
) {

    val viewModel: LeaveRequestScreenViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dataChannel.collect { data ->
            data?.let { safeData ->
                val stringData = Json.encodeToString(safeData)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("data", stringData)
                navController.popBackStack()
            }
        }
    }

    LaunchedEffect(json) {
        json?.let {
            val data = Json.decodeFromString<LeaveRequestData>(it)
            viewModel.onAction(LeaveRequestScreenAction.UpdateLeaveRequestData(data))
        }
    }
    LeaveRequestPageContent(
        onBackClicked = onBackClicked,
        state = state,
        onAction = viewModel::onAction,
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun LeaveRequestPageContent(
    onBackClicked: () -> Unit,
    state: LeaveRequestScreenState,
    onAction: (LeaveRequestScreenAction) -> Unit,
) {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                navigationIcon = {
                    IconButton(
                        onClick = onBackClicked,
                        content = {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                        }
                    )
                },
                title = {
                    Text(
                        text = stringResource(SharedRes.Strings.leave_request_form),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primaryTextColor
                        )
                    )
                }
            )
        },
    ) { paddingValues ->
        LeaveRequestScreenForm(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            onBackClicked = onBackClicked,
            state = state,
            onAction = onAction
        )
    }


}

@Composable
fun LeaveRequestScreenForm(
    modifier: Modifier = Modifier,
    state: LeaveRequestScreenState,
    onBackClicked: () -> Unit,
    onAction: (LeaveRequestScreenAction) -> Unit,
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
            selectableDates = RangeSelectableDates(
                minDateMillis = state.startDate?.actualValue?.plus(1.days.toLong(DurationUnit.DAYS))
            ),
            onDateSelected = {
                onAction(LeaveRequestScreenAction.OnEndDateChange(it))
            }
        )

        //        assignee
        DropDownText(
            label = SharedRes.Strings.assignee,
            hint = SharedRes.Strings.select_assignee,
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
            hint = SharedRes.Strings.select_leave_duration,
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
            hint = SharedRes.Strings.selectLeaveType,
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
            text = state.reason ?: "",
            label = stringResource(SharedRes.Strings.reason),
            hint = stringResource(SharedRes.Strings.enterReason),
            onValueChange = {
                onAction(LeaveRequestScreenAction.OnReasonChange(it))
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Send),
            rules = FormValidate.requiredValidationRules,
            error = state.reasonError,
            onErrorStateChange = {
                onAction(LeaveRequestScreenAction.OnReasonError(it))
            },
            onImeAction = {
                onAction(LeaveRequestScreenAction.Submit)
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
                    onAction(LeaveRequestScreenAction.Submit)
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
    }
}

