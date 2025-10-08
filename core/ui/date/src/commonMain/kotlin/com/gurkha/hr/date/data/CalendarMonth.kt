package com.gurkha.hr.date.data

data class CalendarMonth(
    val year: Int,
    val month: Int,
    val numberOfDays: Int,
    val daysFromStartOfWeekToFirstOfMonth: Int,
    val pageNum: Int,
    val startDate: Int,
    val endDate: Int,
) : Comparable<CalendarMonth> {
    override operator fun compareTo(other: CalendarMonth): Int {
        return this.pageNum.compareTo(other.pageNum)
    }

    fun indexIn(years: IntRange): Int {
        return (year - years.first) * 12 + month - 1
    }

    fun toDate(date: Int) = CalendarDate(
        year = this.year,
        month = this.month,
        dayOfMonth = date,
        page = this.pageNum
    )
}
