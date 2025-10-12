package com.gurkha.hr.components.date.state

import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarMonth


interface DatePickerState<T> {

    var selection: T?

    var displayedMonth: CalendarMonth

    fun select(date: CalendarDate)

    val selectedDate: CalendarDate?


}