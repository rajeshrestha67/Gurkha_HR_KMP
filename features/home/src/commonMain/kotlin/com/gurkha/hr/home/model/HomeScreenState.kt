package com.gurkha.hr.home.model

import com.gurkha.hr.date.data.CalendarDate
import com.gurkha.hr.date.data.CalendarDay
import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.hr.domain.userDetail.model.UserDetailData

data class HomeScreenState(

    val homeGridItemsToShow: List<AttendanceItem> = homeGridItems,
    val fullName: String = "",
    val initials: String = "",
    val levelName: String = "",
    val email: String = "",
    val userProfileUrl: String? = null,
    val isProfileLoading: Boolean = false,
    val isAttendanceLoading: Boolean = false,
    val isBirthDayLoading: Boolean = false,
    val isAnniversaryLoading: Boolean = false,
    val fromDate: String = "2025-09-16",
    val toDate: String = "2025-09-17",
    val enableManualAttendance: String = "N",
    val branchId: String? = null,

    val attendanceReport: List<AttendanceData>? = null,
    val userDetail: UserDetailData? = null,
    val upComingBirthday: List<UpComingBirthdayData> = emptyList(),
    val upComingWorkAnniversary: List<UpComingWorkAnniversaryData> = emptyList(),

    val clockInTime: String = "",
    val clockOutTime: String = "",
    val isLeaveEarly: String = "",
    val isLate: String = "",
    val calendarData: List<CalendarDay> = listOf(),
    val todayBS: CalendarDate
)