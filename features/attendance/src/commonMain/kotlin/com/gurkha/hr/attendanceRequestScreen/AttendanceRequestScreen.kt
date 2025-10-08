package com.gurkha.hr.attendanceRequestScreen

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestAction
import com.gurkha.hr.model.attendanceRequestScreen.AttendanceRequestState
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.ERPDateTextField
import com.gurkha.hr.components.textField.ERPTextField
import com.gurkha.hr.components.textField.ERPTimeTestField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.FutureAndTodayDate
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreen(
    navController: NavHostController,
    json: String?,
    onBackClicked: () -> Unit
) {
    val viewModel: AttendanceRequestViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.dataChannel.collect { data ->
            data?.let {
                val stringData = Json.encodeToString(data)
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.set("data", stringData)
                navController.popBackStack()
            }
        }
    }

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
            state = state
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreenContent(
    modifier: Modifier = Modifier,
    onBackClicked: () -> Unit,
    onAction: (AttendanceRequestAction) -> Unit,
    state: AttendanceRequestState
) {
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

//        clock in time
        ERPTimeTestField(
            modifier = Modifier.fillMaxWidth(),
            rules = FormValidate.requiredValidationRules,
            value = state.clockInTime,
            label = stringResource(SharedRes.Strings.clock_in_time),
            hint = stringResource(SharedRes.Strings.clock_in_time),
            enabled = true,
            error = state.clockInTimeError,
            onErrorStateChange = {
            },
            onTimeSelected = {
                onAction(AttendanceRequestAction.OnClockInTimeChange(it))
            }
        )

//        clock out time
        ERPTimeTestField(
            modifier = Modifier.fillMaxWidth(),
            value = state.clockOutTime,
            label = stringResource(SharedRes.Strings.clock_out_time),
            hint = stringResource(SharedRes.Strings.clock_out_time),
            enabled = true,
            rules = FormValidate.requiredValidationRules,
            error = state.clockOutTimeError,
            onErrorStateChange = {
            },
            onTimeSelected = {
                onAction(AttendanceRequestAction.OnClockOutTimeChange(it))
            }
        )

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
            onImeAction = {
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
    }

}