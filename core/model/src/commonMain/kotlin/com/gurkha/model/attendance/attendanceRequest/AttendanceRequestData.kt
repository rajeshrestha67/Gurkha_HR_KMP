package com.gurkha.model.attendance.attendanceRequest

import kotlinx.serialization.Serializable

@Serializable
data class AttendanceRequestData(
    val assigneeId: String,
    val date: String,
    val clockInTime: String,
    val clockOutTime: String,
    val remarks: String
)

