package com.gurkha.model.timeandAttendance

import kotlinx.serialization.Serializable
@Serializable
data class TimeAttendanceReportResponseDTO(
    val status: String?,
    val message: String?,
    val detail: List<TimeAttendanceDetail>?,
    val success: Boolean?
)
@Serializable
data class TimeAttendanceDetail(
    val attendanceId: Int?,
    val created: String?,
    val forRequestedDate: String?,
    val employeeId: Int?,
    val employeeName: String?,
    val employeeType: String?,
    val departmentId: Int?,
    val branchId: Int?,
    val branchName: String?,
    val clockInTime: String?,
    val clockOutTime: String?,
    val clockInImage: String?,
    val clockOutImage: String?,
    val employeeMapId: String?,
    val onLeave: Boolean?,
    val attendanceStatus: String?,
    val enableManualAttendance: String?,
    val enableImageAttendance: String?,
    val attendanceRequestStatus: String?,
    val isLeaveModification: Boolean?,
    val isLeaveEarly: Boolean?,
    val isLate: Boolean?,
    val message: String?,
    val dayOfWeek: String?,
    val reason: String?,
    val workingHrs: String?,
    val imageUrl: String?,
    val leaveDuration: String?,
    val lateInDuration: String?,
    val approvedRemarks: String?,
    val holiday: Boolean?
)


@Serializable
data class TimeAttendanceReportRequestDTO(
    val fromDate: String,
    val toDate: String
)



