package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.agnepal.ambitionguru.ad_bs_calendar.ui.DateContent
import com.gurkha.hr.components.date.model.rememberCalendarModel
import com.gurkha.hr.components.date.state.rememberDateRangePickerState
import com.gurkha.hr.date.data.CalendarDate


@Composable
fun CalendarContent(
    modifier: Modifier = Modifier,
    onDateSelected: (CalendarDate) -> Unit
) {

    val calendarModel = rememberCalendarModel()

    val state = rememberDateRangePickerState(
        calendarModel = calendarModel
    )

    val monthPagerState = rememberPagerState(
        initialPage = state.displayedMonth.pageNum,
        initialPageOffsetFraction = 0f,
        pageCount = { calendarModel.getNumberOfMonths() }
    )


    DateContent(
        modifier = modifier,
        state = state,
        calendarModel = calendarModel,
        monthPagerState = monthPagerState,
        onDateSelected = onDateSelected,
        onPickerShouldOpen = {
            //onBottomSheetToggle(true)
        }
    )
}

