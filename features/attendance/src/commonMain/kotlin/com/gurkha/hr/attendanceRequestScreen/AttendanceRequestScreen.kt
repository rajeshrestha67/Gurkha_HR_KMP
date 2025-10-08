package com.gurkha.hr.attendanceRequestScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.textField.ERPDateTextField
import com.gurkha.hr.components.textField.ERPTimeTestField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.FutureAndTodayDate
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreen() {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(text = "Attendance Request Screen")
                }
            )
        }
    ) { contentPadding ->
        AttendanceRequestScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceRequestScreenContent(
    modifier: Modifier = Modifier
) {
    val viewModel : AttendanceRequestViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var selectedTime by remember { mutableStateOf("--:--") }
    Column(
        modifier = modifier.fillMaxWidth()
            .padding(
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.bottomBar,
                end = MaterialTheme.dimens.small3,
                start = MaterialTheme.dimens.small3
            )
    ) {
        ERPDateTextField(
            modifier = Modifier
                .fillMaxWidth(),
            rules = FormValidate.requiredValidationRules,
            value = state.date,
            label = stringResource(SharedRes.Strings.date),
            hint = stringResource(SharedRes.Strings.date),
            error = state.dateError,
            onErrorStateChange = {
            },
            selectableDates = FutureAndTodayDate,
            onDateSelected = {
//                onAction(LeaveRequestScreenAction.OnStartDateChange(it))
            }
        )

        ERPTimeTestField(
            modifier = Modifier.fillMaxWidth(),
            value = selectedTime,
            label = "Select Time",
            hint = "hint",
            enabled = true,
//                rules = null,
            error = null,
            onErrorStateChange = {
            },
            onTimeSelected = {
                selectedTime = it
            }
        )
    }
}