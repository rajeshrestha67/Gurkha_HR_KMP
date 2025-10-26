package com.gurkha.hr.date.data


data class CalendarDate(
    val year: Int,
    val month: Int,
    val dayOfMonth: Int,
    val page: Int
) : Comparable<CalendarDate> {
    override operator fun compareTo(other: CalendarDate): Int {
        return if (this.year == other.year) {
            if (this.month == other.month) {
                this.dayOfMonth.compareTo(other.dayOfMonth)
            } else {
                this.month.compareTo(other.month)
            }
        } else {
            this.year.compareTo(other.year)
        }
    }
}
