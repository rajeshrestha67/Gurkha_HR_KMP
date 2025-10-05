package com.gurkha.hr.data.attendance

import com.gurkha.hr.domain.attendance.attendanceReport.repository.AttendanceRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.attendance.attendanceReport.AttendanceRequestDto
import com.gurkha.model.attendance.attendanceReport.AttendanceResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorAttendanceRemoteRepository(
    val httpClient: HttpClient
) : AttendanceRemoteRepository {
    override suspend fun fetchAttendance(
        dateFrom: String,
        toDate: String
    ): ERPResult<AttendanceResponseDto, DataError> {
        return  safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.FETCH_ATTENDANCE_END_POINT
            ){
                setBody(AttendanceRequestDto(dateFrom, toDate))
            }
        }
    }
}