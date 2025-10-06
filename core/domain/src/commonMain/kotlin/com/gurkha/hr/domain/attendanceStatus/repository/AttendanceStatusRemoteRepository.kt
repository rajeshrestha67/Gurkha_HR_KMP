package com.gurkha.hr.domain.attendanceStatus.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendanceStatus.AttendanceStatusResponseDto
import com.gurkha.model.network.DataError

interface AttendanceStatusRemoteRepository {
    suspend fun fetchAttendanceStatus(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ): ERPResult<AttendanceStatusResponseDto, DataError>
}