package com.gurkha.hr.domain.attendance.attendanceReport.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendance.attendanceReport.AttendanceResponseDto
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusResponseDto
import com.gurkha.model.network.DataError

interface AttendanceRemoteRepository {
    suspend fun fetchAttendance(
        dateFrom: String,
        toDate: String,
    ): ERPResult<AttendanceResponseDto, DataError>

    suspend fun fetchAttendanceStatus(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ): ERPResult<AttendanceStatusResponseDto, DataError>
}