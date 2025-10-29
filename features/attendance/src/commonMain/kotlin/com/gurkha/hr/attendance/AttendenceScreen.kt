package com.gurkha.hr.attendance

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
import androidx.compose.foundation.lazy.items
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
import com.gurkha.hr.domain.attendance.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.model.attendanceScreen.AttendanceAction
import com.gurkha.hr.model.attendanceScreen.AttendanceItem
import com.gurkha.hr.model.attendanceScreen.AttendanceScreenState
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen(
    navController: NavHostController,
    onGoToAttendanceRequestScreen: () -> Unit
) {
    val viewModel: AttendanceViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val result = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<String?>("data", null)
        ?.collectAsStateWithLifecycle()

    LaunchedEffect(result?.value) {
        val json = result?.value
        if (!json.isNullOrBlank()) {
            viewModel.onAction(AttendanceAction.OnUpdateAttendanceJsonData(json))
        }
    }

    AttendanceScreenMain(
        onGoToAttendanceRequestScreen = onGoToAttendanceRequestScreen,
        state = state,
        onAction = viewModel::onAction
    )


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreenMain(
    onGoToAttendanceRequestScreen: () -> Unit,
    state: AttendanceScreenState,
    onAction: (AttendanceAction) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0.dp),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = {
                    Text(
                        text = stringResource(SharedRes.Strings.attendance)
                    )
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onGoToAttendanceRequestScreen
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Go to attendance Request Screen")
            }
        },
    ) { contentPadding ->
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            isRefreshing = state.isRefreshing,
            onRefresh = {
                onAction(AttendanceAction.OnRefresh)
            },
            content = {
                AttendanceContent(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = state,
                    onAction = onAction
                )
            }
        )
    }
}

@Composable
fun AttendanceContent(
    modifier: Modifier = Modifier,
    state: AttendanceScreenState,
    onAction: (AttendanceAction) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(
            top = MaterialTheme.dimens.small2,
            bottom = MaterialTheme.dimens.bottomBar,
            start = MaterialTheme.dimens.small3,
            end = MaterialTheme.dimens.small3
        ),
    ) {
//        show 4 diff options for the attendance
        showAttendanceOptions(
            state = state,
            itemsPerRow = 2
        )

//        show the tab
        attendanceStatusTab(
            state = state,
            onAction = onAction
        )

//        show the attendance result
        attendanceResult(
            state = state
        )

        item {
            Spacer(modifier = Modifier.height(MaterialTheme.dimens.bottomBar))
        }
    }
}


fun LazyListScope.showAttendanceOptions(
    itemsPerRow: Int,
    state: AttendanceScreenState
) {
    state.attendanceGridOptions.chunked(itemsPerRow).forEach { rowItems ->
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                if (state.isFetchingAttendanceSummary) {
                    rowItems.forEach { attendanceItem ->
                        ShimmerView(
                            modifier = Modifier
                                .clip(shape = MaterialTheme.shapes.small)
                                .weight(1f).height(MaterialTheme.dimens.heightForOptionBox)
                        )
                    }
                } else {
                    rowItems.forEach { attendanceItem ->
                        AttendanceBox(
                            modifier = Modifier.weight(1f).fillMaxSize(),
                            item = attendanceItem
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
fun AttendanceBox(
    modifier: Modifier = Modifier,
    item: AttendanceItem
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
                color = MaterialTheme.erpColors.darkPrimaryTextColor
            )
        )

        Text(
            text = item.days.toString(),
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.erpColors.primaryTextColor
            )
        )
    }

}


fun LazyListScope.attendanceStatusTab(
    state: AttendanceScreenState,
    onAction: (AttendanceAction) -> Unit
) {
    stickyHeader(key = "AttendanceStatus") {
        ERPTabView(
            items = state.tabItemsList,
            selectedTab = state.selectedTab,
            shape = MaterialTheme.shapes.medium,
            onItemSelected = { item ->
                onAction(
                    AttendanceAction.OnStatusChange(
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


fun LazyListScope.attendanceResult(
    state: AttendanceScreenState
) {
    when {
        state.pendingTapItem.isLoading || state.approvedTapItem.isLoading || state.rejectedTapItem.isLoading -> {
            items(4) {
                ShimmerView(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(MaterialTheme.dimens.bottomBar)
                )
            }
        }

        else -> {
            if (state.currentTapItem.result.isNotEmpty()) {
                items(state.currentTapItem.result) { item ->
                    ResultBox(
                        item = item
                    )
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
    item: AttendanceStatusData
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
                .background(MaterialTheme.erpColors.highLightColor)
                .padding(MaterialTheme.dimens.small2)
                .animateItem(
                    tween(300),
                    tween(500)
                )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            )
            {
                Text(
                    text = stringResource(SharedRes.Strings.date),
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.darkPrimaryTextColor
                    )
                )
                Text(
                    text = item.requestedDate,
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
                        text = "Clock In",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.clockInTime,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.primaryTextColor
                        )
                    )
                }

                Column {
                    Text(
                        text = "Clock Out",
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.clockOutTime,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.primaryTextColor
                        )
                    )
                }

                Column {
                    Text(
                        text = stringResource(SharedRes.Strings.approver),
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = MaterialTheme.erpColors.darkPrimaryTextColor
                        )
                    )
                    Text(
                        text = item.assignedTo,
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
                    text = item.requestRemarks,
                    maxLines = 3,
                    style = MaterialTheme.typography.titleSmall.copy(
                        color = MaterialTheme.erpColors.primaryTextColor
                    )
                )
            }

        }
    }
}