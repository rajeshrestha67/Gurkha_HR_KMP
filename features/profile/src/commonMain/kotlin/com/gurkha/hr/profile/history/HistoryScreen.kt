package com.gurkha.hr.profile.history


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.dateFilterDropDown.DateFilterDropdown
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.profile.model.history_screen.HistoryDataUI
import com.gurkha.hr.profile.model.history_screen.HistoryScreenViewAction
import com.gurkha.hr.profile.model.history_screen.HistoryState
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    onBackPressed: () -> Unit,
) {
    val viewModel: HistoryViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showFilter by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.history)) },
                navigationIcon = {

                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {

                    IconButton(onClick = { showFilter = !showFilter }) {

                        Icon(
                            imageVector = if (!showFilter) Icons.Default.FilterAlt else Icons.Default.Close,
                            contentDescription = "Filter Option"
                        )


                    }
                }
            )
        }
    ) { paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = state.isRefreshing,
            onRefresh = { viewModel.onAction(HistoryScreenViewAction.OnRefresh) },
            content = {
                HistoryScreenContainer(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = state,
                    showFilter = showFilter,
                    onAction = viewModel::onAction
                )
            }
        )
    }
}

@Composable
fun HistoryScreenContainer(
    state: HistoryState,
    modifier: Modifier = Modifier,
    showFilter: Boolean,
    onAction: (HistoryScreenViewAction) -> Unit
) {
    val listState = rememberLazyListState()
    LaunchedEffect(showFilter) {
        if (showFilter) {
            listState.scrollToItem(0)
        }
    }
    LazyColumn(
        state = listState,
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        ),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.small2,
            alignment = Alignment.Top
        )
    ) {
        if (showFilter) {
            item {
                DateFilterHistory(
                    state = state,
                    onAction = onAction
                )
            }
        }
        if (state.isLoading) {
            items(10) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(
                        MaterialTheme.dimens.small2,
                        alignment = Alignment.CenterVertically
                    ),
                ) {
                    ShimmerView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(MaterialTheme.dimens.extraLarge)
                            .clip(MaterialTheme.shapes.small)
                    )
                }
            }
        } else {

            items(items = state.historySummaryList, key = { it.toString() }, itemContent = { item ->
                HistoryScreenContent(
                    item = item,
                    state = state
                )

            })
        }

    }
}

@Composable
fun HistoryScreenContent(
    item: HistoryDataUI,
    state: HistoryState
) {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium),
        tonalElevation = 4.dp
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.dimens.small2)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween

            )
            {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = MaterialTheme.dimens.small2)

                ) {
                    Text(
                        stringResource(SharedRes.Strings.date),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )

                    Text(
                        text = "${item.date} (${item.day})",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.primaryTextColor,
                        )
                    )
                }
                Text(
                    text = item.attendanceStatus,
                    style = MaterialTheme.typography.titleMedium.copy(
                        color = item.attendanceTextColor.textColor
                    )
                )
            }
            if (item.isPresent) {
                HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))
                LowTextContent(item = item)
            }

        }
    }
}

@Composable
fun LowTextContent(
    item: HistoryDataUI
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small2)
    )
    {
        RowInfoText(
            name = stringResource(SharedRes.Strings.clock_in_time),
            titleTextColor = MaterialTheme.erpColors.darkPrimaryTextColor,
            value = stringResource(SharedRes.Strings.clock_out_time),
        )
        RowInfoText(
            name = item.clockInTime,
            subTitleTextColor = MaterialTheme.erpColors.primaryTextColor,
            value = item.clockOutTime,
        )
        if (item.lateInTime.isNotBlank() && item.earlyOutTime.isNotBlank()) {
            RowInfoText(
                name = "Late: ${item.lateInTime} min",
                titleTextColor = MaterialTheme.erpColors.lightRedColor,
                subTitleTextColor = MaterialTheme.erpColors.lightGreenColor,
                value = "Early: ${item.earlyOutTime} min"
            )
        }

    }
    if (item.leaveDuration.isNotBlank()) {
        HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

        Text(
            modifier = Modifier.padding(top = MaterialTheme.dimens.small2),
            text = stringResource(SharedRes.Strings.leaveRequest),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.erpColors.darkPrimaryTextColor
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.dimens.small2)
        )
        {

            RowInfoText(
                name = stringResource(SharedRes.Strings.assigned),
                value = item.assigneeName
            )
            RowInfoText(
                name = stringResource(SharedRes.Strings.remarks),
                value = item.leaveApproverRemarks
            )
            RowInfoText(
                name = stringResource(SharedRes.Strings.response),
                value = item.response
            )
            RowInfoText(
                name = stringResource(SharedRes.Strings.status),
                value = item.leaveRequestStatus
            )
            RowInfoText(
                name = stringResource(SharedRes.Strings.leave_duration),
                value = item.leaveDuration
            )


        }
    }

    if (item.assigneeName.isNotBlank()) {
        HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))
        Text(
            modifier = Modifier.padding(top = MaterialTheme.dimens.small2),
            text = stringResource(SharedRes.Strings.attendanceRequest),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.erpColors.darkPrimaryTextColor
            )
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.dimens.small2)
        ) {

            if (item.assigneeName.isNotBlank()) {
                RowInfoText(
                    name = "Assigned",
                    value = item.assigneeName
                )
                RowInfoText(
                    name = "Remarks",
                    value = item.remarks
                )
                RowInfoText(
                    name = "Response",
                    value = item.response
                )
                RowInfoText(
                    name = "Status",
                    value = item.assigneeStatus
                )
            }


        }
    }

}

@Composable
fun RowInfoText(
    name: String,
    value: String,
    titleTextColor: Color = MaterialTheme.erpColors.primaryTextColor,
    subTitleTextColor: Color = MaterialTheme.erpColors.darkPrimaryTextColor,
) {

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ) {
        Text(
            modifier = Modifier.weight(1f),
            text = name,
            style = MaterialTheme.typography.titleSmall.copy(
                color = titleTextColor
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis

        )
        Text(
            modifier = Modifier.weight(1f),
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                color = subTitleTextColor
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun DateFilterHistory(
    state: HistoryState,
    onAction: (HistoryScreenViewAction) -> Unit
) {
    DateFilterDropdown(
        modifier = Modifier.fillMaxWidth()
            .padding(
                bottom = MaterialTheme.dimens.small3,
                start = MaterialTheme.dimens.small1,
                end = MaterialTheme.dimens.small1
            ),
        selectedMonth = state.monthDisplay,
        selectedYear = state.year?.let {
            state.year.toString()
        } ?: "",
        monthError = state.endMonthError,
        yearError = state.endYearError,
        onMonthSelected = { monthName, monthIndex ->
            onAction(HistoryScreenViewAction.FromMonth(showMonth = monthName, month = monthIndex))
        },
        onYearSelected = { year ->
            onAction(HistoryScreenViewAction.FromYear(year))
        },
        onMonthError = { message ->
            onAction(HistoryScreenViewAction.MonthPickerError(message))
        },
        onYearError = { message ->
            onAction(HistoryScreenViewAction.YearPickerError(message))
        },
        onSubmit = {
            onAction(HistoryScreenViewAction.Submit(employeeId = state.employeeId))
        }
    )
}
