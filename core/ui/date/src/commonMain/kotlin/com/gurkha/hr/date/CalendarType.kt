package com.gurkha.hr.date

import com.gurkha.hr.date.data.CalendarMonth
import com.gurkha.hr.date.data.model.daysInMonth
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.plus

interface CalendarType {
    val monthsName: List<Pair<Int, String>>
    val yearRange: IntRange
    fun getMonth(page: Int): CalendarMonth
    fun getPage(year: Int, month: Int): Int
}

class GregorianCalendar : CalendarType {
    private val availableLastDate = run {
        val (year, month, day) = BSPointer.getLastDay()
        val englishDate = DateConverter.bsToAd(
            year = year, month = month, day = day
        )
        LocalDate(englishDate.year, englishDate.month, englishDate.day)
    }
    private val availableStartDate = run {
        val (year, month, day) = BSPointer.getFirstDay()
        val englishDate = DateConverter.bsToAd(
            year = year, month = month, day = day
        )
        LocalDate(englishDate.year, englishDate.month, englishDate.day)
    }
    override val monthsName: List<Pair<Int, String>>
        get() = listOf(
            1 to "January",
            2 to "February",
            3 to "March",
            4 to "April",
            5 to "May",
            6 to "June",
            7 to "July",
            8 to "August",
            9 to "September",
            10 to "October",
            11 to "November",
            12 to "December",
        )
    override val yearRange: IntRange = IntRange(availableStartDate.year, availableLastDate.year)
    override fun getMonth(page: Int): CalendarMonth {
        val date = availableStartDate
            .plus(DatePeriod(months = page))  // Add months
            .let { adjustedDate ->
                LocalDate(// Rebuild with day=1
                    year = adjustedDate.year,
                    month = adjustedDate.month,
                    day = 1
                )
            }
            .coerceAtMost(availableLastDate)
        return CalendarMonth(
            year = date.year,
            month = date.month.number,
            numberOfDays = date.daysInMonth(),
            startDate = 0,
            endDate = 0,
            daysFromStartOfWeekToFirstOfMonth = date.dayOfWeek.isoDayNumber % 7,
            pageNum = page
        )
    }

    private fun getPageIndex(localDate: LocalDate): Int {
        return (localDate.year - availableStartDate.year) * 12 + (localDate.month.number - availableStartDate.month.number)
    }

    override fun getPage(year: Int, month: Int): Int {
        return getPageIndex(LocalDate(year, month, 1))
    }
}

class BikramSambatCalendar : CalendarType {
    override val monthsName: List<Pair<Int, String>> = listOf(
        1 to "बैशाख",
        2 to "जेठ",
        3 to "असार",
        4 to "साउन",
        5 to "भदौ",
        6 to "असोज",
        7 to "कार्तिक",
        8 to "मंसिर",
        9 to "पुष",
        10 to "माघ",
        11 to "फाल्गुन",
        12 to "चैत"
    )
    override val yearRange: IntRange =
        IntRange(BSPointer.getFirstDay().first, BSPointer.getLastDay().first)

    override fun getMonth(page: Int): CalendarMonth {
        val (yearIndex, month) = BSPointer.getYearAndMonth(page)
        val year = BSPointer.getYear(yearIndex)
        val day = BSPointer.getNumOfDaysInMonth(Year.ofIndex(yearIndex), month)
        val englishDate = DateConverter.bsToAd(
            year = year, month = month, day = 1
        )

        val local = LocalDate(
            englishDate.year,
            englishDate.month,
            englishDate.day
        )

        val week = local.dayOfWeek
        val numberOfDays = local.daysInMonth()

        return CalendarMonth(
            year = year,
            month = month,
            numberOfDays = day,
            startDate = englishDate.day,
            endDate = numberOfDays,
            daysFromStartOfWeekToFirstOfMonth = week.isoDayNumber % 7,
            pageNum = page
        )
    }

    override fun getPage(year: Int, month: Int): Int {
        return BSPointer.getPageIndex(
            year = Year.ofValue(year),
            month = month
        )
    }
}
