package com.gurkha.hr.profile.time_and_attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.textField.ERPDateTextField
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.components.textField.FutureAndTodayDate
import com.gurkha.hr.domain.timeAndAttendance.model.TimeAndAttendanceData
import com.gurkha.hr.profile.model.time_and_attendance_screen.TimeAndAttendanceState
import com.gurkha.hr.profile.model.time_and_attendance_screen.TimeAndAttendanceViewAction
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.highLightColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimeAndAttendanceScreen(
    onBackPressed: () -> Unit,

    ) {
    val viewModel: TimeAndAttendanceViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showFilter by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.time_and_attendance)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            showFilter = !showFilter
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Filter Option"
                        )
                    }
                }

            )
        }
    ) { paddingValues ->
        TimeAndAttendanceScreenContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state,
            showFilter = showFilter,
            onCloseFilter = { showFilter = false },
            onAction = viewModel::onAction

        )


    }
}

@Composable
fun TimeAndAttendanceScreenContainer(
    state: TimeAndAttendanceState,
    showFilter: Boolean,
    onCloseFilter: () -> Unit = {},
    modifier: Modifier = Modifier,
    onAction: (TimeAndAttendanceViewAction) -> Unit
) {

    val listState = rememberLazyListState()
    LaunchedEffect(showFilter) {
        if (showFilter) {
            listState.scrollToItem(0)
        }
    }
    LazyColumn(
        modifier = modifier,
        state = listState,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        )
    ) {
        if (showFilter) {
            item {
                DateFilter(
                    state = state,
                    onClose = onCloseFilter,
                    onAction = onAction
                )
            }
        }
        items(state.timeAndAttendanceList, key = { it.toString() }, itemContent = { item ->
            TimeAndAttendanceDetails(
                onAction = onAction,
                state = state,
                item = item
            )


        })

    }

}

@Composable
fun TimeAndAttendanceDetails(
    item: TimeAndAttendanceData,
    onAction: (TimeAndAttendanceViewAction) -> Unit,
    state: TimeAndAttendanceState,

    ) {

    var showMore by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = MaterialTheme.dimens.small2,
                end = 0.dp,
                top = MaterialTheme.dimens.small1,
                bottom = MaterialTheme.dimens.small1,
            )
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.highLightColor)
            .padding(
                start = MaterialTheme.dimens.small2,
                top = MaterialTheme.dimens.small2,
                bottom = MaterialTheme.dimens.small2,
                end = 0.dp,
            ),

        ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = stringResource(SharedRes.Strings.date),
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
                Text(
                    text = "${item.date} (${
                        item.day.lowercase().replaceFirstChar { it.uppercase() }
                    })",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = MaterialTheme.colorScheme.secondaryTextColor
                    )
                )
            }

            Box {
                if (showMore) {
                    DropdownMenu(
                        containerColor = MaterialTheme.colorScheme.background,
                        expanded = showMore,
                        onDismissRequest = {
                            showMore = false
                        }
                    ) {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(SharedRes.Strings.clockIn),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.primaryTextColor
                                    )
                                )
                            },
                            onClick = {

                            }
                        )
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = stringResource(SharedRes.Strings.clockOut),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.primaryTextColor
                                    )
                                )
                            },
                            onClick = {

                            }
                        )
                    }
                }
                IconButton(
                    onClick = {
                        showMore = true
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More Option"
                    )
                }
            }

        }


        HorizontalDivider(
            modifier = Modifier.height(MaterialTheme.dimens.extraSmall)
                .padding(end = MaterialTheme.dimens.small2)
        )

        Row(
            modifier = Modifier.fillMaxWidth().padding(end = MaterialTheme.dimens.small2),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            RowText(
                name = SharedRes.Strings.clockIn,
                value = item.clockInTime
            )
            RowText(
                name = SharedRes.Strings.clockOut,
                value = item.clockOutTime
            )
            RowText(
                name = SharedRes.Strings.status,
                value = item.status
            )
        }
    }


}

@Composable
fun RowScope.RowText(
    name: StringResource,
    value: String

) {
    Column(
        modifier = Modifier.padding(top = MaterialTheme.dimens.small1).weight(1f),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(name),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor,
                fontWeight = FontWeight.SemiBold
            ),
            textAlign = TextAlign.Start
        )
        Text(
            text = value,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.bodySmall.copy(
                color = MaterialTheme.colorScheme.secondaryTextColor
            ), textAlign = TextAlign.Start

        )
    }
}

@Composable
fun DateFilter(
    state: TimeAndAttendanceState,
    onClose: () -> Unit,
    onAction: (TimeAndAttendanceViewAction) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                bottom = MaterialTheme.dimens.small3,
                start = MaterialTheme.dimens.small2,
                end = MaterialTheme.dimens.small2
            )

    ) {
        IconButton(
            onClick = onClose,
            modifier = Modifier
                .align(Alignment.TopEnd)

        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Filter",
                tint = MaterialTheme.colorScheme.primaryTextColor
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = MaterialTheme.dimens.small3),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            ERPDateTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.fromDate,
                label = stringResource(SharedRes.Strings.fromDate),
                hint = "From Date",
                rules = FormValidate.requiredValidationRules,
                error = state.fromDateError,
                selectableDates = FutureAndTodayDate,
                onErrorStateChange = {},
                onDateSelected = {
                    onAction(TimeAndAttendanceViewAction.fromDate(it))
                }
            )
            ERPDateTextField(
                modifier = Modifier.fillMaxWidth(),
                value = state.toDate,
                label = stringResource(SharedRes.Strings.toDate),
                hint = "To Date",
                rules = FormValidate.requiredValidationRules,
                error = state.toDateError,
                selectableDates = FutureAndTodayDate,
                onErrorStateChange = {},
                onDateSelected = {
                    onAction(TimeAndAttendanceViewAction.toDate(it))
                }
            )

            ERPButton(
                onClick = {
                    onAction(TimeAndAttendanceViewAction.Submit)
//                    if (state.fromDateError == null &&
//                        state.toDateError == null &&
//                        state.fromDate?.displayValue?.isNotEmpty() == true &&
//                        state.toDate?.displayValue?.isNotEmpty() == true
//                    ) {
//                        onClose()
//                    }

                },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.Strings.submit),

                )

        }
    }


}