package com.gurkha.hr.domain.attendance.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendance.AttendanceResponseDto
import com.gurkha.model.network.DataError

interface AttendanceRemoteRepository {
    suspend fun fetchAttendance(
        dateFrom: String,
        toDate: String,
    ): ERPResult<AttendanceResponseDto, DataError>
}