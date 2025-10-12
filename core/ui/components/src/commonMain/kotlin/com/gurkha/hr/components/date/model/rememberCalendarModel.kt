package com.gurkha.hr.components.date.model

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.gurkha.hr.date.data.model.CalendarModel
import com.gurkha.hr.date.data.model.CalendarModelImpl

@Composable
fun rememberCalendarModel(): CalendarModel {
    return remember { CalendarModelImpl() }
}