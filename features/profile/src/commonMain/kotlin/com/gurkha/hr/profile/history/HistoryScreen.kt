package com.gurkha.hr.profile.history


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.ERPButton
import com.gurkha.hr.components.date.ERPDateTextField
import com.gurkha.hr.components.date.FutureAndTodayDate
import com.gurkha.hr.components.textField.DropDownText
import com.gurkha.hr.components.textField.FormValidate
import com.gurkha.hr.date.BSPointer
import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.hr.profile.model.history_screen.HistoryScreenViewAction
import com.gurkha.hr.profile.model.history_screen.HistoryState
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.highLightColor
import com.gurkha.hr.res.theme.lightGreenColor
import com.gurkha.hr.res.theme.lightRedColor
import com.gurkha.hr.res.theme.primaryTextColor
import org.jetbrains.compose.resources.stringArrayResource
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
                            imageVector = Icons.Default.FilterAlt,
                            contentDescription = "Filter Option"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        HistoryScreenContainer(
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
fun HistoryScreenContainer(
    state : HistoryState ,
    modifier: Modifier = Modifier,
    showFilter: Boolean,
    onCloseFilter: () -> Unit,
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
            vertical = MaterialTheme.dimens.small2),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small2, alignment = Alignment.Top)
    ) {
        if (showFilter) {
            item {
                DateFilterHistory(
                    onClose = onCloseFilter,
                    state = state
                )
            }
        }
        items( items = state.historySummaryList, key = {it.toString()}, itemContent = { item ->
            HistoryScreenContent(
                item = item,
                state = state
            )

        })

    }
}

@Composable
fun HistoryScreenContent(
    item : HistoryData,
    state: HistoryState
){

    Column (
        modifier = Modifier
            .fillMaxWidth()
        .clip(MaterialTheme.shapes.medium)
        .background(MaterialTheme.colorScheme.highLightColor)
        .padding(MaterialTheme.dimens.small2)
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.dimens.small2)
        ) {
            Text(
                stringResource(SharedRes.Strings.date),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor)
            )

            Text(
                text = "${item.date}(${item.day})",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
        }
        HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.dimens.small2))
        {
            RowInfoText(
                name = stringResource(SharedRes.Strings.clock_in_time),
                titleTextColor = MaterialTheme.colorScheme.darkPrimaryTextColor,
                value =stringResource(SharedRes.Strings.clock_out_time),
            )
            RowInfoText(
                name = item.clockInTime,
                subTitleTextColor = MaterialTheme.colorScheme.primaryTextColor,
                value = item.clockOutTime,
            )
            if (item.lateInTime.isNotBlank() && item.earlyOutTime.isNotBlank() ){
                RowInfoText(
                    name = "Late: ${item.lateInTime} min",
                    titleTextColor = MaterialTheme.colorScheme.lightRedColor,
                    subTitleTextColor = MaterialTheme.colorScheme.lightGreenColor,
                    value = "Early: ${item.earlyOutTime} min"
                )
            }

        }
        HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))

        Column(
            modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = MaterialTheme.dimens.small2))
        {
            Text(
                text = stringResource(SharedRes.Strings.leaveRequest),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )
            RowInfoText(
                name = "Assigned",
                value = item.assigneeName
            )
            RowInfoText(
                name = "Remarks",
                value = item.leaveApproverRemarks
            )
            RowInfoText(
                name = "Response",
                value = item.response
            )
            RowInfoText(
                name = "Status",
                value = item.leaveRequestStatus
            )
            RowInfoText(
                name = "Leave Duration",
                value = item.leaveDuration
            )


        }
        HorizontalDivider(modifier = Modifier.height(MaterialTheme.dimens.extraSmall))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = MaterialTheme.dimens.small2)
        ) {
            Text(
                text = stringResource(SharedRes.Strings.attendanceRequest),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.darkPrimaryTextColor
                )
            )
            if(item.assigneeName.isNotBlank()){
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
    name : String,
    value: String,
    titleTextColor: Color = MaterialTheme.colorScheme.primaryTextColor,
    subTitleTextColor: Color = MaterialTheme.colorScheme.darkPrimaryTextColor,
){

    Row (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Start
    ){
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
    onClose: () -> Unit,
) {


    val yearInBS = remember { (2070..2085).map { it.toString() } }
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
            DropDownText(
                dropdownIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calender Image",

                    )
                },
                label = SharedRes.Strings.month,
                hint = SharedRes.Strings.month,
                selectedValue = "",
                error = state.endMonthError,
                onError = {

                },
                listOfItems = stringArrayResource(SharedRes.Arrays.months),
                rules = FormValidate.requiredValidationRules,
                itemClicked = {

                },
            )

            DropDownText(
                dropdownIcon = {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Calender Image",

                        )
                },
                label = SharedRes.Strings.month,
                hint = SharedRes.Strings.month,
                selectedValue = "",
                error = state.endYearError,
                onError = {

                },
                listOfItems = yearInBS,
                rules = FormValidate.requiredValidationRules,
                itemClicked = {}
            )

            ERPButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(SharedRes.Strings.submit),
            )
        }
    }
}
