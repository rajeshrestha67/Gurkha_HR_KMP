package com.gurkha.hr.domain.attendance.attendanceRequest.mapper

import com.gurkha.hr.domain.attendance.attendanceRequest.model.AttendanceRequestData
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestResponseDto

fun AttendanceRequestResponseDto.toData(): AttendanceRequestData{
    return AttendanceRequestData(
        status = status ?: "",
        message = message ?: "",
        success = success ?: false
    )
}