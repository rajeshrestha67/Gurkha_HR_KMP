package com.gurkha.hr.domain.attendance.attendanceStatus.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusResponseDto

interface AttendanceStatusRemoteRepository {
    suspend fun fetchAttendanceStatus(attendanceStatus: String,employeeName: String, isSelf: String): ERPResult<AttendanceStatusResponseDto, DataError>
}