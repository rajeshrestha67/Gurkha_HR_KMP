package com.gurkha.hr.data.leaveRequest

import com.gurkha.hr.domain.leave.leaveRequest.repository.LeaveRemoteRepository
import com.gurkha.hr.networkhelper.BaseUrl
import com.gurkha.hr.networkhelper.DataError
import com.gurkha.hr.networkhelper.ERPResult
import com.gurkha.hr.networkhelper.EndPoint
import com.gurkha.hr.networkhelper.get
import com.gurkha.hr.networkhelper.safeCall
import com.gurkha.model.leave.leaveAssignee.LeaveAssigneeResponseDto
import com.gurkha.model.leave.leaveType.LeaveTypeResponseDto
import io.ktor.client.HttpClient

class KtorLeaveRequestRemoteRepository(
    private val httpClient: HttpClient
) : LeaveRemoteRepository {
    override suspend fun fetchLeaveType(): ERPResult<LeaveTypeResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_TYPE_END_POINT
            )
        }
    }

    override suspend fun fetchAssignee(): ERPResult<LeaveAssigneeResponseDto, DataError> {
        return safeCall {
            httpClient.get(
                baseUrl = BaseUrl.Generic,
                endPoint = EndPoint.LEAVE_ASSIGNEE_END_POINT
            )
        }
    }
}