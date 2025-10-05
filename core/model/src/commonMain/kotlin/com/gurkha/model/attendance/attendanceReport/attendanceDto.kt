package com.gurkha.model.attendance.attendanceReport

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequestDto(
    val fromDate: String,
    val toDate: String,
)

@Serializable
data class AttendanceResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<AttendanceDataResponseDto>? = null,
    val success: Boolean? = null
)


@Serializable
data class AttendanceDataResponseDto(
    val attendanceId: Int? = null,
    val created: String? = null,
    val forRequestedDate: String? = null,
    val employeeId: Int? = null,
    val employeeName: String? = null,
    val employeeType: String? = null,
    val departmentId: Int? = null,
    val branchId: Int? = null,
    val branchName: String? = null,
    val clockInTime: String? = null,
    val clockOutTime: String? = null,
    val clockInImage: String? = null,
    val clockOutImage: String? = null,
    val employeeMapId: String? = null,
    val onLeave: Boolean? = null,
    val attendanceStatus: String? = null,
    val enableManualAttendance: String? = null,
    val enableImageAttendance: String? = null,
    val attendanceRequestStatus: String? = null,
    val isLeaveModification: String? = null,
    val isLeaveEarly: Boolean? = null,
    val isLate: Boolean? = null,
    val message: String? = null,
    val dayOfWeek: String? = null,
    val reason: String? = null,
    val workingHrs: String? = null,
    val imageUrl: String? = null,
    val leaveDuration: String? = null,
    val lateInDuration: String? = null,
    val approvedRemarks: String? = null,
    val holiday: Boolean? = null
)