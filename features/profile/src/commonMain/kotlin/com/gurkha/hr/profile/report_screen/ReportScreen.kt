package com.gurkha.hr.profile.report_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gurkha.hr.components.dateFilterDropDown.DateFilterDropdown
import com.gurkha.hr.components.shimmer.ShimmerView
import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.hr.domain.reportScreen.model.ReportData
import com.gurkha.hr.profile.model.history_screen.HistoryDataUI
import com.gurkha.hr.profile.model.report_Screen.Heading
import com.gurkha.hr.profile.model.report_Screen.ReportItems
import com.gurkha.hr.profile.model.report_Screen.ReportScreenState
import com.gurkha.hr.profile.model.report_Screen.ReportScreenViewAction
import com.gurkha.hr.res.SharedRes
import com.gurkha.hr.res.theme.darkPrimaryTextColor
import com.gurkha.hr.res.theme.dimens
import com.gurkha.hr.res.theme.primaryTextColor
import com.gurkha.hr.res.theme.veryLightGray
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ReportScreen(
    onBackPressed: () -> Unit,
) {
    val viewModel: ReportViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    ReportScreenContainer(
        onBackPressed = onBackPressed,
        onAction = viewModel::onAction,
        state = state
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreenContainer(
    onBackPressed: () -> Unit,
    state: ReportScreenState,
    onAction: (ReportScreenViewAction) -> Unit
) {

    var showFilter by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                windowInsets = WindowInsets(0.dp),
                title = { Text(stringResource(SharedRes.Strings.report)) },
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
        ReportScreenContent(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            showFilter = showFilter,
            state = state,
            onAction = onAction
        )
    }
}

@Composable
fun ReportScreenContent(
    modifier: Modifier = Modifier,
    showFilter: Boolean,
    state: ReportScreenState,
    onAction: (ReportScreenViewAction) -> Unit
) {
    val listState = rememberLazyListState()
    val infoList = Heading.list.map { stringResource(it.title) }
    LaunchedEffect(showFilter) {
        if (showFilter) {
            listState.scrollToItem(0)
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Fixed(2),
        state = rememberLazyStaggeredGridState(),
        modifier = modifier,
        contentPadding = PaddingValues(
            horizontal = MaterialTheme.dimens.small3,
            vertical = MaterialTheme.dimens.small2
        ),
        verticalItemSpacing = MaterialTheme.dimens.small3,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.dimens.small3),
        flingBehavior = ScrollableDefaults.flingBehavior(),
        userScrollEnabled = true
    ) {
        if (showFilter) {
            item(
                span = StaggeredGridItemSpan.FullLine
            ) {
                DateFilterReport(
                    state = state,
                    onAction = onAction
                )
            }
        }
        item(
            span = StaggeredGridItemSpan.FullLine
        ) {

            ReportTabRow(
                selectedIndex = state.selectedTab,
                items = infoList,
                onTabSelected = { index ->
                    onAction(ReportScreenViewAction.OnItemSelected(index))

                }
            )

        }

        when (state.selectedTab) {
            0 -> {
                if (state.isLoading){
                    items(count = 12){
                        val differentHeight = remember{(50..180).random()}
                        ShimmerView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(differentHeight.dp)
                                .clip(MaterialTheme.shapes.small)
                        )
                    }
                }else{
                    items(items = state.historySummaryList, key = {it.toString()}, itemContent = {item ->
                        MonthlyAttendanceItemsBox(
                            item = item
                        )
                    })
                }


            }

            1 -> {

                items(state.reportListItems){item ->
                    InfoAttendanceItemsBox(
                        name = item.title,
                        value = item.days
                    )
                }
            }
        }
    }
}
@Composable
fun MonthlyAttendanceItemsBox(
    item : HistoryDataUI
){
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.dimens.small2,
                    horizontal = MaterialTheme.dimens.small1,
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Text(
                text = "${item.date} (${item.day})",
                style = MaterialTheme.typography.titleSmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = MaterialTheme.dimens.small2),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = item.attendanceStatus,
                    style = MaterialTheme.typography.titleLarge.copy(
                        color = item.attendanceTextColor.textColor

                    )
                )
                if (item.isPresent){
                    InOutText(
                        inOrOut = "In: ",
                        inOut = item.clockInTime
                    )
                    InOutText(
                        inOrOut = "Out: ",
                        inOut = item.clockOutTime
                    )
                }

            }
        }
    }

}

@Composable
fun InOutText(
    inOut: String,
    inOrOut: String
){
    Row {
        Text(
            text = inOrOut,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            ),
        )
        Text(
            text = inOut,
            style = MaterialTheme.typography.titleSmall.copy(
                color = MaterialTheme.colorScheme.primaryTextColor
            ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }

}
@Composable
fun InfoAttendanceItemsBox(
    name: StringResource,
    value: String,
) {
    Surface(
        tonalElevation = 4.dp,
        shape = MaterialTheme.shapes.small,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.dimens.small2,
                    horizontal = MaterialTheme.dimens.small1,
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(name),
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )
            Text(
                text = value,
                style = MaterialTheme.typography.displaySmall.copy(
                    color = MaterialTheme.colorScheme.primaryTextColor
                )
            )

        }
    }
}

@Composable
fun ReportTabRow(
    selectedIndex: Int,
    items: List<String>,
    onTabSelected: (Int) -> Unit
) {
    TabRow(
        selectedTabIndex = selectedIndex,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = MaterialTheme.dimens.small1),
        divider = {},
        indicator = {},
    ) {
        items.forEachIndexed { index, title ->
            val isSelected = selectedIndex == index
            val backgroundColor = if (isSelected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.veryLightGray
            }
            val textColor = if (isSelected) {
                MaterialTheme.colorScheme.onBackground
            } else {
                MaterialTheme.colorScheme.primaryTextColor
            }

            Tab(
                modifier = Modifier
                    .background(backgroundColor),
                selected = isSelected,
                onClick = { onTabSelected(index) },
                text = {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleSmall.copy(
                            color = textColor
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            )
        }
    }
}

@Composable
fun DateFilterReport(
    state: ReportScreenState,
    onAction: (ReportScreenViewAction) -> Unit
) {
    DateFilterDropdown(
        modifier = Modifier.fillMaxWidth()
            .padding(
                bottom = MaterialTheme.dimens.small3,
                start = MaterialTheme.dimens.small1,
                end = MaterialTheme.dimens.small1
            ),
        selectedMonth = state.monthDisplay,
        selectedYear = state.year.toString(),
        monthError = state.endMonthError,
        yearError = state.endYearError,
        onMonthSelected = { monthName, monthIndex ->
            onAction(ReportScreenViewAction.MonthField(showMonth = monthName, month = monthIndex))
        },
        onYearSelected = {year ->
            onAction(ReportScreenViewAction.YearField(year))
        },
        onMonthError = { message ->
            onAction(ReportScreenViewAction.MonthFieldError(message))
        },
        onYearError = { message ->
            onAction(ReportScreenViewAction.YearFieldError(message))
        },
        onSubmit = {
            onAction(ReportScreenViewAction.Submit(employeeId = state.employeeId))
        }
    )
}