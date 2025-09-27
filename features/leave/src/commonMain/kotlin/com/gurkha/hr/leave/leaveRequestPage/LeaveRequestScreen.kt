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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.EPRTextField
import com.gurkha.hr.components.textField.ERPDateTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.FutureAndTodayDate
import com.gurkha.hr.components.textField.RangeSelectableDates
import com.gurkha.hr.leave.model.leave_request.LeaveDurationList
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenAction
import com.gurkha.hr.leave.model.leave_request.LeaveRequestScreenState
import com.gurkha.hr.leave.model.leave_request.LeaveTypeList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.time.Duration.Companion.days
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveRequestScreen(
    onBackClicked: () -> Unit,
    onSubmitClicked: (startDate: String, endDate: String, leaveDuration: String, leaveType: String, reason: String) -> Unit
) {

    val viewModel: LeaveRequestScreenViewModel = koinViewModel()

    val state by viewModel.state.collectAsStateWithLifecycle()
    LeaveRequestPageContent(
        onBackClicked = onBackClicked,
        onSubmitClicked = onSubmitClicked,
        state = state,
        onAction = viewModel::onAction
    )

}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTime::class)
@Composable
fun LeaveRequestPageContent(
    onBackClicked: () -> Unit,
    onSubmitClicked: (String, String, String, String, String) -> Unit,
    state: LeaveRequestScreenState,
    onAction: (LeaveRequestScreenAction) -> Unit
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
                            Icon(Icons.Filled.ArrowBack, contentDescription = "")
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
    onAction: (LeaveRequestScreenAction) -> Unit
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

        ERPDateTextField(
            modifier = Modifier
                .fillMaxWidth(),
            rules = FormValidate.requiredValidationRules,
            value = state.startDate,
            label = stringResource(SharedRes.Strings.startDate),
            hint = stringResource(SharedRes.Strings.selectStartDate),
            error = state.startDateError,
            onErrorStateChange = {
                onAction(LeaveRequestScreenAction.OnStartDateError(it))
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
                onAction(LeaveRequestScreenAction.OnEndDateError(it))
            },
            selectableDates = RangeSelectableDates(
                minDateMillis = state.startDate?.actualValue?.plus(1.days.toLong(DurationUnit.DAYS))
            ),
            onDateSelected = {
                onAction(LeaveRequestScreenAction.OnEndDateChange(it))
            }
        )
//        leave duration
        DropDownText(
            label = SharedRes.Strings.leave_duration,
            hint = SharedRes.Strings.select_leave_duration,
            listOfItems = LeaveDurationList.map { stringResource(it.title) },
            selectedValue = state.leaveDuration,
            onError = {
                onAction(LeaveRequestScreenAction.OnLeaveDurationError(it))
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
            listOfItems = LeaveTypeList.map { stringResource(it.title) },
            selectedValue = state.leaveType,
            onError = {
                onAction(LeaveRequestScreenAction.OnLeaveTypeError(it))
            },
            error = state.leaveTypeError,
            itemClicked = {
                onAction(LeaveRequestScreenAction.OnLeaveTypeChange(it))
            }
        )
//        leave reason
        EPRTextField(
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

