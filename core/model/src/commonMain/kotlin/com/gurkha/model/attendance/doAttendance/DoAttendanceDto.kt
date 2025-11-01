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
    val success: Boolean? = null,
    val message: String ? = null,
    val detail: String ?= null,
    val status: String? = null
)