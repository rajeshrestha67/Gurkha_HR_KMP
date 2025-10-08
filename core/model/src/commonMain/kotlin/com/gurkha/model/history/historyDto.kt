package com.gurkha.model.history

import kotlinx.serialization.Serializable

@Serializable
data class HistoryResponseDTO(
    val status: String,
    val message: String,
    val detail: List<AttendanceDetail>,
    val success: Boolean
)
@Serializable
data class AttendanceDetail(
    val mapId: String,
    val fullName: String,
    val imageUrl: String?,
    val fromDate: String,
    val toDate: String,
    val dailyAttendance: List<DailyAttendance>
)
@Serializable
data class DailyAttendance(
    val dateBs: String,
    val day: String,
    val employeeId: Int,
    val clockInTime: String?,        // Nullable because some days are null
    val clockOutTime: String?,       // Nullable because some days are null
    val attendanceStatus: String,
    val workedHours: String?,
    val lateInTime: String?,
    val earlyOutTime: String?,
    val leaveRequestStatus: String?,
    val attendanceRequestStatus: String?,
    val leaveApproverRemarks: String?,
    val attendanceApproverRemarks: String?,
    val assigneeName: String?,
    val remarks: String?,
    val attendanceRequestedDate: String?,
    val leaveDuration: String?
)

@Serializable
data class HistoryRequestDTO(
    val fromDate: String,
    val toDate: String
)
