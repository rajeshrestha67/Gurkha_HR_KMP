package com.gurkha.model.attendance.doAttendance

import kotlinx.serialization.Serializable

@Serializable
data class DoAttendanceRequestDto(
    val employeeId : Int,
    val forDate: String,
    val imageName: String
)

@Serializable
data class DoAttendanceResponseDto(
    val success: Boolean,
    val message: String,
    val detail: String,
    val status: String
)