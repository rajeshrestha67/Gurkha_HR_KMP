package com.gurkha.hr.components.date.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.data.model.CalendarModel

@Composable
fun rememberDateRangePickerState(
    calendarModel: CalendarModel,
    initialDisplayMonth: CalendarMonth? = null,
): DatePickerStateImpl {

    return remember {
        DatePickerStateImpl(
            initialDisplayMonth = initialDisplayMonth,
            calendarModel = calendarModel
        )
    }

}