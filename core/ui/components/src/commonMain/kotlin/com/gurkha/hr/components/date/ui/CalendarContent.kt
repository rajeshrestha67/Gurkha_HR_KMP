package com.gurkha.hr.components.date.ui

import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.gurkha.hr.components.date.SelectableDates
import com.gurkha.hr.components.date.model.rememberCalendarModel
import com.gurkha.hr.components.date.state.rememberDateRangePickerState
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.res.SharedRes


@Composable
fun CalendarContent(
    selectedDate: CalendarDate? = null,
    modifier: Modifier = Modifier,
    selectableDates: SelectableDates,
    onDateSelected: (CalendarDate) -> Unit
) {


    val calendarType = remember {
        listOf(
            SharedRes.Strings.bs,
            SharedRes.Strings.ad
        )
    }
    val calendarModel = rememberCalendarModel()

    val state = rememberDateRangePickerState(
        calendarModel = calendarModel
    )
    var selectedCalendarType by remember { mutableStateOf(calendarType[0]) }

    val monthPagerState = rememberPagerState(
        initialPage = state.displayedMonth.pageNum,
        initialPageOffsetFraction = 0f,
        pageCount = { calendarModel.getNumberOfMonths() }
    )

    LaunchedEffect(selectedDate) {
        selectedDate?.let { selectedDate ->
            state.select(selectedDate)
        }
    }

    DateContent(
        modifier = modifier,
        state = state,
        calendarModel = calendarModel,
        monthPagerState = monthPagerState,
        selectableDates = selectableDates,
        onDateSelected = onDateSelected,
        calendarTypes = calendarType,
        selectedCalendarType = selectedCalendarType,
        onCalendarTypeSelected = {
            selectedCalendarType = it
        }
    )
}

