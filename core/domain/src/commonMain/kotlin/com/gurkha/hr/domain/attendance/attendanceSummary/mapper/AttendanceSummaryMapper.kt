package com.gurkha.hr.domain.attendance.attendanceSummary.mapper

import com.gurkha.hr.domain.attendance.attendanceSummary.model.AttendanceSummaryData
import com.gurkha.model.attendance.attendanceSummary.AttendanceSummaryResponseDto

fun AttendanceSummaryResponseDto.toData(): AttendanceSummaryData{
    return AttendanceSummaryData(
        pendingAttendanceCount = detail?.pendingAttendanceCount ?: 0,
        approvedAttendanceCount = detail?.approvedAttendanceCount ?: 0,
        rejectedAttendanceCount = detail?.rejectedAttendanceCount ?: 0,
        forgottenAttendanceDaysCount = detail?.forgottenAttendanceDaysCount ?: 0
    )
}