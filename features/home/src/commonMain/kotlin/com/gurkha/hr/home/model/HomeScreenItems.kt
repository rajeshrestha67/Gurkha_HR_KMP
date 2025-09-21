package com.gurkha.hr.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.SyncLock
import androidx.compose.material.icons.filled.TimeToLeave
import androidx.compose.ui.graphics.vector.ImageVector
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun generateCalendarDays(
    startDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    daysCount: Int = 365,
    previousDays: Int = 30
): List<CalendarItem> {
    val dayOfWeekNumber = startDate.dayOfWeek.isoDayNumber
    val daysToSubtract = if (dayOfWeekNumber != 7) dayOfWeekNumber else 0
    val firstSunday = startDate.minus(DatePeriod(days = daysToSubtract))
    val calendarStartDate = firstSunday.minus(DatePeriod(days = previousDays))

    return (0 until (daysCount + previousDays)).map { offset ->
        val date = calendarStartDate.plus(DatePeriod(days = offset))
        val dayOfWeek = date.dayOfWeek.name.take(3)
        val dateOfMonth = date.dayOfMonth.toString()

        CalendarItem(
            day = dayOfWeek,
            date = dateOfMonth,
            active = date == startDate
        )
    }
}


data class CalendarItem(
    val day: String,
    val date: String,
    val active: Boolean
)

data class AttendanceItem(
    val icon: ImageVector,
    val title: String,
    val time: String,
    val status: String
)

val attendanceList = listOf(
    AttendanceItem(Icons.Filled.SyncLock, "Check In", "10.20 AM", "On Time"),
    AttendanceItem(Icons.Filled.LockClock, "Check Out", "5.30 AM", "Go Home"),
)
val attendanceList2 = listOf(
    AttendanceItem(Icons.Filled.TimeToLeave, "Leave", "3", "Total Leave"),
    AttendanceItem(Icons.Filled.EditCalendar, "Attendance", "22", "Working Days"),
)