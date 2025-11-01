package com.gurkha.hr.domain.attendance.attendanceCountReport.mapper

import com.gurkha.hr.domain.attendance.attendanceCountReport.model.AttendanceCountReportData
import com.gurkha.model.attendance.attendanceCountReport.AttendanceCountReportResponseDto

fun AttendanceCountReportResponseDto.toData(): AttendanceCountReportData{
    return AttendanceCountReportData(
        present = detail?.PRESENT ?: 0,
        absent = detail?.ABSENT ?: 0
    )
}