package com.gurkha.hr.data.attendanceStatus

import com.gurkha.hr.domain.attendanceStatus.repository.AttendanceStatusRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.attendanceStatus.AttendanceStatusRequestDto
import com.gurkha.model.attendanceStatus.AttendanceStatusResponseDto
import com.gurkha.model.network.DataError
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorAttendanceStatusRemoteRepository(
    private val httpClient: HttpClient
) : AttendanceStatusRemoteRepository {
    override suspend fun fetchAttendanceStatus(
        attendanceStatus: String,
        employeeName: String,
        isSelf: String
    ): ERPResult<AttendanceStatusResponseDto, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.ATTENDANCE_STATUS_REPORT_ENDPOINT,
            ) {
                setBody(AttendanceStatusRequestDto(attendanceStatus, employeeName, isSelf))
            }
        }
    }
}