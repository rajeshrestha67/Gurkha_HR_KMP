package com.gurkha.hr.domain.attendance.attendanceReport.repository

import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.model.attendance.attendanceReport.AttendanceResponseDto
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestResponseDto
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusResponseDTO
import com.gurkha.model.attendance.attendanceSummary.AttendanceSummaryResponseDto
import com.gurkha.model.attendance.doAttendance.DoAttendanceResponseDto
import com.gurkha.model.network.DataError

interface AttendanceRemoteRepository {
    suspend fun fetchAttendance(
        dateFrom: String? = null,
        toDate: String? = null,
        attendanceStatus: String? = null,
        employeeId: Int? = null
    ): ERPResult<AttendanceResponseDto, DataError>

    suspend fun fetchAttendanceStatus(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ): ERPResult<AttendanceStatusResponseDTO, DataError>

    suspend fun requestAttendance(
        assigneeId : Int,
        clockInTime : String?,
        clockOutTime : String?,
        date : String,
        remarks : String
    ): ERPResult<AttendanceRequestResponseDto, DataError>

    suspend fun fetchAttendanceSummary(): ERPResult<AttendanceSummaryResponseDto, DataError>

    suspend fun doAttendance(
        employeeId: Int,
        imageName: String,
        forDate: String
    ): ERPResult<DoAttendanceResponseDto, DataError>
}