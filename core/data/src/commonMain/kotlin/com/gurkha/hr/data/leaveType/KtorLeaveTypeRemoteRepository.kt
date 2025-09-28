package com.gurkha.hr.data.leaveType

import com.gurkha.hr.domain.leaveType.repository.LeaveTypeRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.leaveType.LeaveTypeResponseDto
import io.ktor.client.HttpClient

class KtorLeaveTypeRemoteRepository(
    private val httpClient: HttpClient
) : LeaveTypeRemoteRepository {
    override suspend fun fetchLeaveType(): ERPResult<LeaveTypeResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_TYPE_END_POINT
            )
        }
    }
}