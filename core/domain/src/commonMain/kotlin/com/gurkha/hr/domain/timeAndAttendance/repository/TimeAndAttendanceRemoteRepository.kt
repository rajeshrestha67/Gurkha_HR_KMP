package com.gurkha.hr.domain.timeAndAttendance.repository

import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendance.AttendanceResponseDto
import com.gurkha.model.timeandAttendance.TimeAttendanceReportResponseDTO

interface TimeAndAttendanceRemoteRepository {
    suspend fun fetchTimeAndAttendance(
        dateFrom: String,
        toDate: String
    ): ERPResult<TimeAttendanceReportResponseDTO, DataError>
}