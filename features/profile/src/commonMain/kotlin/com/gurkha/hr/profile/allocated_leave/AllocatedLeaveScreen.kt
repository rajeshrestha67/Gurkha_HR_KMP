package com.gurkha.hr.profile.allocated_leave

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.erpColors
import com.gurkha.hr.profile.model.allocated_leave_Screen.AllocatedLeaveState
import com.gurkha.hr.profile.model.allocated_leave_Screen.AllocatedLeaveViewAction
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllocatedLeaveScreen(
    onBackPressed: () -> Unit,
) {
    val viewModel: AllocatedLeaveScreenViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),

        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.allocated_leave)) },
                navigationIcon = {
                    IconButton(
                        onClick = onBackPressed
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
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
            onRefresh = {
                viewModel.onAction(AllocatedLeaveViewAction.OnRefresh)
            },
            content = {
                AllocatedLeaveScreenContainer(
                    modifier = Modifier
                        .fillMaxSize(),
                    state = state
                )
            }
        )
    }

}

@Composable
fun AllocatedLeaveScreenContainer(
    state: AllocatedLeaveState,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
        )
    ) {
        items(state.leaveSummaryList) { item ->
            LeaveTypeBox(
                title = item.leaveType,
                totalDays = item.totalDays.toInt(),
                leaveTaken = item.leaveTaken.toInt(),
                remainingLeave = item.remainingLeave.toInt()
            )
        }
    }

}


@Composable
fun LeaveTypeBox(
    title: String,
    totalDays: Int,
    leaveTaken: Int,
    remainingLeave: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.erpColors.highLightColor),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.dimens.small2,
            alignment = Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(
                color = MaterialTheme.colorScheme.primary
            )
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
        ) {
            LeaveInfoRow(
                modifier = Modifier.weight(1f),
                name = SharedRes.Strings.totalDays,
                value = totalDays.toString()
            )
            LeaveInfoRow(
                modifier = Modifier.weight(1f),
                name = SharedRes.Strings.leaveTaken,
                value = leaveTaken.toString()
            )
            LeaveInfoRow(
                modifier = Modifier.weight(1f),
                name = SharedRes.Strings.remainingLeave,
                value = remainingLeave.toString()
            )
        }
    }
}

@Composable
private fun LeaveInfoRow(
    name: StringResource,
    value: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.small),
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = modifier
                .padding(MaterialTheme.dimens.small1),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(vertical = MaterialTheme.dimens.small1),
                textAlign = TextAlign.Center,
                text = stringResource(name),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.erpColors.secondaryTextColor
                )
            )
            Box(
                modifier = Modifier.weight(1f),
            )
            Text(
                textAlign = TextAlign.Center,
                text = value,
                style = MaterialTheme.typography.titleLarge.copy(
                    color = MaterialTheme.erpColors.primaryTextColor
                )
            )
            Box(
                modifier = Modifier.weight(1f),
            )
        }
    }
}
