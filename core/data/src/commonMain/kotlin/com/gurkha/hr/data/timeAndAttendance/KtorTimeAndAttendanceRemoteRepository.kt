package com.gurkha.hr.data.timeAndAttendance

import com.gurkha.hr.domain.timeAndAttendance.repository.TimeAndAttendanceRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.post
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.network.DataError
import com.gurkha.model.timeandAttendance.TimeAttendanceReportRequestDTO
import com.gurkha.model.timeandAttendance.TimeAttendanceReportResponseDTO
import io.ktor.client.HttpClient
import io.ktor.client.request.setBody

class KtorTimeAndAttendanceRemoteRepository(private val httpClient: HttpClient) :
    TimeAndAttendanceRemoteRepository {
    override suspend fun fetchTimeAndAttendance(
        dateFrom: String,
        toDate: String
    ): ERPResult<TimeAttendanceReportResponseDTO, DataError> {
        return safeCall {
            httpClient.post(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.TIME_AND_ATTENDANCE_ENDPOINT
            ) {
                setBody(
                    TimeAttendanceReportRequestDTO(
                        toDate = toDate,
                        fromDate = dateFrom
                    )
                )
            }
        }
    }


}