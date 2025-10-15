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
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceStatus
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.getString

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

    val attendanceReport: List<AttendanceData> = listOf(),
    val attendanceReportHistory: List<AttendanceHistoryItemUI> = listOf(),
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

data class AttendanceHistoryItemUI(
    val clockInTime: String,
    val clockOutTime: String,
    val date: String,
    val isHoliday: Boolean,
    val status: AttendanceStatus,
    val statusClips: List<String>
)


suspend fun AttendanceData.toUI(): AttendanceHistoryItemUI {
    val chips = mutableListOf<String>().apply {
        if (isHoliday) {
            add(getString(SharedRes.Strings.holiday))
        } else if (!onLeave) {
            var showPresent = true
            if (isLate) {
                showPresent = false
                add(getString(SharedRes.Strings.late_in))
            }
            if (isEarlyOut) {
                showPresent = false
                add(getString(SharedRes.Strings.early_out))
            }

            if (showPresent) {
                add(getString(SharedRes.Strings.present))
            }
        } else {
            add(getString(SharedRes.Strings.absent))
        }
    }
    return AttendanceHistoryItemUI(
        clockInTime = clockInTime,
        clockOutTime = clockOutTime,
        date = "$date ($day)",
        status = status,
        statusClips = chips,
        isHoliday = isHoliday
    )
}

