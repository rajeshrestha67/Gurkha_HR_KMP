package com.gurkha.hr.domain.attendance.attendanceReport.model

data class AttendanceData(
    val workingHrs : String,
    val clockInTime : String,
    val clockOutTime : String,
    val date: String,
    val day: String,
    val status: String,
    val isPresent: Boolean,
    val isHoliday: Boolean,
    val isLate: Boolean,
    val isEarlyOut: Boolean,
    val employeeId: Int,
)