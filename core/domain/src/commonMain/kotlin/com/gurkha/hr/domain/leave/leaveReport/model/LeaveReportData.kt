package com.gurkha.hr.domain.leave.leaveReport.model

data class LeaveReportData(
    val employeeId: Int,
    val startDate: String,
    val endDate: String,
    val leaveStatus: String,
    val reason: String,
    val leaveDuration: String,
    val assigneeName: String,
    val totalDays: Double,
    val leaveType: String,
    val requestedDate: String,
)
