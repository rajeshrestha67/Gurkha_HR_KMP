package com.gurkha.hr.domain.attendanceStatus.mapper

import com.gurkha.hr.domain.attendanceStatus.model.AttendanceStatusData
import com.gurkha.model.attendanceStatus.AttendanceStatusResponseDto

fun AttendanceStatusResponseDto.toData(): List<AttendanceStatusData> {
    return detail?.map {
        AttendanceStatusData(
            requestedDate = it.requestedDate ?: "",
            requestRemarks = it.requestRemarks ?: "",
            clockInTime = it.clockInTime ?: "",
            clockOutTime = it.clockOutTime ?: "",
            assignedTo = it.assignedTo ?: "",
            approvedRemarks = it.approvedRemarks ?: "",
            lastModifiedBy = it.lastModifiedBy ?: "",
            lastModifiedDate = it.lastModifiedDate ?: "",
            attendanceStatus = it.attendanceStatus ?: "",
        )
    } ?: emptyList()
}
