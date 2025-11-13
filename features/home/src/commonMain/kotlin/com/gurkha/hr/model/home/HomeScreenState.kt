package com.gurkha.hr.model.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.SyncLock
import androidx.compose.ui.graphics.vector.ImageVector
import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceStatus
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingEvent.model.EventData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.hr.res.SharedRes
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString

data class HomeScreenState(

    val requests: List<RequestItem> = RequestType.list.map { RequestItem(it) },
    val fullName: String = "",
    val initials: String = "",
    val levelName: String = "",
    val email: String = "",
    val userProfileUrl: String? = null,
    val isProfileLoading: Boolean = false,
    val isAttendanceLoading: Boolean = false,
    val isBirthDayLoading: Boolean = false,
    val isAnniversaryLoading: Boolean = false,
    val employeeId: Int = 0,
    val isProfileComplete: Boolean = false,

    val attendanceReport: List<AttendanceData> = listOf(),
    val attendanceReportHistory: List<AttendanceHistoryItemUI> = listOf(),
    val upComingBirthday: List<UpComingBirthdayData> = emptyList(),
    val upComingWorkAnniversary: List<UpComingWorkAnniversaryData> = emptyList(),

    val calendarData: List<CalendarDay> = listOf(),
    val todayBS: CalendarDate = CalendarDate(0, 0, 0, 0),
    val selectedDay: Int = 1,
    val showSwipeView: Boolean = false,
    val todayAttendance: AttendanceData? = null,
    val swipeText: StringResource = SharedRes.Strings.swipeToCheckIn,

    val isEventLoading: Boolean = false,
    val upComingEvent: List<EventData> = emptyList(),

    val totalNotificationCount: Int = 0,
    val isNotificationCountLoading: Boolean = false,

    val totalUnSeenNotification: Int = 0,

    val isRefreshing: Boolean = false,

    val isAttendanceCountLoading: Boolean = false,

    val isFetchingSupportList: Boolean = false,

    val showSupportChatIcon: Boolean = false,

    val enableManualAttendance: Boolean = false
)

data class RequestItem(
    val type: RequestType,
    val duration: String = "--:--"
)

enum class RequestType(
    val icon: ImageVector,
    val title: StringResource,
    val status: StringResource
) {

    CheckIn(
        icon = Icons.Filled.SyncLock,
        title = SharedRes.Strings.checkIn,
        status = SharedRes.Strings.onTime
    ),
    CheckOut(
        icon = Icons.Filled.LockClock,
        title = SharedRes.Strings.checkOut,
        status = SharedRes.Strings.goHome
    ),
    Leave(
        icon = Icons.Filled.EventBusy,
        title = SharedRes.Strings.absent,
        status = SharedRes.Strings.totalAbsentDays
    ),
    Attendance(
        icon = Icons.Filled.EventAvailable,
        title = SharedRes.Strings.present,
        status = SharedRes.Strings.totalPresentDays
    );

    companion object Companion {
        private val typeMap =
            enumValues<RequestType>().associateBy { it.title.key }

        fun get(typeName: StringResource): RequestType =
            RequestType.typeMap[typeName.key] ?: CheckIn

        val list: List<RequestType>
            get() = entries.toList().map { it }
    }

}


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
            var showPresent = (status.value == AttendanceStatus.PRESENT.value)
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

            if(!showPresent && !isEarlyOut && !isLate){
                add(getString(SharedRes.Strings.absent))
            }
        } else {
            add(getString(SharedRes.Strings.absent))
        }
    }
    return AttendanceHistoryItemUI(
        clockInTime = clockInTime ?: "--:--",
        clockOutTime = clockOutTime ?: "--:--",
        date = "$date ($day)",
        status = status,
        statusClips = chips,
        isHoliday = isHoliday
    )
}

