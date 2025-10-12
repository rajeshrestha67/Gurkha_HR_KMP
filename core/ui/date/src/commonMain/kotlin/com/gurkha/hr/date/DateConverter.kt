package com.gurkha.hr.date

import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.plus


private const val TAG = "DateConverter"

fun main() {


//    val c = a.adToBs(1991, 5, 3)
//    println(c)
//
//    val d = a.bsToAd(c.year, c.month, c.day)
//    println(d)

    val c = DateConverter.bsToAd(2081, 12, 22)
    println(c)

    val d = DateConverter.adToBs(c.year, c.month, c.day)
    println(d)
//    a.adToBs(
//        DateHolder(
//            2023, 11, 19
//        )
//    )


}

object DateConverter {
    private const val YEAR_AD = 2001
    private const val MONTH_AD = 12
    private const val DAY_AD = 11


    private const val YEAR_BS = 2058
    private const val MONTH_BS = 8
    private const val DAY_BS = 26
    fun adToBs(year: Int, month: Int, day: Int): DateHolder {
        val startingDate = LocalDate(YEAR_AD, MONTH_AD, DAY_AD)

        val endingDate = LocalDate(year, month, day)

        val days = startingDate.daysUntil(endingDate)

        var bsPointer = BSPointer(
            year = Year.ofValue(YEAR_BS),
            month = MONTH_BS,
            day = DAY_BS
        )


        bsPointer += days


        return DateHolder(
            year = bsPointer.year,
            month = bsPointer.month,
            day = bsPointer.day
        )

    }

    fun bsToAd(year: Int, month: Int, day: Int): DateHolder {

        val startingDay = BSPointer(
            year = Year.ofValue(YEAR_BS),
            month = MONTH_BS,
            day = DAY_BS
        )
        val endingDay = BSPointer(
            year = Year.ofValue(year), month = month, day = day
        )

        val days = endingDay - startingDay

        val startingDate = LocalDate(YEAR_AD, MONTH_AD, DAY_AD)
        val requiredData = startingDate.plus(DatePeriod(days = days))


        return DateHolder(
            year = requiredData.year,
            month = requiredData.monthNumber,
            day = requiredData.dayOfMonth
        )

    }
}

data class DateHolder(
    val year: Int,
    val month: Int,
    val day: Int
) {
    operator fun compareTo(other: DateHolder): Int {
        return if (this.year == other.year) {
            if (this.month == other.month) {
                this.day.compareTo(other.day)
            } else {
                this.month.compareTo(other.month)
            }
        } else {
            this.year.compareTo(other.year)
        }
    }
}
