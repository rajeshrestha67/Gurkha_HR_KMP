package com.gurkha.hr.domain.attendanceStatus.model

data class AttendanceStatusData(
    val requestedDate : String,
    val requestRemarks : String,
    val clockInTime : String,
    val clockOutTime : String,
    val assignedTo : String,
    val approvedRemarks : String,
    val lastModifiedBy : String,
    val lastModifiedDate : String,
    val attendanceStatus : String,
)