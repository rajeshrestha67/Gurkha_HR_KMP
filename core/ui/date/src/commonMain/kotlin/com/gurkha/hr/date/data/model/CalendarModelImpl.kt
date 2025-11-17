package com.gurkha.hr.date.data.model

import androidx.compose.runtime.Composable
import com.gurkha.hr.date.BSPointer
import com.gurkha.hr.date.DateConverter
import com.gurkha.hr.date.Year
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.data.todayInBS
import com.gurkha.hr.date.mapNumbers
import com.gurkha.hr.res.SharedRes
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.daysUntil
import kotlinx.datetime.format
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.compose.resources.stringArrayResource
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

class CalendarModelImpl : CalendarModel() {

    override val today: CalendarDate
        get() {
            return CalendarDate.todayInBS()
        }

    override fun numberOfDaysInMonth(): List<CalendarDay> {
        val range = 1..BSPointer.getNumOfDaysInMonth(
            year = Year.ofValue(today.year),
            month = today.month
        )

        val firstDayAD = DateConverter.bsToAd(today.year, today.month, 1)
        val localDate = LocalDate(firstDayAD.year, firstDayAD.month, firstDayAD.day)
        var dayOfWeek = getDayNumberSundayFirst(localDate)
        return range.toList().map { day ->
            if (dayOfWeek == 8) {
                dayOfWeek = 1
            }
            val dayCalendar = CalendarDay(dayOfWeek, day, dayOfWeek == 7)
            dayOfWeek++
            dayCalendar
        }
    }

    private fun getDayNumberSundayFirst(date: LocalDate = LocalDate.now()): Int {
        val day = date.dayOfWeek.isoDayNumber
        return if (day == 7) 1 else day + 1
    }

    override fun getPage(year: Int, month: Int): Int {
        return BSPointer.getPageIndex(
            year = Year.ofValue(year),
            month = month
        )
    }


    override fun getYearRange(): IntRange {
        return BSPointer.getYearRange()
    }

    override fun getMonth(calendarDate: CalendarDate): CalendarMonth {
        return getMonth(calendarDate.page)
    }

    override fun getMonth(page: Int): CalendarMonth {

        val (yearIndex, month) = BSPointer.getYearAndMonth(page)
        val year = BSPointer.getYear(yearIndex)
        val englishDate = DateConverter.bsToAd(
            year = year, month = month, day = 1
        )

        val local = LocalDate(
            englishDate.year,
            englishDate.month,
            englishDate.day
        )
        val numberOfDays = local.daysInMonth()
        val week = local.dayOfWeek
        return CalendarMonth(
            year = year,
            month = month,
            numberOfDays = BSPointer.getNumOfDaysInMonth(Year.ofIndex(yearIndex), month),
            daysFromStartOfWeekToFirstOfMonth = week.isoDayNumber % 7,
            pageNum = page,
            startDate = englishDate.day,
            endDate = numberOfDays,
        )
    }


    override fun getNumberOfMonths(): Int {
        return BSPointer.getNumberOfAvailableMonths()
    }


}

@OptIn(ExperimentalTime::class)
fun LocalDate.Companion.now(): LocalDate =
    Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

fun LocalDate.daysInMonth(): Int {
    val firstDayOfMonth = LocalDate(year, month, 1)
    val firstDayOfNextMonth = firstDayOfMonth.plus(DatePeriod(months = 1))
    return firstDayOfMonth.daysUntil(firstDayOfNextMonth)
}

@Composable
fun CalendarModel.todayFormattedBSDate(): String {
    val months = stringArrayResource(SharedRes.Arrays.months)
    val weeks = stringArrayResource(SharedRes.Arrays.weeksDays)
    val monthsCalendar = numberOfDaysInMonth().find { it.day == today.dayOfMonth }
    return "${today.dayOfMonth.mapNumbers} ${months[today.month - 1]}, ${today.year.mapNumbers} ${
        weeks[monthsCalendar?.let {
            it.dayOfWeek - 1
        } ?: 0]
    }"
}

@OptIn(ExperimentalTime::class)
fun CalendarModel.todayFormattedADDate(): String {
    return try {

        val instant = Clock.System.now()
        val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())

        val dayOfWeekName = localDateTime.date.dayOfWeek.name.lowercase()
            .replaceFirstChar { it.uppercase() }
        localDateTime.format(
            LocalDateTime.Format {
                day(padding = Padding.ZERO) // Day (no padding)
                chars(" ")
                monthName(MonthNames.ENGLISH_FULL) // Full month name (e.g., "June")
                chars(", ")
                year() // Year
                chars(" ")
                chars(dayOfWeekName)
            }
        )
    } catch (e: Exception) {
        e.printStackTrace()
        "Invalid date: $this" // Fallback for parsing errors
    }
}