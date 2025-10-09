package com.agnepal.ambitionguru.ad_bs_calendar.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.gurkha.hr.components.date.state.DatePickerStateImpl
import com.gurkha.hr.components.date.ui.DatePickerContent
import com.gurkha.hr.components.date.ui.TodayContent
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.data.model.todayFormattedADDate
import com.gurkha.hr.date.data.model.todayFormattedBSDate
import com.gurkha.hr.res.theme.dimens
import kotlinx.coroutines.launch

@Composable
fun DateContent(
    modifier: Modifier = Modifier,
    state: DatePickerStateImpl,
    calendarModel: CalendarModel,
    monthPagerState: PagerState,
    onDateSelected: (CalendarDate) -> Unit,
    onPickerShouldOpen: () -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(Unit) {
        onDateSelected(calendarModel.today)
    }
    Column(
        modifier = modifier
    ) {
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
            onPickerShouldOpen = onPickerShouldOpen,
            onTodayClick = {
                onDateSelected(calendarModel.today)
                coroutineScope.launch {
                    monthPagerState.scrollToPage(calendarModel.today.page)
                }

            }
        )

        DatePickerContent(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = MaterialTheme.dimens.small1
                )
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
            monthsPagerState = monthPagerState
        )
    }
}