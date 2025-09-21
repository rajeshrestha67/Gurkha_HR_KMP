package com.gurkha.hr.home.Model

import com.gurkha.hr.domain.attendance.model.AttendanceData
import com.gurkha.hr.domain.upComingBirthday.model.UpComingBirthdayData
import com.gurkha.hr.domain.upComingWorkAnniversaries.model.UpComingWorkAnniversaryData
import com.gurkha.hr.domain.userDetail.model.UserDetailData

data class HomeScreenState(
    val calendarItem: List<CalendarItem> = generateCalendarDays(),
    val requestRow1: List<AttendanceItem> = attendanceList,
    val requestRow2: List<AttendanceItem> = attendanceList2,
    val fullName: String = "",
    val levelName: String = "",
    val email: String = "",
    val userProfileUrl: String? = null,
    val isLoading : Boolean = false,
    val fromDate: String = "2025-09-16",
    val toDate : String = "2025-09-17",
    val enableManualAttendance : String ="N",
    val branchId : String ? = null,

    val attendanceReport : List<AttendanceData> ? = null,
    val userDetail : UserDetailData ? = null,
    val upComingBirthday : List<UpComingBirthdayData>? = emptyList(),
    val upComingWorkAnniversary : List<UpComingWorkAnniversaryData>? = emptyList()
)