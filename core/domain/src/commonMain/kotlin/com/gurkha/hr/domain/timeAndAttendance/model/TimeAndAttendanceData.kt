package com.gurkha.hr.domain.timeAndAttendance.model

data class TimeAndAttendanceData (
    val date: String,
    val day: String,
    val clockInTime: String,
    val clockOutTime: String,
    val status: String,
    val isPresent: Boolean,
    val isHoliday: Boolean,
    val isLate: Boolean,
    val isEarlyOut: Boolean,
)


