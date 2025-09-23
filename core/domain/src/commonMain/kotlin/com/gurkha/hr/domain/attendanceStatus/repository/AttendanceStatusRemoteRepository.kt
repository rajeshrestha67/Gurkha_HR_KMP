package com.gurkha.hr.domain.attendanceStatus.repository

import com.gurkha.hr.domain.attendanceStatus.model.AttendanceStatusData
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendanceStatus.AttendanceStatusResponseDto

interface AttendanceStatusRemoteRepository {
    suspend fun fetchAttendanceStatus(attendanceStatus: String,employeeName: String, isSelf: String): ERPResult<AttendanceStatusResponseDto, DataError>
}