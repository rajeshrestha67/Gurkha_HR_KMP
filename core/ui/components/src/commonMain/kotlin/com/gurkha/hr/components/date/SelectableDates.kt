package com.gurkha.hr.components.date

import com.gurkha.hr.date.data.CalendarDate

interface SelectableDates {

    fun isSelectableDate(date: CalendarDate): Boolean

    fun isSelectableYear(year: Int): Boolean

}

fun SelectableDates.merge(selectableDate: SelectableDates): SelectableDates {
    return object : SelectableDates {
        override fun isSelectableDate(date: CalendarDate): Boolean {
            return this.isSelectableDate(date) && selectableDate.isSelectableDate(date)

        }

        override fun isSelectableYear(year: Int): Boolean {
            return this.isSelectableYear(year) && selectableDate.isSelectableYear(year)
        }
    }
}