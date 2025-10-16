package com.gurkha.hr.domain.history.mapper

import androidx.compose.ui.graphics.Color
import com.gurkha.hr.domain.history.model.HistoryData
import com.gurkha.model.history.HistoryResponseDTO
import com.gurkha.model.history.ui.AttendanceStatusColorUi

fun HistoryResponseDTO.toData(): List<HistoryData> {
    return detail.flatMap { attendanceDetail ->
        attendanceDetail.dailyAttendance.map { daily ->
            HistoryData(
                date = daily.dateBs ,
                day = daily.day,
                clockInTime = daily.clockInTime?: "-",
                clockOutTime = daily.clockOutTime ?: " - ",
                status = daily.attendanceStatus,
                isPresent = daily.attendanceStatus.equals("P", true),
                isHoliday = daily.attendanceStatus.equals("H", true),
                isAbsent = daily.attendanceStatus.equals("A", true) ,
                lateInTime = daily.lateInTime ?: "",
                earlyOutTime = daily.earlyOutTime ?: "",
                assigneeName = daily.assigneeName ?: "",
                remarks = daily.remarks?:" - ",
                assigneeStatus = daily.attendanceRequestStatus?:" - ",
                response = daily.attendanceApproverRemarks?:" - ",
                leaveRequestStatus = daily.leaveRequestStatus?:" - ",
                leaveApproverRemarks = daily.leaveApproverRemarks?:" - ",
                leaveDuration = daily.leaveDuration?:"",
                attendanceStatus = daily.attendanceStatus
            )
        }
    }
}

