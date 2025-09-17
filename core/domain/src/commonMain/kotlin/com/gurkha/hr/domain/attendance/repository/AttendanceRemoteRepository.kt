package com.gurkha.hr.domain.attendance.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendance.AttendanceResponseDto

interface AttendanceRemoteRepository {
    suspend fun fetchAttendance(
        dateFrom: String,
        toDate: String,
        enableManualAttendance: String,
        branchId: String?
    ): ERPResult<AttendanceResponseDto, DataError>
}