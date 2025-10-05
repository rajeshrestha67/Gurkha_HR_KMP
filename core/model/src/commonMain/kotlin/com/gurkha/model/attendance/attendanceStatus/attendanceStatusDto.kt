package com.gurkha.model.attendance.attendanceStatus

import kotlinx.serialization.Serializable


@Serializable
data class AttendanceStatusRequestDto(
    val attendanceStatus: String,
    val employeeName: String,
    val isSelf: String
)

@Serializable
data class AttendanceStatusResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: List<AttendanceDetailDto>? = null,
    val success: Boolean? = null
)

@Serializable
data class AttendanceDetailDto(
    val employeeName: String? = null,
    val employeeType: String? = null,
    val requestedDate: String? = null,
    val requestRemarks: String? = null,
    val clockInTime: String? = null,
    val clockOutTime: String? = null,
    val assignedTo: String? = null,
    val approvedRemarks: String? = null,
    val lastModifiedBy: String? = null,
    val lastModifiedDate: String? = null,
    val attendanceId: Int? = null,
    val attendanceStatus: String? = null,
    val createdDate: String? = null
)
