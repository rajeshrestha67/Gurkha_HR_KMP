package com.gurkha.model.leave.leaveReport

import kotlinx.serialization.Serializable

@Serializable
data class LeaveReportRequestDto(
    val leaveStatus: String
)

@Serializable
data class LeaveReportResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: LeaveReportDetailDto? = null,
    val success: Boolean? = null
)
@Serializable
data class LeaveReportDetailDto(
    val fullName: String? = null,
    val departName: String? = null,
    val levelName: String? = null,
    val employeeLeaveHistory: List<EmployeeLeaveHistoryDto>? = null,
    val leaveRequests: List<LeaveRequestDetailDto>? = null,
    val attendanceResponses: String? = null // can replace with proper type if known
)
@Serializable
data class EmployeeLeaveHistoryDto(
    val employeeId: Int? = null,
    val leaveTypeId: Int? = null,
    val leaveType: String? = null,
    val totalDaysAllowed: Int? = null,
    val totalDaysAssign: Int? = null,
    val totalDaysTaken: Int? = null,
    val substituteLeaveCount: Int? = null
)
@Serializable
data class LeaveRequestDetailDto(
    val id: Int? = null,
    val employeeId: Int? = null,
    val employeeType: String? = null,
    val leaveTypeId: Int? = null,
    val startDate: String? = null,
    val endDate: String? = null,
    val leaveStatus: String? = null,
    val reason: String? = null,
    val leaveDuration: String? = null,
    val assigneeId: Int? = null,
    val assigneeName: String? = null,
    val assigneeRemarks: String? = null,
    val approvedRemarks: String? = null,
    val approvedDate: String? = null,
    val active: String? = null,
    val totalDays: Double? = null,
    val leaveType: String? = null,
    val employeeName: String? = null,
    val requestedDate: String? = null,
    val performingUserId: Int? = null,
    val targetUserId: Int? = null,
    val revertRemarks: String? = null
)
