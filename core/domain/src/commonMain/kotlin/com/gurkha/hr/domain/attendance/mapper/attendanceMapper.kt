package com.gurkha.hr.domain.attendance.mapper

import com.gurkha.hr.domain.attendance.model.AttendanceData
import com.gurkha.model.attendance.AttendanceResponseDto


fun AttendanceResponseDto.toData(): AttendanceData {
    return AttendanceData(
        status = status ?: "",
        detail = detail ?: emptyList(),
        message = message ?: "",
        success = success ?: false,
    )
}