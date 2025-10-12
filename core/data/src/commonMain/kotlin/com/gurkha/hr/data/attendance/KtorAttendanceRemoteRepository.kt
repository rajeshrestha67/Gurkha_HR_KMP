package com.gurkha.hr.data.attendance

import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.attendance.attendanceReport.AttendanceReportRequestDto
import com.gurkha.model.attendance.attendanceReport.AttendanceResponseDto
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestDto
import com.gurkha.model.attendance.attendanceRequest.AttendanceRequestResponseDto
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusRequestDTO
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusResponseDTO
import com.gurkha.model.attendance.attendanceSummary.AttendanceSummaryResponseDto
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorAttendanceRemoteRepository(
    val httpClient: HttpClient
) : AttendanceRemoteRepository {
    override suspend fun fetchAttendance(
        dateFrom: String,
        toDate: String
    ): ERPResult<AttendanceResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.FETCH_ATTENDANCE_END_POINT
            ) {
                setBody(AttendanceReportRequestDto(dateFrom, toDate))
            }
        }
    }

    override suspend fun fetchAttendanceStatus(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ): ERPResult<AttendanceStatusResponseDTO, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ATTENDANCE_STATUS_REPORT_ENDPOINT,
            ) {
                setBody(AttendanceStatusRequestDTO(attendanceStatus, employeeName, isSelf))
            }
        }
    }

    override suspend fun requestAttendance(
        assigneeId: Int,
        clockInTime: String?,
        clockOutTime: String?,
        date: String,
        remarks: String
    ): ERPResult<AttendanceRequestResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ATTENDANCE_REQUEST_END_POINT
            ){
                setBody(AttendanceRequestDto(
                    assigneeId = assigneeId,
                    clockInTime = clockInTime,
                    clockOutTime = clockOutTime,
                    date = date,
                    remarks = remarks
                ))
            }
        }
    }

    override suspend fun fetchAttendanceSummary(): ERPResult<AttendanceSummaryResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ATTENDANCE_SUMMARY_END_POINT
            )
        }
    }
}