package com.gurkha.hr.attendance

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.attendance.model.AttendanceAction
import com.gurkha.hr.attendance.model.AttendanceItem
import com.gurkha.hr.attendance.model.AttendanceScreenState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.veryLightGray
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import kotlin.collections.chunked
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceScreen() {
    val viewModel: AttendanceViewModel = koinViewModel()
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
                        text = stringResource(SharedRes.Strings.attendance)
                    )
                }
            )
        }
    ) { contentPadding ->
        AttendanceContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding),
            state = state,
            onAction = viewModel::onAction
        )
    }
}

@Composable
fun AttendanceContent(
    modifier: Modifier = Modifier,
    state: AttendanceScreenState,
    onAction: (AttendanceAction)-> Unit
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
            modifier = Modifier,
            onAction = onAction
        )
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
                    .fillMaxWidth()
                    .padding(bottom = MaterialTheme.dimens.small3)
                ,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3)
            ) {
                rowItems.forEach { attendanceItem ->
                    AttendanceBox(
                        modifier = Modifier.weight(1f).fillMaxSize(),
                        item = attendanceItem
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
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.darkPrimaryTextColor
            )
        )

        Text(
            text = item.days.toString(),
            style = MaterialTheme.typography.titleLarge.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            )
        )
    }

}


fun LazyListScope.attendanceStatusTab(
    state: AttendanceScreenState,
    modifier: Modifier = Modifier,
    onAction: (AttendanceAction)-> Unit
){
    stickyHeader(key = "AttendanceStatus") {
        TabRow(
            modifier = modifier
                .fillMaxWidth(),
            selectedTabIndex = state.selectedTab.ordinal,
            divider = {},
            indicator = {},

        ){
            state.tabItemsList.forEach { item->
                val isSelected = state.selectedTab == item
                Tab(
                    modifier = Modifier
                        .clip(shape = MaterialTheme.shapes.small)
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.veryLightGray
                        ),
                    selected = isSelected,
                    onClick = {
                        onAction(
                            AttendanceAction.OnStatusChange(
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