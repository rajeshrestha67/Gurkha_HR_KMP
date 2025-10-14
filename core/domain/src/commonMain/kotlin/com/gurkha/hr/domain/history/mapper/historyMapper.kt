package com.gurkha.hr.domain.history.mapper

import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.model.history.HistoryResponseDTO

fun HistoryResponseDTO.toData(): List<HistoryData> {
    return detail?.flatMap { attendanceDetail ->
        attendanceDetail.dailyAttendance.map { daily ->
            HistoryData(
                date = daily.dateBs ?: "",
                day = daily.day,
                clockInTime = daily.clockInTime ?: "",
                clockOutTime = daily.clockOutTime ?: "",
                status = daily.attendanceStatus ?: "",
                isPresent = daily.attendanceStatus == "Present",
                isHoliday = daily.attendanceStatus == "Holiday",
                isAbsent = daily.attendanceStatus == "Absent",
                lateInTime = daily.lateInTime ?: "",
                earlyOutTime = daily.earlyOutTime ?: "",
                assigneeName = daily.assigneeName ?: "",
                remarks = daily.remarks?:"",
                assigneeStatus = daily.attendanceRequestStatus?:"",
                response = daily.attendanceApproverRemarks?:"",
                leaveRequestStatus = daily.leaveRequestStatus?:"",
                leaveApproverRemarks = daily.leaveApproverRemarks?:"",
                leaveDuration = daily.leaveDuration?:"",
                attendanceStatus = daily.attendanceStatus
            )
        }
    } ?: emptyList()
}
