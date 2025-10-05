package com.gurkha.hr.data.attendanceStatus

import com.gurkha.hr.domain.attendance.attendanceStatus.repository.AttendanceStatusRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusRequestDto
import com.gurkha.model.attendance.attendanceStatus.AttendanceStatusResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorAttendanceStatusRemoteRepository(
    private val httpClient: HttpClient
): AttendanceStatusRemoteRepository {
    override suspend fun fetchAttendanceStatus(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ): ERPResult<AttendanceStatusResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ATTENDANCE_STATUS_REPORT_ENDPOINT,
            ){
                setBody(AttendanceStatusRequestDto(attendanceStatus, employeeName, isSelf))
            }
        }
    }
}