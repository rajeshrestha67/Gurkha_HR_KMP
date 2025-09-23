package com.gurkha.hr.leave.model

import com.gurkha.hr.domain.attendanceStatus.model.AttendanceStatusData

data class LeaveScreenState(
    val isLoading : Boolean = false,
//    val attendanceResult : List<AttendanceStatusData> = emptyList(),
    val pendingResult : List<AttendanceStatusData> = emptyList(),
    val approvedResult : List<AttendanceStatusData> = emptyList(),
    val cancelledResult : List<AttendanceStatusData> = emptyList(),
    val fromDate : String = "",
    val toDate : String = "",
    val attendanceStatus : String = "pending",
    val employeeName : String = "",
    val isSelf : String = ""
)
