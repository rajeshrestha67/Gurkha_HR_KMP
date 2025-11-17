package com.gurkha.hr.components.date.model

import com.gurkha.hr.components.date.SelectableDates
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.toEpochMillis
import com.gurkha.hr.date.data.todayInBS
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


object DatePickerDefaults {
    val AllDates: SelectableDates = object : SelectableDates {
        override fun isSelectableDate(date: CalendarDate): Boolean {
            return true
        }

        override fun isSelectableYear(year: Int): Boolean {
            return true
        }
    }


    val FutureAndTodayDate: SelectableDates = RangeSelectableDates(
        minDate = CalendarDate.todayInBS()
    )


    open class RangeSelectableDates(
        minDate: CalendarDate? = null,
        maxDate: CalendarDate? = null,
    ) : SelectableDates {
        val minDateMillis = minDate?.toEpochMillis()
        val maxDateMillis = maxDate?.toEpochMillis()

        override fun isSelectableDate(date: CalendarDate): Boolean {
            val dateMillis = date.toEpochMillis()

            val afterMin = minDateMillis?.let { dateMillis >= it } ?: true
            val beforeMax = maxDateMillis?.let { dateMillis <= it } ?: true

            return afterMin && beforeMax
        }

        @OptIn(ExperimentalTime::class)
        override fun isSelectableYear(year: Int): Boolean {
            val afterMin = minDateMillis?.let {
                year >= Instant.fromEpochMilliseconds(it)
                    .toLocalDateTime(TimeZone.UTC).year
            } ?: true

            val beforeMax = maxDateMillis?.let {
                year <= Instant.fromEpochMilliseconds(it)
                    .toLocalDateTime(TimeZone.UTC).year
            } ?: true

            return afterMin && beforeMax
        }
    }
}