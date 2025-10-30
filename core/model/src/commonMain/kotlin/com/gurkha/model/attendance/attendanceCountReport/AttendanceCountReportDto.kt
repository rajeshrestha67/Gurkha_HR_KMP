package com.gurkha.model.attendance.attendanceCountReport

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceCountReportResponseDto(
    val status: String? = null,
    val message: String? = null,
    val detail: AttendanceDetailDto? = null,
    val success: Boolean? = null
)

@Serializable
data class AttendanceDetailDto(
    val PRESENT: Int? = null,
    val ABSENT: Int? = null
)
