package com.gurkha.hr.leave.leave

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.components.tabbar.ERPTabView
import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData
import com.gurkha.hr.leave.model.leave.LeaveItem
import com.gurkha.hr.leave.model.leave.LeaveScreenAction
import com.gurkha.hr.leave.model.leave.LeaveScreenState
import com.gurkha.hr.leave.model.leave.backgroundColor
import com.gurkha.hr.leave.model.leave.outlineColor
import com.gurkha.hr.res.SharedRes
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveScreen(
    navController: NavHostController,
    onGoToLeaveRequestPage: (String?) -> Unit,
) {
    val viewModel: LeaveScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val leaveListState = rememberLazyListState()

    val result = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("data", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(result?.value) {
        val json = result?.value
        if (!json.isNullOrBlank()) {
            viewModel.onAction(LeaveScreenAction.UpdateRequestData(json))
            delay(500)
            leaveListState.animateScrollToItem(state.currentTapItem.result.lastIndex + 1)
        }

    }
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(
                        text = stringResource(SharedRes.Strings.leave_request_form),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onGoToLeaveRequestPage(state.leaveRequestDataJson) },
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Go to Request page")
                }
            )
        },
    ) { contentPadding ->
        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            isRefreshing = state.isRefreshing,
            onRefresh = {
                viewModel.onAction(LeaveScreenAction.OnRefresh)
            },
            content = {
                LeaveScreenContent(
                    modifier = Modifier.fillMaxSize(),
                    state = state,
                    onAction = viewModel::onAction,
                    leaveListState = leaveListState
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveScreenContent(
    modifier: Modifier = Modifier,
    state: LeaveScreenState,
    onAction: (LeaveScreenAction) -> Unit,
    leaveListState: LazyListState
) {

    LazyColumn(
        modifier = modifier,
        state = leaveListState,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            vertical = MaterialTheme.dimens.small2,
            horizontal = MaterialTheme.dimens.small3,
        ),
    ) {
//        show the 4 leave options
        leaveOptions(
            state = state
        )

//        show the tabs for the attendance status
        leaveStatusTab(
            state = state,
            onAction = onAction
        )

//        show the result of the attendance
        leaveResults(state = state)
        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.bottomBar))
        }
    }
}


fun LazyListScope.leaveOptions(itemsPerRow: Int = 2, state: LeaveScreenState) {
    state.leaveItemsList.chunked(itemsPerRow).forEach { rowItems ->
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                if (state.isLeaveSummaryLoading) {
                    rowItems.forEach { leaveItem ->
                        ShimmerView(
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .weight(1f)
                                .height(MaterialTheme.dimens.heightForOptionBox)
                        )
                    }
                } else {
                    rowItems.forEach { leaveItem ->
                        LeaveBox(
                            modifier = Modifier.weight(1f).fillMaxSize(),
                            item = leaveItem
                        )
                    }
                }
                // Fill remaining spaces in row if needed
                repeat(itemsPerRow - rowItems.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}


@Composable
fun LeaveBox(
    modifier: Modifier = Modifier,
    item: LeaveItem
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .border(
                width = 1.dp,
                color = item.outlineColor,
                shape = MaterialTheme.shapes.medium
            )
            .clip(shape = MaterialTheme.shapes.medium)
            .background(item.backgroundColor)
            .heightIn(min = MaterialTheme.dimens.leaveBoxHeight)
            .clickable(onClick = {})
            .padding(MaterialTheme.dimens.small2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.erpColors.darkPrimaryTextColor
            )
        )
        Text(
            text = item.days.toString(), style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.erpColors.primaryTextColor
            )
        )
    }
}

fun LazyListScope.leaveStatusTab(
    state: LeaveScreenState,
    onAction: (LeaveScreenAction) -> Unit
) {
    stickyHeader(key = "leaveStatusTab") {
        ERPTabView(
            items = state.tabItemsList,
            selectedTab = state.leaveStatus,
            shape = MaterialTheme.shapes.medium,
            onItemSelected = { item ->
                onAction(
                    LeaveScreenAction.OnStatusChange(
                        item
                    )
                )
            }
        ) { item, isSelected ->
            val color =
                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = color
                )
            )
        }
    }
}


fun LazyListScope.leaveResults(
    state: LeaveScreenState
) {
    when {
        state.pendingTapItem.isLoading || state.approvedTapItem.isLoading || state.rejectedTapItem.isLoading ->
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2)
                ) {
                    repeat(4) {
                        ShimmerView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(MaterialTheme.dimens.bottomBar)
                                .clip(MaterialTheme.shapes.small)
                        )
                    }
                }
            }

        else -> {
            if (state.currentTapItem.result.isNotEmpty()) {
                items(state.currentTapItem.result) { item ->
                    ResultBox(item = item)
                }
            } else {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(SharedRes.Strings.no_data_found))
                    }
                }
            }
        }
    }


}


@Composable
fun LazyItemScope.ResultBox(
    item: LeaveReportData
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        tonalElevation = 4.dp,

        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.dimens.small1
                )
                .clip(MaterialTheme.shapes.medium)
                .padding(MaterialTheme.dimens.small2)
                .animateItem(
                    tween(300),
                    tween(500)
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.small2)
            )
            {
                Text(
                    text = stringResource(SharedRes.Strings.date),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.darkPrimaryTextColor
                    )
                )
                Text(
                    text = "From : ${item.startDate}   To : ${item.endDate}",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )
            }

            HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.small2),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            )
            {
                Column {
                    Text(
                        text = stringResource(SharedRes.Strings.applyDays),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.totalDays.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.primaryTextColor
                        )
                    )
                }

                Column(
                    modifier = Modifier.padding(horizontal = MaterialTheme.dimens.small1),
                ) {
                    Text(
                        text = stringResource(SharedRes.Strings.approver),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.assigneeName, style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.primaryTextColor
                        )
                    )
                }

                Column {
                    Text(
                        text = stringResource(SharedRes.Strings.leaveType),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.leaveType,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.primaryTextColor
                        )
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.small2)
            )
            {
                Text(
                    text = stringResource(SharedRes.Strings.reason),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.darkPrimaryTextColor
                    )
                )
                Text(
                    text = item.reason,
                    maxLines = 3,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )
            }

        }
    }
}

