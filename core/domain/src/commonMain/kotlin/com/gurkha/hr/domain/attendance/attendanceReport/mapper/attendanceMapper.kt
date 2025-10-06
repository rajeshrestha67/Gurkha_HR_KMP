package com.gurkha.hr.domain.attendance.attendanceReport.mapper

import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.model.attendance.attendanceReport.AttendanceResponseDto


fun AttendanceResponseDto.toData(): List<AttendanceData> {
    return detail?.map {
        AttendanceData(
            workingHrs = it.workingHrs ?: "",
            clockInTime = it.clockInTime ?: "",
            clockOutTime = it.clockOutTime ?: "",
        )
    }?: emptyList()
}