package com.gurkha.hr.domain.attendance.attendanceReport.mapper

import com.gurkha.hr.domain.attendance.attendanceReport.model.AttendanceData
import com.gurkha.model.attendance.attendanceReport.AttendanceResponseDto


fun AttendanceResponseDto.toData(): List<AttendanceData> {
    return detail?.map {
        AttendanceData(
            workingHrs = it.workingHrs ?: "",
            date = it.created ?: "",
            day = it.dayOfWeek ?: "",
            clockInTime = it.clockInTime ?: "--|--",
            clockOutTime = it.clockOutTime ?: "--|--",
            status = it.attendanceStatus ?: "",
            isPresent = it.onLeave ?: false,
            isHoliday = it.holiday ?: false,
            isLate = it.isLate ?: false,
            isEarlyOut = it.isLeaveEarly ?: false,
            employeeId = it.employeeId?: 0,
        )
    }?: emptyList()
}