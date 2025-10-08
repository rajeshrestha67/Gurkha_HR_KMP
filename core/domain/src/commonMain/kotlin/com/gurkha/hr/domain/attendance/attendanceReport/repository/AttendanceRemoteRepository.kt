package com.gurkha.hr.domain.attendance.attendanceReport.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.ErrorData
import com.gurkha.model.attendance.attendanceReport.AttendanceResponseDto
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestResponseDto
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusResponseDTO
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
    ): ERPResult<AttendanceStatusResponseDTO, DataError>

    suspend fun requestAttendance(
        assigneeId : Int,
        clockInTime : String,
        clockOutTime : String,
        date : String,
        remarks : String
    ): ERPResult<AttendanceRequestResponseDto, DataError>
}