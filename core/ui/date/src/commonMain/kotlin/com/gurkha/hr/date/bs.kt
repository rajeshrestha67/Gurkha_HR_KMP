package com.gurkha.hr.date

import androidx.compose.runtime.Composable
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.stringArrayResource
import kotlin.jvm.JvmInline
import kotlin.math.floor
import kotlin.math.sign

private const val TAG = "bs"

@JvmInline
value class Year(
    val yearIndex: Int
) {
    companion object {
        fun ofValue(year: Int) = Year(BSPointer.getYearIndex(year))
        fun ofIndex(yearIndex: Int) = Year(yearIndex)
    }
}


class BSPointer(
    year: Year,
    val month: Int,
    val day: Int
) {

    val yearIndex = year.yearIndex

    val year: Int
        get() = getYear(yearIndex = yearIndex)

    init {
        check(month in 1..12) {
            "Month, $month should be between 1 and 12."
        }
        check(this.year in BS_DATES.first()[0]..BS_DATES.last()[0]) {
            "Invalid Year."
        }
    }


    private tailrec fun find(remainingDays: Int, monthOffset: Int): BSPointer {
        val (yearIndex, monthIndex) = addMonthToPointer(monthOffset)
        val numOfDaysInMonth = getNumOfDaysInMonth(Year.ofIndex(yearIndex), monthIndex)
        return if (remainingDays in (-numOfDaysInMonth + 1)..numOfDaysInMonth) {
            BSPointer(
                year = Year.ofIndex(yearIndex),
                month = monthIndex,
                day = if (remainingDays <= 0) {
                    numOfDaysInMonth + remainingDays
                } else {
                    remainingDays
                }
            )
        } else {
            val sign = remainingDays.sign
            find(
                remainingDays - (numOfDaysInMonth * sign),
                monthOffset + sign
            )
        }
    }


    operator fun plus(value: Int): BSPointer {
        val remainingDays = day + value
        val monthOffset = if (remainingDays <= 0) {
            -1
        } else {
            0
        }
        return find(remainingDays, monthOffset)
    }


    private fun addMonthToPointer(monthToAdd: Int): Pair<Int, Int> {
        val offset = (month - 1) + monthToAdd
        return Pair(
            first = yearIndex + floor(offset / 12f).toInt(),
            second = ((offset % 12) + 12) % 12 + 1
        )
    }

    fun getPointedValue(monthToAdd: Int): Int {
        val (yearIndex, monthIndex) = addMonthToPointer(monthToAdd)
        return getNumOfDaysInMonth(Year.ofIndex(yearIndex), monthIndex)
    }

    operator fun minus(value: Int): BSPointer {
        return plus(-value)
    }


    operator fun minus(other: BSPointer): Int {
        val shouldBePositive = this > other
        val (low, high) = if (shouldBePositive) other to this else this to other

        var monthOffset = 0
        var days = 0

        while (low.addMonthToPointer(monthOffset) != high.addMonthToPointer(0)) {
            days += low.getPointedValue(monthOffset)
            monthOffset += 1
        }

        return (days - low.day + high.day) * if (shouldBePositive) 1 else -1

    }

    operator fun compareTo(other: BSPointer): Int {
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

    override fun toString(): String {
        return "Year = $year,  Month = $month, Day = $day"
    }

    companion object {
        fun getYear(yearIndex: Int) = BS_DATES[yearIndex][0]

        fun getNumOfDaysInMonth(year: Year, month: Int) =
            BS_DATES[year.yearIndex][month]

        fun getNumberOfAvailableMonths() = BS_DATES.size * 12

        fun getYearIndex(year: Int) = BS_DATES.indexOfFirst { it[0] == year }

        fun getYearAndMonth(position: Int): Pair<Int, Int> {
            val yearIndex = position / 12
            val month = (position % 12) + 1
            return yearIndex to month
        }

        fun getPageIndex(year: Year, month: Int) = year.yearIndex * 12 + month - 1

        fun getFirstDay() = Triple(
            BS_DATES.first()[0],
            1,
            1
        )

        fun getLastDay() = Triple(
            BS_DATES.last()[0],
            12,
            BS_DATES.last().last()
        )

        fun getYearRange(): IntRange{
            return IntRange(BSPointer.getFirstDay().first, BSPointer.getLastDay().first)
        }


    }


}


private val BS_DATES: Array<Array<Int>> = arrayOf(
    arrayOf(1971, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(1972, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(1973, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(1974, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1975, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(1976, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(1977, 30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31),
    arrayOf(1978, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1979, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(1980, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(1981, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30),
    arrayOf(1982, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1983, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(1984, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(1985, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30),
    arrayOf(1986, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1987, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(1988, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(1989, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1990, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1991, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
    arrayOf(1992, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(1993, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1994, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1995, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
    arrayOf(1996, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(1997, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1998, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(1999, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2000, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2001, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2002, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2003, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2004, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2005, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2006, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2007, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2008, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31),
    arrayOf(2009, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2010, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2011, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2013, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2013, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2014, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2015, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2016, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30),
    arrayOf(2017, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2018, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2019, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2020, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2021, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2022, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
    arrayOf(2023, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2024, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2025, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2026, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2027, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2028, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2029, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2030, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2031, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2032, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2033, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2034, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2035, 30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31),
    arrayOf(2036, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2037, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2038, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2039, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30),
    arrayOf(2040, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2041, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2042, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2043, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30),
    arrayOf(2044, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2045, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2046, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2047, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2048, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2049, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
    arrayOf(2050, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2051, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2052, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2053, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
    arrayOf(2054, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2055, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2056, 31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2057, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2058, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2059, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2060, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2061, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2062, 30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31),
    arrayOf(2063, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2064, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2065, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2066, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31),
    arrayOf(2067, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2068, 31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2069, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2070, 31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30),
    arrayOf(2071, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2072, 31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30),
    arrayOf(2073, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31),
    arrayOf(2074, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2075, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2076, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
    arrayOf(2077, 31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2078, 31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2079, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2080, 31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30),
    arrayOf(2081, 31, 31, 32, 32, 31, 30, 30, 30, 29, 30, 29, 31),
    arrayOf(2082, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2083, 31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30),
    arrayOf(2084, 31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30),
    arrayOf(2085, 31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30),
    arrayOf(2086, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30),
    arrayOf(2087, 31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30),
    arrayOf(2088, 30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30),
    arrayOf(2089, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30),
    arrayOf(2090, 30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30)
)


val String.mapNumbers: String
    @Composable
    get() = this.map {
        val numbers = stringArrayResource(SharedRes.Arrays.numbers)
        when (it) {
            '0' -> numbers[0] // ०
            '1' -> numbers[1]  // १
            '2' -> numbers[2]  // २
            '3' -> numbers[3]  // ३
            '4' -> numbers[4]  // ४
            '5' -> numbers[5]  // ५
            '6' -> numbers[6]  // ६
            '7' -> numbers[7]  // ७
            '8' -> numbers[8]  // ८
            '9' -> numbers[9]  // ९
            else -> it
        }
    }.joinToString("")
val Int.mapNumbers: String
    @Composable
    get() = this.toString().mapNumbers

//val months_ = SharedRes.Arrays.months

//val months = listOf(
//    1 to "बैशाख",
//    2 to "जेठ",
//    3 to "असार",
//    4 to "साउन",
//    5 to "भदौ",
//    6 to "असोज",
//    7 to "कार्तिक",
//    8 to "मंसिर",
//    9 to "पुष",
//    10 to "माघ",
//    11 to "फाल्गुन",
//    12 to "चैत"
//)