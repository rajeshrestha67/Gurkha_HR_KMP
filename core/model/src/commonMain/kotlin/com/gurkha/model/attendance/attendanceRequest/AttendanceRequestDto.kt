package com.gurkha.model.attendance.attendanceRequest

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequestDto(
    val assigneeId: Int? = null,
    val date : String? = null,
    val clockInTime: String? = null,
    val clockOutTime: String? = null,
    val remarks : String? = null
)


@Serializable
data class AttendanceRequestResponseDto(
    val status: String? = null,
    val message: String? = null,
    val success: Boolean? = null
)
