package com.gurkha.hr.date.data

import com.gurkha.hr.date.BSPointer
import com.gurkha.hr.date.DateConverter
import com.gurkha.hr.date.Year
import com.gurkha.hr.date.data.model.now
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toInstant
import kotlin.time.ExperimentalTime


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

    companion object {

    }
}

@OptIn(ExperimentalTime::class)
fun CalendarDate.toEpochMillis(): Long {
    val ad = DateConverter.bsToAd(year, month, dayOfMonth)
    return LocalDateTime(
        year = ad.year,
        month = ad.month,
        day = ad.day,
        hour = 0,
        minute = 0
    ).toInstant(TimeZone.UTC).toEpochMilliseconds()
}

fun CalendarDate.Companion.todayInBS(): CalendarDate {
    val today = LocalDate.now()
    val nepaliDate = DateConverter.adToBs(
        year = today.year,
        month = today.month.number,
        day = today.day
    )
    return CalendarDate(
        year = nepaliDate.year,
        month = nepaliDate.month,
        dayOfMonth = nepaliDate.day,
        page = BSPointer.getPageIndex(
            Year.ofValue(nepaliDate.year), month = nepaliDate.month
        )
    )
}