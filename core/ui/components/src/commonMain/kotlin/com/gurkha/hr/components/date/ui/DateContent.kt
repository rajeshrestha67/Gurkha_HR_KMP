package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.gurkha.hr.components.date.SelectableDates
import com.gurkha.hr.components.date.state.DatePickerStateImpl
import com.gurkha.hr.components.dimens
import com.gurkha.hr.components.tabbar.ERPTabView
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.data.model.todayFormattedADDate
import com.gurkha.hr.date.data.model.todayFormattedBSDate
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun DateContent(
    modifier: Modifier = Modifier,
    state: DatePickerStateImpl,
    calendarModel: CalendarModel,
    monthPagerState: PagerState,
    selectableDates: SelectableDates,
    onCalendarTypeSelected: (StringResource) -> Unit,
    selectedCalendarType: StringResource,
    calendarTypes: List<StringResource>,
    onDateSelected: (CalendarDate) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        onDateSelected(calendarModel.today)
    }


    Column(
        modifier = modifier
    ) {
        ERPTabView(
            modifier = Modifier.fillMaxWidth(0.4f).padding(vertical = MaterialTheme.dimens.small1)
                .align(Alignment.CenterHorizontally),
            items = calendarTypes,
            selectedTab = selectedCalendarType,
            onItemSelected = {
                onCalendarTypeSelected(it)
            }
        ) { item, isSelected ->
            val color =
                if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground
            Text(
                text = stringResource(item),
                style = MaterialTheme.typography.titleSmall.copy(
                    color = color
                )
            )
        }
        TodayContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.dimens.small1,
                    horizontal = MaterialTheme.dimens.medium1
                )
                .clip(
                    shape = MaterialTheme.shapes.small
                ),
            bsDate = calendarModel.todayFormattedBSDate(),
            adDate = calendarModel.todayFormattedADDate(),
            onTodayClick = {
                onDateSelected(calendarModel.today)
                coroutineScope.launch {
                    monthPagerState.scrollToPage(calendarModel.today.page)
                }

            }
        )
        HorizontalDivider(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.dimens.medium1,
                vertical = MaterialTheme.dimens.small2
            )
        )
        DatePickerContent(
            modifier = Modifier
                .fillMaxWidth()
                .clip(
                    shape = MaterialTheme.shapes.small
                ),
            selectedDate = state.selectedItem,
            displayedMonth = state.displayedMonth,
            onDateSelectionChange = {
                state.select(it)
                onDateSelected(it)
            },
            onDisplayedMonthChange = {
                state.displayedMonth = it
            },
            calendarModel = calendarModel,
            monthsPagerState = monthPagerState,
            selectableDates = selectableDates
        )
    }
}