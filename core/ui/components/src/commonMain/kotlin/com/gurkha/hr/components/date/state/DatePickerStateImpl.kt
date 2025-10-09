package com.gurkha.hr.components.date.state

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.data.model.CalendarModel

@Stable
class DatePickerStateImpl(
    initialDisplayMonth: CalendarMonth?,
    calendarModel: CalendarModel
) : DatePickerState<CalendarDate> {
    var selectedItem: CalendarDate? by mutableStateOf(calendarModel.today)
    override val selectedDate: CalendarDate? = null
    override var selection: CalendarDate?
        get() {
            return selectedItem
        }
        set(value) {
            selectedItem = value
        }
    override var displayedMonth: CalendarMonth by mutableStateOf(
        initialDisplayMonth ?: calendarModel.getMonth(
            calendarModel.today
        )
    )

    override fun select(date: CalendarDate) {
        selectedItem = date
    }

}

