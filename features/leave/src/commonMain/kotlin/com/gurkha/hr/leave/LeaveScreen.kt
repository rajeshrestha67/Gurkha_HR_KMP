package com.gurkha.hr.leave

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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.domain.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.leave.model.AttendanceStatusEnum
import com.gurkha.hr.leave.model.LeaveItem
import com.gurkha.hr.leave.model.LeaveScreenAction
import com.gurkha.hr.leave.model.LeaveScreenState
import com.gurkha.hr.leave.model.leaveItemsList
import com.gurkha.hr.leave.model.tabItemsList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.highLightColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.veryLightGray
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveScreen(
    onGoToLeaveRequestPage: () -> Unit,
) {
    val viewModel: LeaveScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


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
                })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoToLeaveRequestPage,
                content = {
                    Icon(Icons.Filled.Add, contentDescription = "Go to Request page")
                }
            )
        }
    ) { contentPadding ->
        LeaveScreenContent(
            modifier = Modifier.fillMaxSize().padding(contentPadding),
            state = state,
            onAction = viewModel::onAction,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeaveScreenContent(
    modifier: Modifier = Modifier,
    state: LeaveScreenState,
    onAction: (LeaveScreenAction) -> Unit
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            top = MaterialTheme.dimens.small2,
            bottom = MaterialTheme.dimens.bottomBar
        ),
    ) {
//        show the 4 leave options
        leaveOptions()

//        show the tabs for the attendance status
        leaveStatusTab(
            selectedItem = state.attendanceStatus,
            onAction = onAction
        )

//        show the result of the attendance
        attendanceResult(state = state)
    }
}


fun LazyListScope.leaveOptions(itemsPerRow: Int = 2) {
    leaveItemsList.chunked(itemsPerRow).forEach { rowItems ->
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.dimens.small3),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                rowItems.forEach { leaveItem ->
                    LeaveBox(
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        item = leaveItem
                    )
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
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.darkPrimaryTextColor
            )
        )
        Text(
            text = item.days, style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            )
        )
    }
}

fun LazyListScope.leaveStatusTab(
    selectedItem: AttendanceStatusEnum = AttendanceStatusEnum.PENDING,
    onAction: (LeaveScreenAction) -> Unit
) {
    stickyHeader(key = "leaveStatusTab") {
        TabRow(
            selectedTabIndex = selectedItem.ordinal,
            indicator = {},
            divider = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.dimens.small3),
        ) {
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


fun LazyListScope.attendanceResult(
    state: LeaveScreenState
) {
    when {
        state.pendingTapItem.isLoading || state.approvedTapItem.isLoading || state.cancelTapItem.isLoading ->
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
                    Text(text = "No Data Found!")
                }
            }
        }
    }


}


@Composable
fun ResultBox(
    item: AttendanceStatusData
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
            .padding(MaterialTheme.dimens.small2),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 50.dp)
        )
        {
            Text(
                text = "Date", style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )
            Text(
                text = item.requestedDate,
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
        }

        HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        )
        {
            Column {
                Text(
                    text = "Apply Days",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    text = "3 days",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            Column {
                Text(
                    text = "Leave Balance",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    text = "16", style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }

            Column {
                Text(
                    text = "Approved By",
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.darkPrimaryTextColor
                    )
                )
                Text(
                    text = item.assignedTo,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.colorScheme.primaryTextColor
                    )
                )
            }
        }

    }
}

