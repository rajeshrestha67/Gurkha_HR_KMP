package com.gurkha.hr.leave.model.leave

import com.gurkha.hr.domain.attendance.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.domain.leave.leaveReport.model.LeaveReportData

data class LeaveScreenState(
    val pendingTapItem: LeaveTapItem = LeaveTapItem(),
    val approvedTapItem: LeaveTapItem = LeaveTapItem(),
    val cancelTapItem: LeaveTapItem = LeaveTapItem(),
    val currentTapItem: LeaveTapItem = LeaveTapItem(),
    val fromDate: String = "",
    val toDate: String = "",
//    val attendanceStatus: AttendanceStatusEnum = AttendanceStatusEnum.PENDING,
    val leaveStatus: LeaveStatusEnum = LeaveStatusEnum.PENDING,
    val employeeName: String = "",
    val isSelf: String = "",
    val leaveRequestDataJson: String? = null,

    val isRequestingLeave: Boolean = false,
    val leaveRequestError: Boolean = false


)

//data class LeaveTapItem(
//    val isLoading: Boolean = false,
//    val result: List<AttendanceStatusData> = emptyList(),
//)
data class LeaveTapItem(
    val isLoading: Boolean = false,
    val result: List<LeaveReportData> = emptyList(),
)
