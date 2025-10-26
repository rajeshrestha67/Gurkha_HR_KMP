package com.gurkha.hr.domain.attendance.attendanceSummary.model

data class AttendanceSummaryData(
    val pendingAttendanceCount: Int,
    val approvedAttendanceCount: Int,
    val rejectedAttendanceCount: Int,
    val forgottenAttendanceDaysCount: Int,
)
