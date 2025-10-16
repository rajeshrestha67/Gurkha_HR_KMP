package com.gurkha.hr.leave.leave

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData
import com.gurkha.hr.leave.model.leave.LeaveItem
import com.gurkha.hr.leave.model.leave.LeaveScreenAction
import com.gurkha.hr.leave.model.leave.LeaveScreenState
import com.gurkha.hr.leave.model.leave.LeaveStatusEnum
import com.gurkha.hr.leave.model.leave.tabItemsList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.highLightColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.veryLightGray
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
                        text = stringResource(SharedRes.Strings.all_leaves),
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.darkPrimaryTextColor
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
        LeaveScreenContent(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            state = state,
            onAction = viewModel::onAction,
            leaveListState = leaveListState
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
            top = MaterialTheme.dimens.small2,
            bottom = MaterialTheme.dimens.bottomBar
        ),
    ) {
//        show the 4 leave options
        leaveOptions(
            state = state
        )

//        show the tabs for the attendance status
        leaveStatusTab(
            selectedItem = state.leaveStatus,
            onAction = onAction
        )

//        show the result of the attendance
        leaveResults(state = state)
    }
}


fun LazyListScope.leaveOptions(itemsPerRow: Int = 2, state: LeaveScreenState) {
    state.leaveItemsList.chunked(itemsPerRow).forEach { rowItems ->
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small3),
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
                color = item.color,
                shape = MaterialTheme.shapes.medium
            )
            .clip(shape = MaterialTheme.shapes.medium)
            .background(item.backGroundColor)
            .heightIn(min = MaterialTheme.dimens.leaveBoxHeight)
            .clickable(onClick = {})
            .padding(MaterialTheme.dimens.small2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small1)
    ) {
        Text(
            text = stringResource(item.title),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.darkPrimaryTextColor
            )
        )
        Text(
            text = item.days.toString(), style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            )
        )
    }
}

fun LazyListScope.leaveStatusTab(
    selectedItem: LeaveStatusEnum = LeaveStatusEnum.PENDING,
    onAction: (LeaveScreenAction) -> Unit
) {
    stickyHeader(key = "leaveStatusTab") {
        SecondaryTabRow(
            selectedItem.ordinal,
            Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small3),
            TabRowDefaults.primaryContainerColor, TabRowDefaults.primaryContentColor, {},
            {}) {
            tabItemsList.forEach { item ->
                val isSelected = selectedItem == item
                Tab(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.small)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.veryLightGray
                        ),
                    selected = isSelected,
                    onClick = {
                        onAction(
                            LeaveScreenAction.OnStatusChange(
                                item
                            )
                        )
                    },
                    text = {
                        val color =
                            if (isSelected) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.primaryTextColor
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleSmall.copy(
                                color = color
                            )
                        )
                    }
                )
            }

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
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = MaterialTheme.dimens.small3),
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
                    Text(text = stringResource(SharedRes.Strings.no_data_found))
                }
            }
        }
    }


}


@Composable
fun LazyItemScope.ResultBox(
    item: LeaveReportData
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = MaterialTheme.dimens.small3,
                vertical = MaterialTheme.dimens.small1
            )
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.highLightColor)
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
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )
            Text(
                text = "From : ${item.startDate}   To : ${item.endDate}",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
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
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    text = item.totalDays.toString(),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            Column {
                Text(
                    text = stringResource(SharedRes.Strings.approver),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    text = item.assigneeName, style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            Column {
                Text(
                    text = stringResource(SharedRes.Strings.leaveType),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    text = item.leaveType,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
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
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )
            Text(
                text = item.reason,
                maxLines = 3,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
        }

    }
}

