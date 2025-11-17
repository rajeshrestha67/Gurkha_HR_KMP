package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.date.SelectableDates
import com.gurkha.hr.components.dimens
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.data.model.CalendarModel

@Composable
fun HorizontalMonthsList(
    pagerState: PagerState,
    selectedDate: CalendarDate?,
    onDateSelectionChange: (date: CalendarDate) -> Unit,
    onDisplayedMonthChange: (calendarMonth: CalendarMonth) -> Unit,
    calendarModel: CalendarModel,
    selectableDates: SelectableDates
) {

    HorizontalPager(
        modifier = Modifier.padding(
            start = MaterialTheme.dimens.small1,
            end = MaterialTheme.dimens.small1
        ),
        state = pagerState
    ) {
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            val month = calendarModel.getMonth(it)
            Month(
                month = month,
                onDateSelectionChange = onDateSelectionChange,
                today = calendarModel.today,
                selectedDate = selectedDate,
                selectableDates = selectableDates
            )
        }
    }

    LaunchedEffect(pagerState) {
        updateDisplayedMonth(
            pagerState = pagerState,
            onDisplayedMonthChange = onDisplayedMonthChange,
            calendarModel = calendarModel
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
internal suspend fun updateDisplayedMonth(
    pagerState: PagerState,
    onDisplayedMonthChange: (month: CalendarMonth) -> Unit,
    calendarModel: CalendarModel
) {
    snapshotFlow { pagerState.currentPage }.collect { page ->
        onDisplayedMonthChange(
            calendarModel.getMonth(page)
        )
    }
}