package com.gurkha.hr.date.data.model

import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.date.data.CalendarMonth

abstract class CalendarModel {
    abstract val today: CalendarDate

    //    abstract val weekdayNames: List<String>
    abstract fun numberOfDaysInMonth(): List<CalendarDay>

    abstract fun getPage(year: Int, month: Int): Int

    abstract fun getYearRange(): IntRange

    abstract fun getMonth(calendarDate: CalendarDate): CalendarMonth

    abstract fun getMonth(page: Int): CalendarMonth

    abstract fun getNumberOfMonths(): Int
}