package com.gurkha.hr.profile.allocated_leave

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
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.profile.model.allocated_leave_Screen.AllocatedLeaveState
import com.gurkha.hr.profile.model.allocated_leave_Screen.LeaveTypeData

import com.gurkha.hr.profile.model.document_screen.DocumentList
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.highLightColor
import com.gurkha.hr.res.theme.lightRedColor
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.secondaryTextColor
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
        AllocatedLeaveScreenContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            state = state
        )


    }

}

@Composable
fun AllocatedLeaveScreenContainer(
    state : AllocatedLeaveState,
    modifier: Modifier = Modifier,

) {

    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2),
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3
        )
    ) {
        stickyHeader (key = "summary leave header") {
            HeaderSection(
                text = SharedRes.Strings.leaveSummary
            )
        }

        items(
            state.leaveSummaryList, key = {it.toString()}, itemContent = { item ->
                LeaveTypeBox(
                    title = item.leaveType,
                    totalDays = item.totalDays.toInt(),
                    leaveTaken = item.leaveTaken.toInt(),
                    remainingLeave = item.remainingLeave.toInt()

                )
            },
            )
    }

}




@Composable
fun HeaderSection(text: StringResource) {

    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                MaterialTheme.dimens.small2
            ),
        text = stringResource(text),
        style = MaterialTheme.typography.titleLarge

    )
}

@Composable
fun LeaveTypeBox(
    title: String,
    totalDays: Int,
    leaveTaken: Int,
    remainingLeave: Int
){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.highLightColor),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2, alignment = Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        // to avoid parent vertical alignment padding
        Column(
            modifier = Modifier.fillMaxWidth().padding(MaterialTheme.dimens.small1)
                .align(Alignment.CenterHorizontally)
        ) {
            Text(
                modifier = Modifier.padding(MaterialTheme.dimens.small1),
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )
            HorizontalDivider(
                modifier = Modifier.height(MaterialTheme.dimens.extraSmall)
            )
        }

        LeaveInfoRow(
            name = SharedRes.Strings.totalDays,
            value = totalDays.toString()
        )
        LeaveInfoRow(
            name = SharedRes.Strings.leaveTaken,
            value = leaveTaken.toString()
        )
        LeaveInfoRow(
            name = SharedRes.Strings.remainingLeave,
            value = remainingLeave.toString()

        )
    }
}

@Composable
private fun LeaveInfoRow(
    name: StringResource,
    value: String
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding( MaterialTheme.dimens.small1),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.align(Alignment.CenterVertically),
            text = stringResource(name),
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.secondaryTextColor
            )
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
        ))
    }
}
