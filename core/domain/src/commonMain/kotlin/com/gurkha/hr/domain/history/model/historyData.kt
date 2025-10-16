package com.gurkha.hr.domain.history.model

data class HistoryData(
    val date: String,
    val day: String,
    val clockInTime: String,
    val clockOutTime: String,
    val status: String,
    val isPresent: Boolean,
    val isHoliday: Boolean,
    val isAbsent: Boolean,
    val lateInTime: String,
    val earlyOutTime: String,
    val assigneeName: String,
    val remarks: String,
    val response: String,
    val assigneeStatus: String,
    val leaveRequestStatus: String,
    val leaveApproverRemarks: String,
    val leaveDuration: String,
    val attendanceStatus: String

)