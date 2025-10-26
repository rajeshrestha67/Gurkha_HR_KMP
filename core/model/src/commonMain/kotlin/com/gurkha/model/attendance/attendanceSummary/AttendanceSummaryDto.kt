package com.gurkha.model.attendance.attendanceSummary

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceSummaryResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: AttendanceDetailDto? = null,
    val success: Boolean? = null
)

@Serializable
data class AttendanceDetailDto(
    val pendingAttendanceCount: Int? = null,
    val approvedAttendanceCount: Int? = null,
    val rejectedAttendanceCount: Int? = null,
    val forgottenAttendanceDaysCount: Int? = null
)