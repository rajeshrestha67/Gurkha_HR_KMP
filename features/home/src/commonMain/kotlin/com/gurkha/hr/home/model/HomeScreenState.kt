package com.gurkha.hr.home.model

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EditCalendar
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.SyncLock
import androidx.compose.material.icons.filled.TimeToLeave
import androidx.compose.ui.graphics.vector.ImageVector
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData

data class HomeScreenState(

    val homeGridItemsToShow: List<AttendanceItem> = listOf(
        AttendanceItem(Icons.Filled.SyncLock, "Check In", "10.20 AM", "On Time"),
        AttendanceItem(Icons.Filled.LockClock, "Check Out", "5.30 AM", "Go Home"),
        AttendanceItem(Icons.Filled.TimeToLeave, "Leave", "3", "Total Leave"),
        AttendanceItem(Icons.Filled.EditCalendar, "Attendance", "22", "Working Days"),
    ),
    val fullName: String = "",
    val initials: String = "",
    val levelName: String = "",
    val email: String = "",
    val userProfileUrl: String? = null,
    val isProfileLoading: Boolean = false,
    val isAttendanceLoading: Boolean = false,
    val isBirthDayLoading: Boolean = false,
    val isAnniversaryLoading: Boolean = false,

    val attendanceReport: List<AttendanceData>? = null,
    val upComingBirthday: List<UpComingBirthdayData> = emptyList(),
    val upComingWorkAnniversary: List<UpComingWorkAnniversaryData> = emptyList(),
    
    val calendarData: List<CalendarDay> = listOf(),
    val todayBS: CalendarDate,
    val selectedDay: Int = 1
)


data class AttendanceItem(
    val icon: ImageVector,
    val title: String,
    val time: String,
    val status: String
)
