package com.gurkha.hr.domain.attendance.model

import com.gurkha.model.attendance.AttendanceDetailResponse

data class AttendanceData(
    val status: String,
    val message: String,
    val detail: List<AttendanceDetailResponse>,
    val success: Boolean
)