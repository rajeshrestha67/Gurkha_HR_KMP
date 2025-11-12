package com.gurkha.hr.domain.history.mapper

import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.model.history.HistoryResponseDTO

fun HistoryResponseDTO.toData(): List<HistoryData> {
    return detail.flatMap { attendanceDetail ->
        attendanceDetail.dailyAttendance.map { daily ->
            HistoryData(
                date = daily.dateBs,
                day = daily.day,
                clockInTime = getTimeOnly(daily.clockInTime),
                clockOutTime = getTimeOnly(daily.clockOutTime),
                status = daily.attendanceStatus,
                isPresent = daily.attendanceStatus.equals("P", true),
                isHoliday = daily.attendanceStatus.equals("H", true),
                isAbsent = daily.attendanceStatus.equals("A", true),
                lateInTime = daily.lateInTime ?: "",
                earlyOutTime = daily.earlyOutTime ?: "",
                assigneeName = daily.assigneeName ?: "",
                remarks = daily.remarks ?: " - ",
                assigneeStatus = daily.attendanceRequestStatus ?: " - ",
                response = daily.attendanceApproverRemarks ?: " - ",
                leaveRequestStatus = daily.leaveRequestStatus ?: " - ",
                leaveApproverRemarks = daily.leaveApproverRemarks ?: " - ",
                leaveDuration = daily.leaveDuration ?: "",
                attendanceStatus = daily.attendanceStatus
            )
        }
    }
}

fun getDateOnly(timestamp: String?): String {
    if (timestamp.isNullOrBlank()) return ""
    return timestamp.split("T").firstOrNull() ?: ""
}

fun getTimeOnly(timestamp: String?): String {
    if (timestamp.isNullOrBlank()) return ""
    return timestamp.split("T").getOrNull(1)?.split(".")?.firstOrNull() ?: ""
}