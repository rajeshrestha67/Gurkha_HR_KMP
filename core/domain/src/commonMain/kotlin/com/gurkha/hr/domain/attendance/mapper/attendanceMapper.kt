package com.gurkha.hr.domain.attendance.mapper

import com.gurkha.hr.domain.attendance.model.AttendanceData
import com.gurkha.model.attendance.AttendanceResponseDto


fun AttendanceResponseDto.toData(): List<AttendanceData> {
    return detail?.map {
        AttendanceData(
            workingHrs = it.workingHrs ?: "",
            clockInTime = it.clockInTime ?: "",
            clockOutTime = it.clockOutTime ?: "",
        )
    }?: emptyList()
}