package com.gurkha.hr.domain.attendance.doAttendance.mapper

import com.gurkha.hr.domain.attendance.doAttendance.model.DoAttendanceData
import com.gurkha.model.attendance.doAttendance.DoAttendanceResponseDto

fun DoAttendanceResponseDto.toData(): DoAttendanceData{
    return DoAttendanceData(
        message =  message ?: ""
    )
}